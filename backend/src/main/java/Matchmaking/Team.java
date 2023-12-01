package Matchmaking;

import Matchmaking.Player;
import java.util.ArrayList;
import java.util.List;

public class Team {
  private List<Player> players;

  public Team() {
    this.players = new ArrayList<>();
  }
  public void addPlayer(Player player){
    this.players.add(player);
  }
  public List<Player> getPlayers() {
    return this.players;
  }
}
