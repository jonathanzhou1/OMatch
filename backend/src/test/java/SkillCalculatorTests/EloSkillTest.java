package SkillCalculatorTests;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Outcome;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.SkillCalculators.EloSkill;
import Matchmaking.SkillCalculators.WLSkill;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class EloSkillTest {
  EloSkill eloSkill;
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
    eloSkill = new EloSkill();
  }
  @Test
  public void simpleEloTest() throws IOException {
    List<Player> players = makePlayers(2);
    players.get(0).setSkillLevel(1200);
    players.get(1).setSkillLevel(1000);
    SimpleMatchMaker simpleMatchMaker = new SimpleMatchMaker();
    Match match = simpleMatchMaker.matchmaker(players, 2).get(0);
    match.setOutcome(Outcome.TEAM1WIN);
    this.eloSkill.skillUpdater(match);
    Assert.assertEquals(players.get(0).getSkillLevel(), 1207.2, 0.1);
    Assert.assertEquals(players.get(1).getSkillLevel(), 992.8, 0.1);

  }
}
