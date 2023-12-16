package MatchMakingTests;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.PositionMatchMaker;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class PositionMatchTest {
  private PositionMatchMaker pmm;

  public List<Player> makePlayers(int playerCount) {
    List<Player> players = new ArrayList<>();
    Position[] positions = Position.values();
    for (int i = 0; i < playerCount; i++) {
      Position position = positions[i % 5];
      Player player = new Player(Integer.toString(i), position);
      players.add(player);
    }
    return players;
  }

  @BeforeEach
  public void setup() {
    this.pmm = new PositionMatchMaker();
  }

  @Test
  public void positionSimpleTest() throws IOException {
    List<Player> players = makePlayers(10);
    Collections.shuffle(players);
    List<Match> matches = this.pmm.matchmaker(players, 2);
    Match match = matches.get(0);
    Team team1 = match.getTeam1();
    Team team2 = match.getTeam2();
    List<Position> position1 = new ArrayList<>(Arrays.asList(Position.values()));
    List<Position> position2 = new ArrayList<>(Arrays.asList(Position.values()));
    for (Player player : team1.getPlayers()) {
      position1.remove(player.getPosition());
    }
    for (Player player : team2.getPlayers()) {
      position2.remove(player.getPosition());
    }
    Assert.assertTrue(position1.isEmpty());
    Assert.assertTrue(position2.isEmpty());
  }

  @Test
  public void position4team() throws IOException {
    List<Player> players = makePlayers(20);
    Collections.shuffle(players);
    List<Match> matches = this.pmm.matchmaker(players, 4);
    Match match1 = matches.get(0);
    Match match2 = matches.get(1);
    Team team1 = match1.getTeam1();
    Team team2 = match1.getTeam2();
    Team team3 = match2.getTeam1();
    Team team4 = match2.getTeam2();
    List<Position> position1 = new ArrayList<>(Arrays.asList(Position.values()));
    List<Position> position2 = new ArrayList<>(Arrays.asList(Position.values()));
    List<Position> position3 = new ArrayList<>(Arrays.asList(Position.values()));
    List<Position> position4 = new ArrayList<>(Arrays.asList(Position.values()));

    for (Player player : team1.getPlayers()) {
      position1.remove(player.getPosition());
    }
    for (Player player : team2.getPlayers()) {
      position2.remove(player.getPosition());
    }
    for (Player player : team3.getPlayers()) {
      position3.remove(player.getPosition());
    }
    for (Player player : team4.getPlayers()) {
      position4.remove(player.getPosition());
    }
    Assert.assertTrue(position1.isEmpty());
    Assert.assertTrue(position2.isEmpty());
    Assert.assertTrue(position3.isEmpty());
    Assert.assertTrue(position4.isEmpty());
  }
}
