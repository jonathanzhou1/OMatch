package Matchmaking;

public enum Position {
  POINT_GUARD("PG"),
  SHOOTING_GUARD("SG"),
  SMALL_FORWARD("SF"),
  POWER_FORWARD("PF"),
  CENTER("C");

  private final String abbreviation;

  Position(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public String getAbbreviation() {
    return abbreviation;
  }
}
