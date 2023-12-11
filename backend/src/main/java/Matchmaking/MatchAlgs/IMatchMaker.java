package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import java.io.IOException;
import java.util.List;

public interface IMatchMaker {

  /**
   * Algorithm to create matches from a queue of players
   * Splits the players into two different teams and then creates matches for them
   * @param players, a list of players who will play in the match
   * @return
   */
  public List<Match> matchmaker(List<Player> players, int numTeams) throws IOException;
}
