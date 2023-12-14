package server.handlers.match;

import Matchmaking.Match;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Route;

/** Adds a player to the most fitting match for their skill levels */
public class MatchEndHandler implements Route {
  private Server server;

  /**
   * @param server
   */
  public MatchEndHandler(Server server) {
    this.server = server;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    // Get the player ID and if they won
    String playerID;
    boolean playerWon;
    try {
      playerID = request.queryMap().get("id").value();
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Error in specifying 'id' variable: ");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try {
      playerWon = Boolean.parseBoolean(request.queryMap().get("playerWon").value());
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Error in specifying 'playerWon' variable. Variable must be true or false");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }



    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
