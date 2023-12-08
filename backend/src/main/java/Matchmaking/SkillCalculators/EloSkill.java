package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Team;
import java.io.IOException;
import java.util.List;

public class EloSkill implements  SkillUpdater{

  /**
   * Method to update the skill of all players in a match according to the Elo algoritm
   * Elo is a 1 v 1 algorithm so averages were used across teams and then players skills were
   * adjusted based on those averages
   *
   * @param match
   */
  @Override
  public void skillUpdater(Match match) throws IOException {
    int outcome = match.getOutcome();
    if(outcome == -1){
      throw new IOException("Cannot Update Skills, Match is Incomplete.");
    }
    if(outcome == 0) {
      return;
    }
    if(outcome == 1){
      Team team1 = match.getTeam1();
      Team team2 = match.getTeam2();
      this.EloHelper(team1, team2);
      return;
    }
    if(outcome == 2){
      Team team1 = match.getTeam1();
      Team team2 = match.getTeam2();
      this.EloHelper(team2, team1);
      return;
    }
    throw new IOException("Outcome is Unexpected Number");
  }

  private void EloHelper(Team wTeam, Team lTeam) {
    float wOldRank = wTeam.getAvgSkill();
    float lOldRank = lTeam.getAvgSkill();
    double wExpected = 1/(1+ Math.pow(10, (lOldRank - wOldRank) / 400.0));
    double lExpected = 1/(1+ Math.pow(10, (wOldRank - lOldRank) / 400.0));
    double wNewRank = wOldRank + (32 *(1 - wExpected));
    double lNewRank = lOldRank + (32*(0 - lExpected));
    float wAdd = (float) wNewRank - wOldRank;
    float lSubtract = lOldRank - (float) lNewRank;
    for(Player player : wTeam.getPlayers()){
      float oldSkill = player.getSkillLevel();
      player.setSkillLevel(oldSkill + wAdd);
    }
    for (Player player: lTeam.getPlayers()){
      float oldskill = player.getSkillLevel();
      player.setSkillLevel(oldskill - lSubtract);
    }
  }
}