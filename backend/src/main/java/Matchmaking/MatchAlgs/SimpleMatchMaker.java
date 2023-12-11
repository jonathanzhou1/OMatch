package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMatchMaker implements IMatchMaker {

  /**
   * Simple matchmaking algorithm, splits up players alternatively without taking into account
   * skill. Then pairs teams together
   * @param players, a list of players who will play in the match
   * @return
   */
  public List<Match> matchmaker(List<Player> players, int numTeams) throws IOException {
    if(numTeams % 2 != 0) {
      throw new IOException("Number of Teams Cannot Be Odd");
    }
    int teamSize = players.size() / numTeams;
    List<Team> teams = new ArrayList<>();
    for (int i = 0; i < numTeams; i++) {
      teams.add(new Team());
    }
    for (int i = 0; i < players.size(); i++) {
      teams.get(i % numTeams).addPlayer(players.get(i));
    }
    List<Match> matches = new ArrayList<>();
    for (int i = 0; i < numTeams; i += 2) {
      Match match = new Match(teams.get(i), teams.get(i + 1));
      matches.add(match);
    }
    return matches;
  }
}
