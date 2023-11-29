package Matchmaking.SimpleMatching;

import Matchmaking.IMatch;

public class SimpleMatch implements IMatch {
  private SimpleTeam team1;
  private SimpleTeam team2;
  private int outcome;

  public SimpleMatch(SimpleTeam team1, SimpleTeam team2) {
    this.team1 = team1;
    this.team2 = team2;
    this.outcome = -1;
  }

  public void setOutcome(int outcome) {
    this.outcome = outcome;
  }

  public SimpleTeam getTeam1() {
    return this.team1;
  }

  public SimpleTeam getTeam2() {
    return this.team2;
  }

  public int getOutcome() {
    return this.outcome;
  }
}
