package MatchMakingTests;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class SimpleMatchTests {
  private SimpleMatchMaker smm;

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

  @Test
  public void testMakePlayers() {
    List<Player> players = makePlayers(10);
    Position[] positions = Position.values();
    for (int i = 0; i < players.size(); i++) {
      Assert.assertEquals(players.get(i).getName(), Integer.toString(i));
      Assert.assertEquals(players.get(i).getPosition(), positions[i % 5]);
      System.out.println(players.get(i).getPosition());
    }
  }

  @BeforeEach
  public void setup() {
    this.smm = new SimpleMatchMaker();
  }

  @Test
  public void testSimpleMatch() throws IOException {
    List<Player> tenPLayers = this.makePlayers(10);
    List<Match> matches = this.smm.matchmaker(tenPLayers, 2);
    Assert.assertEquals(matches.size(), 1);
    Match match = matches.get(0);
    Team team1 = match.getTeam1();
    Team team2 = match.getTeam2();
    Assert.assertEquals(team1.getSize(), 5);
    Assert.assertEquals(team2.getSize(), 5);
  }

  @Test
  public void unevenTeams() throws IOException {
    List<Player> tenPlayers = this.makePlayers(10);
    try {
      List<Match> matches = this.smm.matchmaker(tenPlayers, 3);
    } catch (IOException e) {
      return;
    }
    throw new AssertionError("Error Not Thrown");
  }

  @Test
  public void multiTeams() throws IOException {
    List<Player> twentyPLayers = this.makePlayers(20);
    List<Match> matches = this.smm.matchmaker(twentyPLayers, 4);
    Assert.assertEquals(matches.size(), 2);
    Team team1 = matches.get(0).getTeam1();
    Team team2 = matches.get(0).getTeam2();
    Team team3 = matches.get(1).getTeam1();
    Team team4 = matches.get(1).getTeam2();
    List<Team> teams = List.of(team1, team2, team3, team4);
    for (Team team : teams) {
      Assert.assertEquals(team.getSize(), 5);
    }
  }
}
