package Matchmaking.SimpleMatching;

import java.util.ArrayList;
import java.util.List;

public class SimpleTeam {
  private List<SimplePlayer> players;

  public SimpleTeam() {
    this.players = new ArrayList<>();
  }
  public void addPlayer(SimplePlayer player){
    this.players.add(player);
  }
  public List<SimplePlayer> getPlayers() {
    return this.players;
  }
}
