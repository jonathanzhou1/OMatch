package server.handlers.match;

import Matchmaking.CourtAssigners.ICourt;
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

/** Adds a player to the most fitting match for their skill levels */
public class MatchEndHandler implements Route {
  private ServerSharedState serverSharedState;

  /**
   * @param serverSharedState
   */
  public MatchEndHandler(ServerSharedState serverSharedState) {
    this.serverSharedState = serverSharedState;
  }

  /**
   * The match end handler is called when a player indicates that the match has ended and that they
   * think the court can be freed. It should be noted however, that the handler requires all players
   * to have a query associated with them for the handler to automatically delete the match and
   * update the players' stat
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

    // Get the player ID and if they won
    String playerID;
    Player player;
    String playerWon;
    try {
      playerID = request.queryMap().get("id").value();
      player = this.serverSharedState.getDataStore().getPlayer(playerID);
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("details", "Error in specifying 'id' variable: ");
      responseMap.put("queries", request.queryParams());
      return adapter.toJson(responseMap);
    }
    try {
      // If the playerWon value is correct, move onto the next step.
      playerWon = request.queryMap().get("playerWon").value();
      if (!(playerWon.equalsIgnoreCase("win")
          || playerWon.equalsIgnoreCase("tie")
          || playerWon.equalsIgnoreCase("lose"))) {
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
    ICourt[] courts = this.serverSharedState.getCourtAssigner().getCourts();

    for (int i = 0; i < courts.length; i++) {
      if (courts[i].getPlayers().contains(player)) {
        if (courts[i].tryEndGame(player, playerWon)) {
          // This court has done its job and can now be removed.
          Map<String, Player> playerMap =
              serverSharedState.getCourtAssigner().removeInternalCourt(i);
          for (String pID : playerMap.keySet()) {
            serverSharedState.getDataStore().updatePlayer(pID, playerMap.get(pID));
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
