package SkillCalculatorTests;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Outcome;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.SkillCalculators.WLSkill;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class WLSkillTests {
  WLSkill wlSkill;
  public List<Player> makePlayers(int playerCount) {
    List<Player> players = new ArrayList<>();
    Position[] positions = Position.values();
    for (int i = 0; i < playerCount; i ++) {
      Position position = positions[i % 5];
      Player player = new Player(Integer.toString(i), position);
      players.add(player);
    }
    return players;
  }
  @BeforeEach
  public void setup(){
    wlSkill = new WLSkill();
  }
  @Test
  public void simpleWlSkill() throws IOException {
    List<Player> players = makePlayers(10);
    SimpleMatchMaker simpleMatchMaker = new SimpleMatchMaker();
    List<Match> matches = simpleMatchMaker.matchmaker(players, 2);
    Match match = matches.get(0);
    match.setOutcome(Outcome.TEAM1WIN);
    this.wlSkill.skillUpdater(match);
    for (Player player : match.getTeam1().getPlayers()) {
      Assert.assertEquals(player.getSkillLevel(), 1);
    }
    for (Player player : match.getTeam2().getPlayers()) {
      Assert.assertEquals(player.getSkillLevel(), 0);
    }
  }
  @Test void complicatedWlSkill() throws IOException {
    List<Player> players = makePlayers(10);
    for(int i = 0; i < 10; i++) {
      if (i % 2 == 0) {
        players.get(i).addWin();
      } else {
        players.get(i).addLoss();
      }
    }
    SimpleMatchMaker simpleMatchMaker = new SimpleMatchMaker();
    Match match = simpleMatchMaker.matchmaker(players, 2).get(0);
    match.setOutcome(Outcome.TEAM2WIN);
    this.wlSkill.skillUpdater(match);
    for (Player player : match.getTeam1().getPlayers()) {
      Assert.assertEquals(player.getSkillLevel(), 0.5);
    }
    for (Player player : match.getTeam1().getPlayers()) {
      Assert.assertEquals(player.getSkillLevel(), 0.5);
    }

  }
}
