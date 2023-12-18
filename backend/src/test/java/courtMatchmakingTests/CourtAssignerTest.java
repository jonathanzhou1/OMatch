package courtMatchmakingTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.Outcome;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.SkillCalculators.SimpleSkill;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.junit.jupiter.api.Test;
import server.exceptions.NoItemMadeException;

/** Tests the court assigner, the component designed to create matches and manage court states */
public class CourtAssignerTest {

  /**
   * Tests the auto creation features of the court assigner, specifically when there aren't enough
   * people to make a match
   *
   * @throws NoItemMadeException
   * @throws IOException
   */
  @Test
  public void testCourtAssignerLowQueue() throws NoItemMadeException, IOException {
    CourtAssigner courtAssigner = new CourtAssigner(2, new SimpleMatchMaker(), new SimpleSkill());

    Queue<Player> playerQueue = new LinkedList<>(this.generatePlayerList());
    // Lower the size of the queue to 8
    playerQueue.poll();
    playerQueue.poll();

    Exception exception =
        assertThrows(
            NoItemMadeException.class,
            () -> {
              courtAssigner.addPlayers(playerQueue);
            });
    assertEquals("Queue length is not yet long enough for matchmaking.", exception.getMessage());
  }

  /**
   * Tests the auto creation features of the court assigner, specifically that of taking a queue and
   * making a match internally if that is possible, as well as removing it.
   *
   * @throws NoItemMadeException
   * @throws IOException
   */
  @Test
  public void testCourtAssignerAutoCreation() throws NoItemMadeException, IOException {
    CourtAssigner courtAssigner = new CourtAssigner(1, new SimpleMatchMaker(), new SimpleSkill());

    List<Player> players = this.generatePlayerList();
    Queue<Player> playerQueue = new LinkedList<>(players);
    assertEquals(0, courtAssigner.addPlayers(playerQueue));
    // Check that the new court exists
    assertNotNull(courtAssigner.getCourts()[0]);

    Exception exception =
        assertThrows(
            NoItemMadeException.class,
            () -> {
              courtAssigner.addPlayers(playerQueue);
            });
    assertEquals(
        "All Courts are full. Please wait for other players to finish", exception.getMessage());

    // Set the current court to the team 1 winstate, so that we can see players getting updated.
    courtAssigner.getCourts()[0].getMatch().setOutcome(Outcome.TEAM1WIN);
    Map<String, Player> playerMap = courtAssigner.removeInternalCourt(0);
    for (String k : playerMap.keySet()) {
      assertNotEquals(1500, playerMap.get(k).getSkillLevel());
    }
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
