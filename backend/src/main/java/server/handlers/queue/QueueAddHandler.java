package server.handlers.queue;

import Matchmaking.CourtAssigners.ICourt;
import Matchmaking.Player;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import server.Server;
import server.exceptions.NoItemFoundException;
import server.exceptions.NoItemMadeException;
import spark.Request;
import spark.Response;
import spark.Route;

public class QueueAddHandler implements Route {

  private Server server;

  public QueueAddHandler(Server server) {
    this.server = server;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   * @throws Exception implementation can choose to throw exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    String playerID;
    try {
      playerID = request.queryMap().get("id").value();
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Error in specifying id variable: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    Player player;
    try {
      player = server.getDataStore().getPlayer(playerID);
      if (!server.getDataStore().getQueue().contains(player)) {
        player = this.server.getDataStore().addQueue(playerID);
      } else {
        throw new Exception("Player has already been added to queue.");
      }

    } catch (NoItemFoundException e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Player Not Found: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Now that the player is added to the queue, we can check if a match can be formed
    ICourt court;
    responseMap.put("newCourtMade", false);
    try {
      int courtIndex = server.getCourtAssigner().addPlayers(server.getDataStore().getQueue());
      court = server.getCourtAssigner().getCourts()[courtIndex];
      responseMap.put("court", court);
      responseMap.put("newCourtMade", true);
      for (int i = 0; i < 10; i++) {
        server.getDataStore().getQueue().poll();
      }
    } catch (NoItemMadeException e) {
      if (!e.getMessage().equals("Queue length is not yet long enough for matchmaking.")) {
        responseMap.put("result", "matchmaking_error");
        responseMap.put("details", "All queues full currently: " + e.getMessage());
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }
    } catch (IOException e) {
      responseMap.put("result", "matchmaking_error");
      responseMap.put("details", "Internal matchmaker error: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    } catch (IndexOutOfBoundsException e) {
      responseMap.put("result", "matchmaking_full");
      responseMap.put("details", "Courts are currently full: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    responseMap.put("result", "success");
    responseMap.put("addedID", playerID);
    responseMap.put("Message", "Player added to queue");
    return adapter.toJson(responseMap);
  }
}
