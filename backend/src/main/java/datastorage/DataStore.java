package datastorage;

import Matchmaking.Player;
import java.util.Map;
import java.util.Queue;
import server.exceptions.ItemAlreadyExistsException;
import server.exceptions.NoItemFoundException;

public interface DataStore {

  /**
   * Returns the player when referenced by their ID. Throws a NoItemFoundException when the ID
   * doesn't reference a Player
   *
   * @param id A string containing the ID of the particular player we're referencing. Used primarily
   *     for internal purposes.
   * @return A Player object that corresponds to the request ID
   * @throws NoItemFoundException Thrown if there is no such element within the database.
   */
  public Player getPlayer(String id) throws NoItemFoundException;

  /**
   * Adds a player based on their ID and uses that to place them into the database. Throws an
   * ItemAlreadyExistsException upon a failure where the ID already exists in the system.
   *
   * @param player The Player that is to be inserted into the database
   * @return A string corresponding to their ID.
   * @throws ItemAlreadyExistsException Thrown if the item already exists within the database.
   */
  public String addPlayer(Player player) throws ItemAlreadyExistsException;

  /**
   * Updates a given player within the database
   *
   * @param id The ID of the player to be updated
   * @param player The updated player to be put into the database
   * @throws NoItemFoundException Thrown if there is no such element within the database.
   */
  public void updatePlayer(String id, Player player) throws NoItemFoundException;

  /**
   * Deletes the player referenced by their ID.
   *
   * @param id A string containing the ID of the particular player we're referencing. Used primarily
   *     for internal purposes.
   * @return The player that was just deleted. Null if there was no deleted player.
   * @throws NoItemFoundException In case no player has been deleted, throw an exception to notify *
   *     the caller of this
   */
  public Player deleteItem(String id) throws NoItemFoundException;

  /**
   * Gets a Map of players.
   *
   * @return The list of players in the form of an unmodifiable map from String ID to Player
   */
  public Map<String, Player> getPlayers();

  /**
   * Takes in a json and uses it to initialize the database with information
   *
   * @param fileJson The file which is used to populate the database
   */
  public void parseFile(String fileJson);

  /**
   * Method that adds a player to the Queue
   *
   * @param ID, player to be added to the queue
   */
  public Player addQueue(String ID) throws NoItemFoundException;

  /**
   * Returns the queue for getting player data
   *
   * @return The queue representing the current matchmaking queue
   */
  public Queue<Player> getQueue();
}
