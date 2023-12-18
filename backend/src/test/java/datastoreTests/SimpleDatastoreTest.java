package datastoreTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import Matchmaking.Player;
import Matchmaking.Position;
import datastorage.DataStore;
import datastorage.SimpleDataStore;
import org.junit.jupiter.api.Test;
import server.exceptions.ItemAlreadyExistsException;
import server.exceptions.NoItemFoundException;

/** Test class for the simpleDataStore class, upon which the current build depends on. */
public class SimpleDatastoreTest {

  /**
   * Tests that the datastore correctly handles addition of two otherwise identical players
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreAddition() throws ItemAlreadyExistsException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    assertEquals(2, testDS.getPlayers().size());
    assertEquals(player1, testDS.getPlayers().get("1234567890"));
    assertEquals(player2, testDS.getPlayers().get("234567890"));
  }

  /**
   * Tests that the datastore correctly handles addition and subsequent retrieval of two otherwise
   * identical players
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreRetrieval() throws ItemAlreadyExistsException, NoItemFoundException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    assertEquals(2, testDS.getPlayers().size());
    assertEquals(player1, testDS.getPlayer("1234567890"));
    assertEquals(player2, testDS.getPlayer("234567890"));
  }

  /**
   * Tests that the datastore correctly throws exceptions in correct circumstances
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreExceptionsGetAdd()
      throws ItemAlreadyExistsException, NoItemFoundException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    // Now add and get players that do not exist or already do.
    // This exception testing was found from the following site:
    // https://www.baeldung.com/junit-assert-exception

    Exception exception =
        assertThrows(
            ItemAlreadyExistsException.class,
            () -> {
              testDS.addPlayer(player1);
            });
    assertEquals(
        "Player to be added already exists within database. "
            + "Please use updatePlayer in this instance.",
        exception.getMessage());

    assertEquals(2, testDS.getPlayers().size());
    assertEquals(player1, testDS.getPlayer("1234567890"));
    assertEquals(player2, testDS.getPlayer("234567890"));

    exception =
        assertThrows(
            NoItemFoundException.class,
            () -> {
              testDS.getPlayer("nonsense");
            });
    assertEquals("No Player found with corresponding ID", exception.getMessage());
  }

  /**
   * Tests that the datastore correctly handles addition and subsequent editing of two otherwise
   * identical players
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreEditing() throws ItemAlreadyExistsException, NoItemFoundException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    Player player1New = new Player("BoshBoshington", Position.SHOOTING_GUARD, "1234567890");
    Player player2New = new Player("McBallin", Position.SMALL_FORWARD, "234567890");

    assertEquals(2, testDS.getPlayers().size());
    testDS.updatePlayer("1234567890", player1New);
    testDS.updatePlayer("234567890", player2New);

    assertEquals(player1New, testDS.getPlayer("1234567890"));
    assertEquals(player2New, testDS.getPlayer("234567890"));
  }

  /**
   * Tests that the datastore correctly handles deletion of players and attempting to retrieve them
   * after deletion results in an exception
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreDeletion() throws ItemAlreadyExistsException, NoItemFoundException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    Player player1New = new Player("BoshBoshington", Position.SHOOTING_GUARD, "1234567890");
    Player player2New = new Player("McBallin", Position.SMALL_FORWARD, "234567890");

    assertEquals(2, testDS.getPlayers().size());
    testDS.deleteItem("1234567890");
    testDS.deleteItem("234567890");
    assertEquals(0, testDS.getPlayers().size());

    Exception exception =
        assertThrows(
            NoItemFoundException.class,
            () -> {
              testDS.getPlayer("1234567890");
            });
    assertEquals("No Player found with corresponding ID", exception.getMessage());

    exception =
        assertThrows(
            NoItemFoundException.class,
            () -> {
              testDS.getPlayer("234567890");
            });
    assertEquals("No Player found with corresponding ID", exception.getMessage());
  }

  /**
   * Tests that the datastore queue correctly adds and displays the queue.
   *
   * @throws ItemAlreadyExistsException
   */
  @Test
  public void testDatastoreQueue() throws ItemAlreadyExistsException, NoItemFoundException {
    DataStore testDS = new SimpleDataStore();
    Player player1 = new Player("JoshJoshington", Position.CENTER, "1234567890");
    Player player2 = new Player("JoshJoshington", Position.CENTER, "234567890");
    assertEquals("1234567890", testDS.addPlayer(player1));
    assertEquals("234567890", testDS.addPlayer(player2));

    Player player1New = new Player("BoshBoshington", Position.SHOOTING_GUARD, "1234567890");
    Player player2New = new Player("McBallin", Position.SMALL_FORWARD, "234567890");

    assertEquals(2, testDS.getPlayers().size());
    testDS.addQueue("1234567890");
    testDS.addQueue("234567890");
    assertEquals(2, testDS.getPlayers().size());

    assertEquals(2, testDS.getQueue().size());

    Exception exception =
        assertThrows(
            NoItemFoundException.class,
            () -> {
              testDS.addQueue("123456");
            });
    assertEquals("Cannot Add Player To Queue, No ID Found", exception.getMessage());
  }
}
