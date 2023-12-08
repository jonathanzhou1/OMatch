package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Team;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortSkillMatchMaker implements IMatchMaker{

  /**
   * Skill absed matchmaking algorithm, divides player alternatively after sorting on skill
   * Then sort teams based on average skill and match
   * @param players, a list of players who will play in the match
   * @return
   */
  @Override
  public List<Match> matchmaker(List<Player> players, int numTeams) throws IOException {
    if(numTeams % 2 != 0) {
      throw new IOException("Number of Teams Cannot Be Odd");
    }
    Collections.sort(players, (p1, p2) -> Float.compare(p1.getSkillLevel(), p2.getSkillLevel()));
    List<Team> teams = new ArrayList<>();
    for (int i = 0; i < numTeams; i++) {
      teams.add(new Team());
    }
    for (int i = 0; i < players.size(); i++) {
      teams.get(i % numTeams).addPlayer(players.get(i));
    }
    Collections.sort(teams, (t1, t2) -> Float.compare(t1.getAvgSkill(), t2.getAvgSkill()));
    for (int i = 0; i < numTeams; i +=2) {
      Match match = new Match(teams.get(i), teams.get(i + 1));
    }

  }
}
