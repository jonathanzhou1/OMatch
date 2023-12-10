package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import Matchmaking.Player;
import java.io.IOException;
import java.util.List;

public class WLSkill implements SkillUpdater{

  /**
   * Method to update the skill of all players in a match
   * This class will update the skill of each player to their winning percentage
   * This is there wins/total games played
   * @param match
   */
  @Override
  public void skillUpdater(Match match) throws IOException {
    int outcome = match.getOutcome();
    if(outcome == -1){
      throw new IOException("Cannot Update Skills, Match is Incomplete.");
    }
    if(outcome == 0){
      return;
    }
    if (outcome == 1){
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      WLHelper(team1Players, team2Players);
      return;
    }
    if (outcome == 2){
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      WLHelper(team2Players, team1Players);
      return;
    }
    throw new IOException("Outcome is Unexpected Number");
  }
  public void WLHelper(List<Player> wTeam, List<Player> lTeam){
    for(Player player : wTeam){
      player.addWin();
      float newSkill = player.getWinPercentage();
      player.setSkillLevel(newSkill);
    }
    for(Player player: lTeam){
      player.addLoss();
      float newSkill = player.getWinPercentage();
      player.setSkillLevel(newSkill);
    }
  }
}
