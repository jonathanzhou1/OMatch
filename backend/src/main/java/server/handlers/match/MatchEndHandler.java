package server.handlers.match;

import Matchmaking.CourtAssigners.ICourt;
import Matchmaking.Match;
import Matchmaking.Player;
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
    Player player;
    String playerWon;
    try {
      playerID = request.queryMap().get("id").value();
      player = this.server.getDataStore().getPlayer(playerID);
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Error in specifying 'id' variable: ");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try {
      // If the playerWon value is correct, move onto the next step.
      playerWon = request.queryMap().get("playerWon").value();
      if(!(playerWon.equalsIgnoreCase("win") ||
          playerWon.equalsIgnoreCase("tie") ||
          playerWon.equalsIgnoreCase("lose"))){
        throw new Exception();
      }
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "details",
          "Error in specifying 'playerWon' variable. Variable must be 'win', 'tie', or 'lose'");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }

    // Now that we have the ID and winState correct, we can call the matching Court and add it
    // to the court's internal tally
    ICourt[] courts = this.server.getCourtAssigner().getCourts();

    for(int i = 0; i < courts.length; i++){
      if(courts[i].getPlayers().contains(player)){
        if(courts[i].tryEndGame(player,playerWon)){
          // This court has done its job and can now be removed.
          Map<String, Player> playerMap = server.getCourtAssigner().removeInternalCourt(i);
          for(String pID: playerMap.keySet()){
            server.getDataStore().updatePlayer(pID, playerMap.get(pID));
          }
        }
        break;
      }
    }
    // Success. Return success message
    responseMap.put("result", "success");
    responseMap.put("queries", request.queryParams());
    return adapter.toJson(responseMap);
  }
}
