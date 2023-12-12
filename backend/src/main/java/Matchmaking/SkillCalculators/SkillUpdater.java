package Matchmaking.SkillCalculators;

import Matchmaking.Match;
import java.io.IOException;

/** Interface for algorithms that update skills of players after a match */
public interface SkillUpdater {

  /**
   * Method to update the skill of all players in a match
   *
   * @param match, the match
   */
  public void skillUpdater(Match match) throws IOException;
}
