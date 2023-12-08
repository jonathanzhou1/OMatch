package server.handlers;

import Matchmaking.IPlayer;
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

  public ProfileEditHandler(Server server){
    this.server = server;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    HashMap<String, Object> responseMap = new HashMap<>();

    // get the parameters
    String playerName;
    Position playerPosition;
    String action;
    Player player;
    try{
      playerName = request.queryMap().get("name").value();
      action = playerName = request.queryMap().get("action").value();
      playerPosition = Position.valueOf(request.queryMap().get("position").value());

      // if the player exists within the database, update them, and use their new
      // stats/position within the database.
      player = server.getDataStore().getPlayer(playerName);
      player = new Player(playerName, playerPosition);

      if(action.equalsIgnoreCase("edit")){
        server.getDataStore().updatePlayer(playerName, player);
      }else if(action.equalsIgnoreCase("delete")){
        server.getDataStore().deleteItem(playerName);
      }else{
        responseMap.put("result", "error_bad_request");
        responseMap.put("details", "action keyword must contain the word 'edit' or 'delete'. Any"
            + "other word will result in an error");
        responseMap.put("queries", request.queryParams());
        return adapter.toJson(responseMap);
      }
    }catch(NoItemFoundException e){
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "No item found within the database to edit"
          + "please use add handler in this case: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }catch(Exception e){
      responseMap.put("result", "error_bad_contents");
      responseMap.put("details", "Error in editing player to database: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
