package server.handlers;

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

public class ProfileViewHandler implements Route {

  private Server server;

  public ProfileViewHandler(Server server) {
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

    String playerID;
    try{
      if(request.queryMap().hasKey("id")) {
        playerID = request.queryMap().get("id").value();
        responseMap.put("player", server.getDataStore().getPlayer(playerID));
      }else{
        responseMap.put("players", server.getDataStore().getPlayers());
      }
    } catch (NoItemFoundException e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "No item found within the database: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("result", "error_datastore");
      responseMap.put("details", "Datastore Error: " + e.getMessage());
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
