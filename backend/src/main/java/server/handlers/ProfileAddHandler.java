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
import server.exceptions.ItemAlreadyExistsException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ProfileAddHandler implements Route {

  private Server server;

  public ProfileAddHandler(Server server){
    this.server = server;
  }

  /**
   * Adds a new profile to the database
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

    // Try getting the queryparams
    String playerName;
    Position playerPosition;
    try{
      playerName = request.queryMap().get("name").value();
      playerPosition = Position.valueOf(request.queryMap().get("position").value());
    }catch(Exception e){
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Error in specifying 'name' and 'position' variables: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Now that we have the queryparams, we can update the server with this new information:
    try {
      Player newPlayer = new Player(playerName, playerPosition);
      server.getDataStore().addPlayer(newPlayer);
    }catch(ItemAlreadyExistsException e){
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Player already exists in the database, please use edit "
          + "handler instead: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }catch(Exception e){
      responseMap.put("result", "error_bad_contents");
      responseMap.put("details", "Error in adding player to database: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
