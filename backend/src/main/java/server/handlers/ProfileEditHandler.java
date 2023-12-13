package server.handlers;

import Matchmaking.Player;
import Matchmaking.Position;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import server.Server;
import server.exceptions.NoItemFoundException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ProfileEditHandler implements Route {

  private Server server;

  public ProfileEditHandler(Server server) {
    this.server = server;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    // get the parameters
    String playerID;
    String playerName;
    Position playerPosition;

    String action;
    Player player;

    // Get the id and action handlers, these are always necessary

    try{
      if(request.queryMap().hasKey("id")) {
        playerID = request.queryMap().get("id").value();
      }else{
        throw new Exception("id neccesary");
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details", "Error in specifying 'id' variable: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try{
      if(request.queryMap().hasKey("action")) {
        action = request.queryMap().get("action").value();
        if(!(action.equalsIgnoreCase("edit") ||
            action.equalsIgnoreCase("delete") )){
          throw new Exception("Please use the string 'edit' or 'delete' when specifying "
              + "action keyword");
        }
      }else{
        throw new Exception("action neccesary");
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details", "Error in specifying 'action' variable: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Now check the action:

    if(action.equalsIgnoreCase("delete")){
      server.getDataStore().deleteItem(playerID);
    }else if(action.equalsIgnoreCase("edit")){

      // get the player ID and/or player Position, if they're not there, then just use the old value

      boolean playerNamePresent = false;
      boolean playerPositionPresent = false;
      player = server.getDataStore().getPlayer(playerID);

      // Check for player name

      try{
        if(request.queryMap().hasKey("name")) {
          playerName = request.queryMap().get("name").value();
          playerNamePresent = true;
        }else{
          playerName = player.getName();
        }
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put(
            "details", "Error in specifying 'id' variable: " + e.getMessage());
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }
      // check for player position
      try{
        if(request.queryMap().hasKey("position")) {
          playerPosition = Position.valueOf(request.queryMap().get("position").value());
          playerPositionPresent = true;
        }else{
          playerPosition = player.getPosition();
        }
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put(
            "details", "Error in specifying 'id' variable: " + e.getMessage());
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }

      // if neither is present, throw an error
      if(!playerNamePresent && !playerPositionPresent){
        responseMap.put("result", "error_bad_request");
        responseMap.put(
            "details", "When editing, the player name or position must be present, "
                + "otherwise there's nothing to edit");
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }

      // update the player with a new value and return a success
      player = new Player(playerName, playerPosition);
      try {
        server.getDataStore().updatePlayer(playerID, player);
      }catch (NoItemFoundException e){
        responseMap.put("result", "error_datastore");
        responseMap.put(
            "details", "Internal datastore error when updating player: " + e.getMessage());
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }

    }else{
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
    return adapter.toJson(responseMap);
  }
}
