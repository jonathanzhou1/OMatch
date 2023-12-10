package Matchmaking;

import Matchmaking.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.jetty.util.IO;

public class Team {
  private List<Player> players;
  private float avgSkill;
  private int size;

  public Team() {
    this.players = new ArrayList<>();
    this.avgSkill = 0;
    this.size = 0;
  }
  public void addPlayer(Player player){
    this.players.add(player);
    this.size++;
    this.avgSkill = this.updateAvgSkill();
  }
  public List<Player> getPlayers() {
    return this.players;
  }

  /**
   * May want to make a new exception for this, IO Exception isn't the best
   * @param id
   * @return
   * @throws IOException
   */
  public Player getPlayer(String id) throws IOException {
    for (Player player : this.players) {
      if (Objects.equals(player.getId(), id)) {
        return player;
      }
    }
    throw new IOException("Player Not Found");
  }
  public boolean isPlayer(String id) {
    for (Player player : this.players) {
      if (Objects.equals(player.getId(), id)) {
        return true;
      }
    }
    return false;
  }
  public int getSize() {
    return this.size;
  }

  private float updateAvgSkill(){
    float totalSkill = 0;
    for(Player player: this.players){
      totalSkill += player.getSkillLevel();
    }
    return totalSkill/this.size;
  }

  public float getAvgSkill() {
    return this.avgSkill;
  }

  public void setAvgSkill(float avgSkill) {
    this.avgSkill = avgSkill;
  }
}
