package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import Matchmaking.Outcome;
import Matchmaking.Player;
import Matchmaking.Team;
import java.io.IOException;

public class EloSkill implements SkillUpdater {

  /**
   * Method to update the skill of all players in a match according to the Elo algoritm Elo is a 1 v
   * 1 algorithm so averages were used across teams and then players skills were adjusted based on
   * those averages.
   *
   * @param match
   */
  @Override
  public void skillUpdater(Match match) throws IOException {

    Outcome outcome = match.getOutcome();
    if (outcome == Outcome.ONGOING) {
      throw new IOException("Cannot Update Skills, Match is Incomplete.");
    }
    if (outcome == Outcome.TIE) {
      return;
    }
    if (outcome == Outcome.TEAM1WIN) {
      Team team1 = match.getTeam1();
      Team team2 = match.getTeam2();
      this.EloHelper(team1, team2);
      return;
    }

    if (outcome == Outcome.TEAM2WIN) {
      Team team1 = match.getTeam1();
      Team team2 = match.getTeam2();
      this.EloHelper(team2, team1);
      return;
    }
    throw new IOException("Matchmaking.Outcome is Unexpected Number");
  }

  private void EloHelper(Team wTeam, Team lTeam) {
    float wOldRank = wTeam.getAvgSkill();
    float lOldRank = lTeam.getAvgSkill();

    double wExpected = 1 / (1 + Math.pow(10, (lOldRank - wOldRank) / 400.0));
    double lExpected = 1 / (1 + Math.pow(10, (wOldRank - lOldRank) / 400.0));
    double wNewRank = wOldRank + (30 * (1 - wExpected));
    double lNewRank = lOldRank + (30 * (0 - lExpected));
    float wAdd = (float) wNewRank - wOldRank;
    float lSubtract = lOldRank - (float) lNewRank;
    for (Player player : wTeam.getPlayers()) {
      float oldSkill = player.getSkillLevel();
      player.setSkillLevel(oldSkill + wAdd);
      player.addWin();
    }

    for (Player player : lTeam.getPlayers()) {
      float oldSkill = player.getSkillLevel();
      player.setSkillLevel(oldSkill - lSubtract);
      player.addLoss();
    }
  }
}
