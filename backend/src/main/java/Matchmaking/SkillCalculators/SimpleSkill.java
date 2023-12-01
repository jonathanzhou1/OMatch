package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Team;
import java.io.IOException;
import java.util.List;

public class SimpleSkill implements SkillUpdater {

  /**
   * Method to update skill of all players in match
   * Simple algorithm. Subtracts one from teams that win and adds one to teams that lose
   * @param match, the match of skills to be updated
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
    if(outcome == 1){
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      for(Player player: team1Players){
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill + 1);
      }
      for(Player player: team2Players){
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill - 1);
      }
    }
    if(outcome == 2){
      List<Player> team1Players = match.getTeam1().getPlayers();
      List<Player> team2Players = match.getTeam2().getPlayers();
      for(Player player: team2Players){
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill + 1);
      }
      for(Player player: team1Players){
        float oldSkill = player.getSkillLevel();
        player.setSkillLevel(oldSkill - 1);
      }
    }
    throw new IOException("Outcome is Unexpected Number");

  }
}
