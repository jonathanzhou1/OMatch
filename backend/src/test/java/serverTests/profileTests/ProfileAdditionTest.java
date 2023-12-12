package serverTests.profileTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import server.Server;
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

  @BeforeEach
  public void setup() throws FileNotFoundException {

    Server server = new Server();

    Spark.init();
    Spark.awaitInitialization();

    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + "3232" + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");
    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testAPICode200() throws IOException {
    HttpURLConnection clientConnection = tryRequest("profile-add");
    assertEquals(200, clientConnection.getResponseCode());
  }

  /**
   * Test that the handler can handle thuosands of profiles being added - Simple database
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
}
