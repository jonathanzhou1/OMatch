package serverTests.profileTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import server.exceptions.ItemAlreadyExistsException;
import server.handlers.match.MatchEndHandler;
import server.handlers.match.MatchViewHandler;
import server.handlers.profile.ProfileAddHandler;
import server.handlers.profile.ProfileEditHandler;
import server.handlers.profile.ProfileViewHandler;
import server.handlers.queue.QueueAddHandler;
import server.handlers.queue.QueueViewHandler;
import spark.Spark;

public class ProfileEditTest {

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
    HttpURLConnection clientConnection = tryRequest("profile-edit");
    assertEquals(200, clientConnection.getResponseCode());
  }

  /**
   * Tests that the API correctly edits and deletes handlers
   *
   * @throws IOException
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testEditMultiplePlayers() throws IOException, ItemAlreadyExistsException {

    HttpURLConnection clientConnection =
        tryRequest("profile-add?name=johnjohnson&position=POINT_GUARD&id=1234567");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=BackMcBackend&position=CENTER&id=siufgb2ihb");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection =
        tryRequest("profile-add?name=NimTelson&position=SMALL_FORWARD&id=soduvbwi234");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection =
        tryRequest("profile-add?name=HoopHoopington&position=POWER_FORWARD&id=q932rfbsabBEWOB3");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-add?name=aaaa&position=CENTER&id=FE7DF3esfDFF");
    assertEquals(200, clientConnection.getResponseCode());

    // profile-view - Test that the players are all getting retrieved by the database
    clientConnection = tryRequest("profile-edit?id=1234567&action=delete");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-edit?id=siufgb2ihb&action=delete");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-edit?id=soduvbwi234&action=edit&position=CENTER");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection = tryRequest("profile-edit?id=q932rfbsabBEWOB3&action=edit&name=BallMcBallin");
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection =
        tryRequest("profile-edit?id=FE7DF3esfDFF&action=edit&position=SMALL_FORWARD&name=bbbb");
    assertEquals(200, clientConnection.getResponseCode());

    sharedState.getDataStore().getPlayers();

    // Test that the players have been deleted
    assertNull(sharedState.getDataStore().getPlayers().get("1234567"));
    assertNull(sharedState.getDataStore().getPlayers().get("siufgb2ihb"));

    // Test that the players have been successfully been edited
    assertEquals("NimTelson", sharedState.getDataStore().getPlayers().get("soduvbwi234").getName());
    assertEquals(
        Position.CENTER, sharedState.getDataStore().getPlayers().get("soduvbwi234").getPosition());

    assertEquals(
        "BallMcBallin", sharedState.getDataStore().getPlayers().get("q932rfbsabBEWOB3").getName());
    assertEquals(
        Position.POWER_FORWARD,
        sharedState.getDataStore().getPlayers().get("q932rfbsabBEWOB3").getPosition());

    assertEquals("bbbb", sharedState.getDataStore().getPlayers().get("FE7DF3esfDFF").getName());
    assertEquals(
        Position.SMALL_FORWARD,
        sharedState.getDataStore().getPlayers().get("FE7DF3esfDFF").getPosition());
  }

  /**
   * Tests that the API doesn't catastrophically fail upon an incorrect edit request
   *
   * @throws IOException
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testAPIFailureStates() throws IOException, ItemAlreadyExistsException {

    sharedState.getDataStore().addPlayer(new Player("Nim Telson", Position.CENTER, "aaaa"));

    // profile-edit
    HttpURLConnection clientConnection = tryRequest("profile-edit?id=aaaa&action=edit");
    assertEquals(200, clientConnection.getResponseCode());

    // The body of the string contains the proper data - "Success"
    Map<String, Object> body =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("error_bad_request", body.get("result"));

    // profile-edit
    clientConnection = tryRequest("profile-edit?id=abab&action=delete");
    assertEquals(200, clientConnection.getResponseCode());

    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "No item found to delete: No item found within the database to delete.",
        body.get("details"));

    // profile-edit
    clientConnection =
        tryRequest("profile-edit?id=abab&action=edit&name=TimTelson&" + "position=SMALL_FORWARD");
    assertEquals(200, clientConnection.getResponseCode());

    body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assert body != null;
    assertEquals("error_bad_request", body.get("result"));
    assertEquals(
        "No item found to edit: No Player found with corresponding ID", body.get("details"));
  }
}
