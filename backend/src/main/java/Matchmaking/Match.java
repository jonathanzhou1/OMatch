package Matchmaking;

public class Match implements IMatch {
  private Team team1;
  private Team team2;
  private Outcome outcome;
  private String score;
  /**
   * Constructor for a match
   * @param team1, the first team
   * @param team2, the second team
   */
  public Match(Team team1, Team team2) {
    this.team1 = team1;
    this.team2 = team2;
    this.outcome = Outcome.ONGOING;
  }

  /**
   * SetOutcome. Default = -1 = Not finished. 0 = Draw, 1 = Team 1 Wins, 2 = Team 2 Wins
   * @param outcome
   */
  public void setOutcome(Outcome outcome) {
    this.outcome = outcome;
  }

  /**
   * Getter for team1 players
   * @return team 1 players
   */
  public Team getTeam1() {
    return this.team1;
  }

  public String getScore() {
    return this.score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  /**
   * Getter for team 2 players
   * @return team 2 players
   */
  public Team getTeam2() {
    return this.team2;
  }

  /**
   * Getter for the outcome
   * @return, outcome of the game
   */
  public Outcome getOutcome() {
    return this.outcome;
  }
}
