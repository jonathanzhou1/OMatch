package serverTests.queueTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ServerSharedState;
import server.handlers.match.MatchEndHandler;
import server.handlers.match.MatchViewHandler;
import server.handlers.profile.ProfileAddHandler;
import server.handlers.profile.ProfileEditHandler;
import server.handlers.profile.ProfileViewHandler;
import server.handlers.queue.QueueAddHandler;
import server.handlers.queue.QueueViewHandler;
import spark.Spark;

public class QueueAddTest {
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

  // Taken from edstem #397
  @AfterAll
  public static void shutdown() throws InterruptedException {
    Spark.stop();
    Thread.sleep(3000);
  }

  @Test
  public void testAPICode200() throws IOException {
    HttpURLConnection clientConnection = tryRequest("queue-add");
    assertEquals(200, clientConnection.getResponseCode());
  }

  /**
   * Tests that the queue add handler can successfully add players when the correct conditions are
   * met.
   */
  @Test
  public void testAddPlayers() throws IOException {
    HttpURLConnection clientConnection;

    // Add something to the queue - Testing ID failure state
    clientConnection = tryRequest("queue-add");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request", body.get("result"));
    assertEquals("Player Not Found: No Player found with corresponding ID", body.get("details"));

    // Add players to the queue:
    int playerAdded = 0;
    for (Player p : this.generatePlayerList()) {
      String apiCall = "profile-add?id=";
      apiCall += p.getId() + "&name=";
      apiCall += p.getName() + "&position=";
      apiCall += p.getPosition();
      clientConnection = tryRequest(apiCall);
      assertEquals(200, clientConnection.getResponseCode());

      clientConnection = tryRequest("queue-add?id=" + p.getId());
      // Check that the body is a success
      body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      System.out.println(body);
      assertEquals("success", body.get("result"));
      if (playerAdded > 8) {
        assertEquals("success", body.get("result"));
        assertTrue((Boolean) body.get("newCourtMade"));
        assertNotNull(body.get("court"));
      } else {
        assertFalse((Boolean) body.get("newCourtMade"));
      }
      // Call queue view again - Something should be there now
      clientConnection = tryRequest("queue-view?id=" + p.getId());
      assertEquals(200, clientConnection.getResponseCode());
      body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      assert body != null;
      assertEquals("success", body.get("result"));

      playerAdded++;
    }

    // Try a failed addition
    clientConnection = tryRequest("queue-add?id=" + "definitelyNotAnID");
    // Check that the body is a success
    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    System.out.println(body);
    assertEquals("error_bad_request", body.get("result"));
    assertEquals("Player Not Found: No Player found with corresponding ID", body.get("details"));
  }

  private List<Player> generatePlayerList() {
    List<Player> players = new LinkedList<>();
    Player testPlayer1 = new Player("a", Position.CENTER, "1");
    players.add(testPlayer1);
    Player testPlayer2 = new Player("b", Position.SMALL_FORWARD, "2");
    players.add(testPlayer2);
    Player testPlayer3 = new Player("c", Position.POINT_GUARD, "3");
    players.add(testPlayer3);
    Player testPlayer4 = new Player("d", Position.POWER_FORWARD, "4");
    players.add(testPlayer4);
    Player testPlayer5 = new Player("e", Position.SHOOTING_GUARD, "5");
    players.add(testPlayer5);
    Player testPlayer6 = new Player("f", Position.CENTER, "6");
    players.add(testPlayer6);
    Player testPlayer7 = new Player("g", Position.SMALL_FORWARD, "7");
    players.add(testPlayer7);
    Player testPlayer8 = new Player("h", Position.POINT_GUARD, "8");
    players.add(testPlayer8);
    Player testPlayer9 = new Player("i", Position.POWER_FORWARD, "9");
    players.add(testPlayer9);
    Player testPlayerA = new Player("j", Position.SHOOTING_GUARD, "a");
    players.add(testPlayerA);
    return players;
  }
}
