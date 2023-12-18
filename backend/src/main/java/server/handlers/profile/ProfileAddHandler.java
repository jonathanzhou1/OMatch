package server.handlers.profile;

import Matchmaking.Player;
import Matchmaking.Position;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import server.ServerSharedState;
import server.exceptions.ItemAlreadyExistsException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ProfileAddHandler implements Route {

  private ServerSharedState serverSharedState;

  public ProfileAddHandler(ServerSharedState serverSharedState) {
    this.serverSharedState = serverSharedState;
  }

  /**
   * The profile addition handler manages the addition of profiles to the backend database.
   * Any players with exactly the same name and position will be treated as different players
   * with a different ID, so it is imperative that you use the editing handler for editing or
   * deleting players.
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

    // Try getting the queryparams
    String playerID = "problem";
    String playerName = "problem";
    Position playerPosition = null;

    // Run a check on each queryparam

    try {
      if (request.queryMap().hasKey("id")) {
        playerID = request.queryMap().get("id").value();
      } else {
        throw new Exception("id neccesary");
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Error in specifying 'id' variable: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try {
      if (request.queryMap().hasKey("name")) {
        playerName = request.queryMap().get("name").value();
      } else {
        throw new Exception("name neccesary");
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Error in specifying 'name' variable: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try {

      if (request.queryMap().hasKey("position")) {
        playerPosition = Position.valueOf(request.queryMap().get("position").value());
      } else {
        throw new Exception("position query neccesary");
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Error in specifying 'id' variable. Please use 'POINT_GUARD', "
              + "'SHOOTING_GUARD', 'SMALL_FORWARD', 'POWER_FORWARD', or 'CENTER' in your "
              + "position query: "
              + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    Player newPlayer;
    // Now that we have the queryparams, we can update the server with this new information:
    try {
      newPlayer = new Player(playerName, playerPosition);
      newPlayer.setId(playerID);
      serverSharedState.getDataStore().addPlayer(newPlayer);
    } catch (ItemAlreadyExistsException e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Player already exists in the database, please use edit "
              + "handler instead: "
              + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("result", "error_server");
      responseMap.put("details", "Error in adding player to database: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("playerID", newPlayer.getId());
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
