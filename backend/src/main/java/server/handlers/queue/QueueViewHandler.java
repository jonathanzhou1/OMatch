package server.handlers.queue;

import Matchmaking.Player;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import server.ServerSharedState;
import spark.Request;
import spark.Response;
import spark.Route;

public class QueueViewHandler implements Route {

  private ServerSharedState serverSharedState;

  public QueueViewHandler(ServerSharedState serverSharedState) {
    this.serverSharedState = serverSharedState;
  }

  /**
   * Returns either the queue or the position of a player within the queue.
   *
   * @param request A Standard HTTPS request according to Spark Java
   * @param response A Standard HTTPS response according to Spark Java
   * @return A Map from String to Object, adapted to JSON.
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    String playerID;
    try {
      Player[] queuePlayers = serverSharedState.getDataStore().getQueue().toArray(new Player[0]);
      if (request.queryMap().hasKey("id")) {
        playerID = request.queryMap().get("id").value();
        // Get an array representation of the queue

        int queuePosition = -1;
        for (Player i : queuePlayers) {
          if (playerID != i.getId()) {
            queuePosition++;
          } else {
            break;
          }
        }
        responseMap.put("playerPosition", queuePosition);
      } else {
        responseMap.put("PlayerQueue", queuePlayers);
      }
    } catch (Exception e) {
      responseMap.put("result", "error_datastore");
      responseMap.put("details", "Datastore Error: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    System.out.println(serverSharedState.getDataStore().getQueue());
    return adapter.toJson(responseMap);
  }
}
