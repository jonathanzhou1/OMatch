package serverTests.matchTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.CourtAssigners.ICourt;
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
import server.exceptions.NoItemFoundException;
import server.handlers.match.MatchEndHandler;
import server.handlers.match.MatchViewHandler;
import server.handlers.profile.ProfileAddHandler;
import server.handlers.profile.ProfileEditHandler;
import server.handlers.profile.ProfileViewHandler;
import server.handlers.queue.QueueAddHandler;
import server.handlers.queue.QueueViewHandler;
import spark.Spark;

public class MatchEndTest {

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
          new SimpleDataStore(), new CourtAssigner(1, new SimpleMatchMaker(), new SimpleSkill()));

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
    HttpURLConnection clientConnection = tryRequest("match-end");
    assertEquals(200, clientConnection.getResponseCode());
  }

  /**
   * Tests that the matchEndHandler correctly ends a match after all players vote to end
   *
   * @throws IOException
   * @throws NoItemFoundException
   */
  @Test
  public void testMatchCreation() throws IOException, NoItemFoundException {
    // Make at least 10 players in the server, then add a few extra as well
    HttpURLConnection clientConnection =
        tryRequest("profile-add?name=aaa&position=POINT_GUARD&id=1");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=bbb&position=CENTER&id=2");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=ccc&position=SMALL_FORWARD&id=3");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=ddd&position=POWER_FORWARD&id=4");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=eee&position=CENTER&id=5");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=fff&position=POINT_GUARD&id=6");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=ggg&position=CENTER&id=7");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=hhh&position=SMALL_FORWARD&id=8");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=iii&position=POWER_FORWARD&id=9");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=jjj&position=CENTER&id=a");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=kk&position=POWER_FORWARD&id=b");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=lll&position=CENTER&id=c");
    assertEquals(200, clientConnection.getResponseCode());

    tryRequest("queue-add?id=1");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=2");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=3");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=4");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=5");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=6");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=7");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=8");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=9");
    assertEquals(200, clientConnection.getResponseCode());

    assertNull(sharedState.getCourtAssigner().getCourts()[0].getMatch());
    System.out.println(sharedState.getDataStore().getQueue());
    tryRequest("queue-add?id=a");
    System.out.println(sharedState.getDataStore().getQueue());
    assertEquals(200, clientConnection.getResponseCode());
    // match should be created now

    // assertNotNull(sharedState.getCourtAssigner().getCourts()[0].getMatch());
    // assertNotNull(sharedState.getCourtAssigner().getCourts()[1].getMatch());
    // assertNotNull(sharedState.getCourtAssigner().getCourts()[2].getMatch());
    // assertNotNull(sharedState.getCourtAssigner().getCourts()[3].getMatch());
    // assertNotNull(sharedState.getCourtAssigner().getCourts()[4].getMatch());
    // assertNotNull(sharedState.getCourtAssigner().getCourts()[5].getMatch());

    for (ICourt i : sharedState.getCourtAssigner().getCourts()) {
      System.out.println(i.getMatch());
    }

    assertEquals(0, sharedState.getDataStore().getQueue().size());

    // Add a couple more to the queue to make sure that the queue is only length 2 at this point.
    tryRequest("queue-add?id=b");
    assertEquals(200, clientConnection.getResponseCode());
    tryRequest("queue-add?id=c");
    assertEquals(200, clientConnection.getResponseCode());
    // assertEquals(2, sharedState.getDataStore().getQueue().size());

    for (ICourt i : sharedState.getCourtAssigner().getCourts()) {
      System.out.println(i.getMatch());
    }
  }

  @Test
  public void testBadPlayerwonRequest() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("profile-add?name=aaa&position=POINT_GUARD&id=1");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("match-end?id=1&playerWon=mimimi");
    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "Error in specifying 'playerWon' variable. " + "Variable must be 'win', 'tie', or 'lose'",
        body.get("details"));
  }

  @Test
  public void testMatchEnding() throws IOException {
    HttpURLConnection clientConnection;
    Map<String, Object> body;

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

      // Try ending the match
      clientConnection = tryRequest("match-end?id=" + p.getId() + "&playerWon=win");
      assertEquals(200, clientConnection.getResponseCode());
      body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      assertEquals("success", body.get("result"));
      playerAdded++;
    }

    clientConnection = tryRequest("match-end?id=1&playerWon=mimimi");
    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "Error in specifying 'playerWon' variable. " + "Variable must be 'win', 'tie', or 'lose'",
        body.get("details"));

    assertNull(sharedState.getCourtAssigner().getCourts()[0]);
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
