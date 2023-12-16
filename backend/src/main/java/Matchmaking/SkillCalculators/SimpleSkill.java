package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import Matchmaking.Outcome;
import Matchmaking.Player;
import java.io.IOException;
import java.util.List;

public class SimpleSkill implements SkillUpdater {

  /**
   * Method to update skill of all players in match Simple algorithm. Subtracts one from teams that
   * win and adds one to teams that lose
   *
   * @param match, the match of skills to be updated
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
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      for (Player player : team1Players) {
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill + 1);
        player.addLoss();
      }
      for (Player player : team2Players) {
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill - 1);
        player.addLoss();
      }
      return;
    }

    if (outcome == Outcome.TEAM2WIN) {
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      for (Player player : team2Players) {
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill + 1);
        player.addWin();
      }
      for (Player player : team1Players) {
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill - 1);
        player.addLoss();
      }
    }

    throw new IOException("Matchmaking.Outcome is Unexpected Number");
  }
}
