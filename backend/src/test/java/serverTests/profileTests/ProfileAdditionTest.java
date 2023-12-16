package serverTests.profileTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Position;
import Matchmaking.SkillCalculators.SimpleSkill;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import datastorage.SimpleDataStore;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerSharedState;
import server.exceptions.NoItemFoundException;
import server.handlers.match.MatchEndHandler;
import server.handlers.match.MatchViewHandler;
import server.handlers.profile.ProfileAddHandler;
import server.handlers.profile.ProfileEditHandler;
import server.handlers.profile.ProfileViewHandler;
import server.handlers.queue.QueueAddHandler;
import server.handlers.queue.QueueViewHandler;
import spark.Spark;

/**
 * Test class for the profile addition handler. Every component outside the server is being mocked
 * with a simpler verson of the final product (using simple data stores instead of the final
 * firebase for example)
 */
public class ProfileAdditionTest {

  @BeforeAll
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in console for test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;

  private final ServerSharedState sharedState =
      new ServerSharedState(
          new SimpleDataStore(), new CourtAssigner(6, new SimpleMatchMaker(), new SimpleSkill()));

  @BeforeEach
  public void setup() throws FileNotFoundException {
    Spark.get("profile-add", new ProfileAddHandler(sharedState));
    Spark.get("profile-edit", new ProfileEditHandler(sharedState));
    Spark.get("profile-view", new ProfileViewHandler(sharedState));
    Spark.get("match-end", new MatchEndHandler(sharedState));
    Spark.get("match-view", new MatchViewHandler(sharedState));
    Spark.get("queue-add", new QueueAddHandler(sharedState));
    Spark.get("queue-view", new QueueViewHandler(sharedState));

    Spark.init();
    Spark.awaitInitialization();

    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("profile-add");
    Spark.unmap("profile-edit");
    Spark.unmap("profile-view");
    Spark.unmap("match-end");
    Spark.unmap("match-view");
    Spark.unmap("queue-add");
    Spark.unmap("queue-view");
    Spark.awaitStop();
  }

  @Test
  public void testAPICode200() throws IOException {
    HttpURLConnection clientConnection = tryRequest("profile-add");
    assertEquals(200, clientConnection.getResponseCode());
  }

  /**
   * Test that the handler can handle potentially thousands of profiles being added - Simple
   * database
   *
   * @throws IOException
   */
  @Test
  public void testAPIProfileAdditionFuzz() throws IOException {

    for (int i = 0; i < 1000; i++) {
      HttpURLConnection clientConnection =
          tryRequest("profile-add?name=john johnson?position=FRONT_GUARD");
      assertEquals(200, clientConnection.getResponseCode());

      // The body of the string contains the proper data - "Success"
      Map<String, Object> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      assert body != null;

      // Checks that the player has been added and that the playerID is the correct length
      assertEquals("success", body.get("result"));
      assertEquals(20, body.get("playerID").toString().length());
    }
  }

  /**
   * Tests that the profile-add handler correctly adds a player to the database through established
   * methods in the database interface. Additionally, it checks that the IDs are correct both within
   * the player object and how it is referenced through the map.
   *
   * @throws IOException
   * @throws NoItemFoundException
   */
  @Test
  public void testFiveAdditionsCorrectPlayers() throws IOException, NoItemFoundException {
    HttpURLConnection clientConnection1 =
        tryRequest("profile-add?name=john johnson?position=FRONT_GUARD&id=1234567");
    HttpURLConnection clientConnection2 =
        tryRequest("profile-add?name=Back McBackend?position=CENTER&id=siufgb2ihb");
    HttpURLConnection clientConnection3 =
        tryRequest("profile-add?name=Nim Telson?position=SMALL_FORWARD&id=soduvbwi234");
    HttpURLConnection clientConnection4 =
        tryRequest("profile-add?name=Hoop Hoopington?position=POWER_FORWARD&id=q932rfbsabBEWOB3");
    HttpURLConnection clientConnection5 =
        tryRequest("profile-add?name=aaaa?position=CENTER&id=FE7DF3esfDFF");
    assertEquals(200, clientConnection1.getResponseCode());
    assertEquals(200, clientConnection2.getResponseCode());
    assertEquals(200, clientConnection3.getResponseCode());
    assertEquals(200, clientConnection4.getResponseCode());
    assertEquals(200, clientConnection5.getResponseCode());

    HttpURLConnection[] clientConnections = {
      clientConnection1, clientConnection2, clientConnection3, clientConnection4, clientConnection5
    };

    LinkedList<Map<String, Object>> bodies = new LinkedList<>();

    String[] ids = new String[5];

    for (int i = 0; i < clientConnections.length; i++) {
      bodies.add(adapter.fromJson(new Buffer().readFrom(clientConnections[i].getInputStream())));
      assert bodies.get(i) != null;
      assertEquals("success", bodies.get(i).get("result"));
      ids[i] = bodies.get(i).get("id").toString();
      assertEquals(ids[i], sharedState.getDataStore().getPlayer(ids[i]).getId());
    }

    // Now that we have the players added, we can check their validity.

    assertEquals("john johnson", sharedState.getDataStore().getPlayer(ids[0]).getName());
    assertEquals("Back McBackend", sharedState.getDataStore().getPlayer(ids[1]).getName());
    assertEquals("Nim Telson", sharedState.getDataStore().getPlayer(ids[2]).getName());
    assertEquals("Hoop Hoopington", sharedState.getDataStore().getPlayer(ids[3]).getName());
    assertEquals("aaaa", sharedState.getDataStore().getPlayer(ids[4]).getName());

    assertEquals(
        Position.valueOf("FRONT_GUARD"),
        sharedState.getDataStore().getPlayer(ids[0]).getPosition());
    assertEquals(
        Position.valueOf("CENTER"), sharedState.getDataStore().getPlayer(ids[1]).getPosition());
    assertEquals(
        Position.valueOf("SMALL_FORWARD"),
        sharedState.getDataStore().getPlayer(ids[2]).getPosition());
    assertEquals(
        Position.valueOf("POWER_FORWARD"),
        sharedState.getDataStore().getPlayer(ids[3]).getPosition());
    assertEquals(
        Position.valueOf("CENTER"), sharedState.getDataStore().getPlayer(ids[4]).getPosition());
  }
}
