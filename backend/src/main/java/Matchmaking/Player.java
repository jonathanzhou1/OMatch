package Matchmaking;

public class Player implements IPlayer {

  private String id;
  private float skillLevel;
  private int wins;
  private int losses;
  private Position position;

  public String getId() {
    return this.id;
  }

  public void setName(String id) {
    this.id = id;
  }

  public Player(String id, Position position) {
    this.id = id;
    this.position = position;
    this.skillLevel = 10;
    this.wins = 0;
    this.losses = 0;
  }

  public Position getPosition() {
    return this.position;
  }

  public void addWin() {
    this.wins++;
  }

  public void addLoss() {
    this.losses++;
  }

  public void setSkillLevel(float skillLevel) {
    this.skillLevel = skillLevel;
  }
  public float getSkillLevel(){
    return this.skillLevel;
  }

  public float getWinPercentage(){
    return (float) this.wins/(this.wins + this.losses);
  }
}
