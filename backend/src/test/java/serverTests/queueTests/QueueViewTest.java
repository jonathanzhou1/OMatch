package serverTests.queueTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
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

public class QueueViewTest {
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
    HttpURLConnection clientConnection = tryRequest("queue-view");
    assertEquals(200, clientConnection.getResponseCode());
  }
}
