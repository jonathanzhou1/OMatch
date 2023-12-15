package server.handlers.match;

import Matchmaking.CourtAssigners.ICourt;
import Matchmaking.Match;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Route;

public class MatchViewHandler implements Route {

  private Server server;

  /**
   * @param server
   */
  public MatchViewHandler(Server server) {
    this.server = server;
  }

  /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    // Try viewing the matches
    try {
      // Make an Array of the courts:
      ICourt[] courts = server.getCourtAssigner().getCourts();
      LinkedList<Match> matches = new LinkedList<>();

      for (ICourt i : courts) {
        Match match;
        try {
          match = i.getMatch();
          matches.add(match);
        } catch (IllegalStateException ignored) {
        }
      }
      responseMap.put("matches", matches);

      // Success. Return success message
      responseMap.put("result", "success");
      responseMap.put("queries", request.queryParams());
      // responseMap.put("matches", matches);
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("result", "internal_server_error");
      responseMap.put("details", "Error getting match data from the server: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
  }
}
