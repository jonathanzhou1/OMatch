package Matchmaking.SimpleMatching;

import Matchmaking.IPlayer;

public class SimplePlayer implements IPlayer {
  private String id;
  private int skillLevel;

  public String getid() {
    return this.id;
  }

  public void setName(String id) {
    this.id = id;
  }

  public SimplePlayer(String id){
    this.id = id;
    this.skillLevel = 10;
  }
}
