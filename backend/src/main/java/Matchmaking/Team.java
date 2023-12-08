package Matchmaking;

import Matchmaking.Player;
import java.util.ArrayList;
import java.util.List;

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
