package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class PositionMatchMaker implements IMatchMaker {

  /**
   * Algorithm to create matches from a queue of players Splits the players into two different teams
   * and then creates matches for them The first goal of this algorithm is to make sure each team
   * maximizes the positions, and then we can sort the overall teams based on skill. Assume teams
   * are of size 5.
   *
   * @param players
   * @param numTeams
   * @return
   */
  @Override
  public List<Match> matchmaker(List<Player> players, int numTeams) throws IOException {
    players.sort((p1, p2) -> Float.compare(p2.getSkillLevel(), p1.getSkillLevel()));
    Map<Position, List<Player>> positionMap = new HashMap<>();
    for (Player player : players) {
      positionMap.computeIfAbsent(player.getPosition(), k -> new ArrayList<>()).add(player);
    }
    List<Team> teams = new ArrayList<>();
    for (int i = 0; i < numTeams; i++) {
      teams.add(new Team());
    }
    // Split up players based on position
    for (Position position : Position.values()) {
      List<Player> playersForPostion = positionMap.getOrDefault(position, new ArrayList<>());
      for (int i = 0; i < numTeams && !playersForPostion.isEmpty(); i++) {
        Player playerToAdd = playersForPostion.remove(0);
        teams.get(i).addPlayer(playerToAdd);
        players.remove(playerToAdd);
      }
    }
    // Split up reamining players
    for (Player remainingPlayer : players) {
      for (Team team : teams) {
        if (team.getSize() < 5) {
          team.addPlayer(remainingPlayer);
          break;
        }
      }
    }
    return matchTeams(numTeams, teams);
  }

  @NotNull
  static List<Match> matchTeams(int numTeams, List<Team> teams) {
    Collections.sort(teams, (t1, t2) -> Float.compare(t1.getAvgSkill(), t2.getAvgSkill()));
    List<Match> matches = new ArrayList<>();
    for (int i = 0; i < numTeams; i += 2) {
      Match match = new Match(teams.get(i), teams.get(i + 1));
      matches.add(match);
    }
    return matches;
  }
}
