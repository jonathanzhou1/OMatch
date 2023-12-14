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
public class MatchAddHandler implements Route {
  private Server server;

  /**
   * @param server
   */
  public MatchAddHandler(Server server) {
    this.server = server;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    String playerName;
    Match match;
    try {
      playerName = request.queryMap().get("name").value();
      match = null;
      // TODO: figure out how to match a specific player with a specific team, then use matchmaker
      // to assign player.

    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "action keyword must contain the word 'edit' or 'delete'. Any"
              + "other word will result in an error");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    responseMap.put("matchAdded", match);
    return adapter.toJson(responseMap);
  }
}
