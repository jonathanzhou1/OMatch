package server;

import Matchmaking.Player;
import java.util.HashMap;

public class ServerSharedState {
  private HashMap<String, Player> playerData;

  public ServerSharedState() {
    playerData = new HashMap<>();
  }
}
