package serverTests.profileTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Player;
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

  @Test
  public void testAPIFailureStates() throws IOException {
    // profile-add
    HttpURLConnection clientConnection = tryRequest("profile-add");
    assertEquals(200, clientConnection.getResponseCode());

    // The body of the string contains the proper data - "Success"
    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("error_bad_request", body.get("result"));
    assertEquals("Error in specifying 'id' variable: id neccesary", body.get("details"));

    // profile-add w/ id
    clientConnection = tryRequest("profile-add?id=1234567890");
    assertEquals(200, clientConnection.getResponseCode());

    // The body of the string contains the proper data - "Success"
    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;

    assertEquals("error_bad_request", body.get("result"));
    assertEquals("Error in specifying 'name' variable: name neccesary", body.get("details"));

    // profile-add w/ id + name
    clientConnection = tryRequest("profile-add?id=1234567890&name=JoshJoshington");
    assertEquals(200, clientConnection.getResponseCode());

    // The body of the string contains the proper data - "Success"
    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;

    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "Error in specifying 'id' variable. "
            + "Please use 'POINT_GUARD', 'SHOOTING_GUARD', 'SMALL_FORWARD', 'POWER_FORWARD', or "
            + "'CENTER' in your position query: position query neccesary",
        body.get("details"));

    // profile-add - correct
    clientConnection = tryRequest("profile-add?name=johnjohnson&position=POINT_GUARD&id=1234567");
    assertEquals(200, clientConnection.getResponseCode());

    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("success", body.get("result"));

    // profile-add - correct
    clientConnection = tryRequest("profile-add?name=johnjohnson&position=POINT_GUARD&id=1234567");
    assertEquals(200, clientConnection.getResponseCode());
    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "Player already exists in the database, please use edit handler instead: "
            + "Player to be added already exists within database. Please use updatePlayer in this "
            + "instance.",
        body.get("details"));
  }

  /**
   * Test that the handler can handle potentially thousands of profiles being added - Simple
   * database
   *
   * @throws IOException
   */
  @Test
  public void testAPIProfileAdditionFuzz() throws IOException {
    Player IDMaker = new Player("a", Position.CENTER);
    String request = "";
    for (int i = 0; i < 1000; i++) {
      request = "profile-add?name=johnjohnson&position=POINT_GUARD&id=" + IDMaker.generateID();
      HttpURLConnection clientConnection = tryRequest(request);
      assertEquals(200, clientConnection.getResponseCode());

      // The body of the string contains the proper data - "Success"
      Map<String, Object> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      assert body != null;

      // Checks that the player has been added or that the request had an ID the same as another one

      if (body.get("result").equals("success")) {
        assertEquals("success", body.get("result"));
      } else {
        assertEquals("error_bad_request", body.get("result"));
      }
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
        tryRequest("profile-add?name=johnjohnson&position=POINT_GUARD&id=1234567");
    HttpURLConnection clientConnection2 =
        tryRequest("profile-add?name=BackMcBackend&position=CENTER&id=siufgb2ihb");
    HttpURLConnection clientConnection3 =
        tryRequest("profile-add?name=NimTelson&position=SMALL_FORWARD&id=soduvbwi234");
    HttpURLConnection clientConnection4 =
        tryRequest("profile-add?name=HoopHoopington&position=POWER_FORWARD&id=q932rfbsabBEWOB3");
    HttpURLConnection clientConnection5 =
        tryRequest("profile-add?name=aaaa&position=CENTER&id=FE7DF3esfDFF");
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
      ids[i] = bodies.get(i).get("playerID").toString();
      assertEquals(ids[i], sharedState.getDataStore().getPlayer(ids[i]).getId());
    }

    // Now that we have the players added, we can check their validity.
    assertEquals("johnjohnson", sharedState.getDataStore().getPlayer(ids[0]).getName());
    assertEquals("BackMcBackend", sharedState.getDataStore().getPlayer(ids[1]).getName());
    assertEquals("NimTelson", sharedState.getDataStore().getPlayer(ids[2]).getName());
    assertEquals("HoopHoopington", sharedState.getDataStore().getPlayer(ids[3]).getName());
    assertEquals("aaaa", sharedState.getDataStore().getPlayer(ids[4]).getName());

    assertEquals(
        Position.valueOf("POINT_GUARD"),
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
