package courtMatchmakingTests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import Matchmaking.CourtAssigners.Court;
import Matchmaking.CourtAssigners.ICourt;
import Matchmaking.Match;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Outcome;
import Matchmaking.Player;
import Matchmaking.Position;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CourtTest {

  @Test
  public void testCourtAccess() {
    ICourt tCourt = new Court();
    List<Player> players = new LinkedList<>();
    Player testPlayer1 = new Player("a", Position.CENTER, "1");
    tCourt.addPlayer(testPlayer1);
    players.add(testPlayer1);
    Player testPlayer2 = new Player("b", Position.SMALL_FORWARD, "2");
    tCourt.addPlayer(testPlayer2);
    players.add(testPlayer2);
  }

  @Test
  public void testCourtMatching() {
    ICourt tCourt = new Court();
    List<Player> players = this.generatePlayerList();

    for (Player p : players) {
      tCourt.addPlayer(p);
      assertNull(tCourt.getMatch());
    }

    // Now that we've added the players, we can check that the players have all been added and that
    // the match has been properly created.
    assertArrayEquals(tCourt.getPlayers().toArray(new Player[0]), players.toArray(new Player[0]));
  }

  @Test
  public void testCourtMatchEndTie() throws Exception {
    ICourt tCourt = new Court();
    List<Player> players = this.generatePlayerList();

    for (Player p : players) {
      tCourt.addPlayer(p);
      assertNull(tCourt.getMatch());
    }

    // Make a match containing all the requisite players.
    Match match = new SimpleMatchMaker().matchmaker(players, 2).get(0);

    tCourt.setMatch(match);
    assertNotNull(tCourt.getMatch());

    assertEquals(Outcome.ONGOING, tCourt.getMatch().getOutcome());
    for (Player p : players) {
      tCourt.tryEndGame(p, "tie");
    }
    assertNotNull(tCourt.getMatch());
    assertEquals(Outcome.TIE, tCourt.getMatch().getOutcome());
  }

  @Test
  public void testCourtMatchEndWin1() throws Exception {
    ICourt tCourt = new Court();
    List<Player> players = this.generatePlayerList();

    for (Player p : players) {
      tCourt.addPlayer(p);
      assertNull(tCourt.getMatch());
    }

    // Make a match containing all the requisite players.
    Match match = new SimpleMatchMaker().matchmaker(players, 2).get(0);

    tCourt.setMatch(match);
    assertNotNull(tCourt.getMatch());

    assertEquals(Outcome.ONGOING, tCourt.getMatch().getOutcome());
    for (Player p : players) {
      if (tCourt.getMatch().getTeam1().getPlayers().contains(p)) {
        tCourt.tryEndGame(p, "win");
      } else {
        tCourt.tryEndGame(p, "loss");
      }
    }
    assertNotNull(tCourt.getMatch());
    assertEquals(Outcome.TEAM1WIN, tCourt.getMatch().getOutcome());
  }

  @Test
  public void testCourtMatchEndWin2() throws Exception {
    ICourt tCourt = new Court();
    List<Player> players = this.generatePlayerList();

    for (Player p : players) {
      tCourt.addPlayer(p);
      assertNull(tCourt.getMatch());
    }

    // Make a match containing all the requisite players.
    Match match = new SimpleMatchMaker().matchmaker(players, 2).get(0);

    tCourt.setMatch(match);
    assertNotNull(tCourt.getMatch());

    assertEquals(Outcome.ONGOING, tCourt.getMatch().getOutcome());
    for (Player p : players) {
      if (tCourt.getMatch().getTeam2().getPlayers().contains(p)) {
        tCourt.tryEndGame(p, "win");
      } else {
        tCourt.tryEndGame(p, "loss");
      }
    }
    assertNotNull(tCourt.getMatch());
    assertEquals(Outcome.TEAM2WIN, tCourt.getMatch().getOutcome());
  }

  private List<Player> generatePlayerList() {
    List<Player> players = new LinkedList<>();
    Player testPlayer1 = new Player("a", Position.CENTER, "1");
    players.add(testPlayer1);
    Player testPlayer2 = new Player("b", Position.SMALL_FORWARD, "2");
    players.add(testPlayer2);
    Player testPlayer3 = new Player("c", Position.POINT_GUARD, "3");
    players.add(testPlayer3);
    Player testPlayer4 = new Player("d", Position.POWER_FORWARD, "4");
    players.add(testPlayer4);
    Player testPlayer5 = new Player("e", Position.SHOOTING_GUARD, "5");
    players.add(testPlayer5);
    Player testPlayer6 = new Player("f", Position.CENTER, "6");
    players.add(testPlayer6);
    Player testPlayer7 = new Player("g", Position.SMALL_FORWARD, "7");
    players.add(testPlayer7);
    Player testPlayer8 = new Player("h", Position.POINT_GUARD, "8");
    players.add(testPlayer8);
    Player testPlayer9 = new Player("i", Position.POWER_FORWARD, "9");
    players.add(testPlayer9);
    Player testPlayerA = new Player("j", Position.SHOOTING_GUARD, "a");
    players.add(testPlayerA);
    return players;
  }
}
