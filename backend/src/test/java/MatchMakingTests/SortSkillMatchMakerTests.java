package MatchMakingTests;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.MatchAlgs.SortSkillMatchMaker;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.Team;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class SortSkillMatchMakerTests {

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

  SortSkillMatchMaker smm;
  @BeforeEach
  public void setup() {
    this.smm = new SortSkillMatchMaker();
  }

  @Test
  public void testSimpleMatch() throws IOException {
    List<Player> tenPlayers = this.makePlayers(10);
    float i = 0;
    for(Player player: tenPlayers ) {
      player.setSkillLevel(i);
      i += 10;
    }
    Collections.shuffle(tenPlayers);
    List<Match> matches = this.smm.matchmaker(tenPlayers, 2);
    Match match = matches.get(0);
    Team team1 = match.getTeam1();
    Team team2 = match.getTeam2();
    Assert.assertEquals(team1.getAvgSkill(), team2.getAvgSkill(), 10);
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
}
