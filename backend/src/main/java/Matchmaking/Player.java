package Matchmaking;

import java.util.Random;

public class Player implements IPlayer {

  private String id;
  private String name;
  private float skillLevel;
  private int wins;
  private int losses;
  private Position position;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public Player(String name, Position position) {
    this.name = name;
    this.position = position;
    this.skillLevel = 1500;
    this.wins = 0;
  }

  public void generateID() {
    String initialChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    Random random = new Random();
    StringBuilder idBuilder = new StringBuilder();
    int length = 0;
    // Generate a sufficiently long id to minimize collisions (they're still handled but chances
    // are remarkably low)

    while (length < 20) {
      // get next int from 0 (inclusive) to length - 1 (exclusive)

      int nextCharInt = random.nextInt(initialChars.length() - 2);
      idBuilder.append(initialChars, nextCharInt, nextCharInt + 1);

      length++;
    }
    this.id = idBuilder.toString();
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

  public float getSkillLevel() {
    return this.skillLevel;
  }

  public float getWinPercentage() {
    return (float) this.wins / (this.wins + this.losses);
  }
}
