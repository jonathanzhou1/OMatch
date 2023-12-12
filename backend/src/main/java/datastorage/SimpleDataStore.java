package datastorage;

import Matchmaking.Player;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import server.exceptions.ItemAlreadyExistsException;
import server.exceptions.NoItemFoundException;

/**
 * A simple implementation of the DataStore interface with mocking/early deployment in mind. Stores
 * all data in a HashMap from String to Player.
 */
public class SimpleDataStore implements DataStore {



  private HashMap<String, Player> dataMap;

  public SimpleDataStore(){
    this.dataMap = new HashMap<>();
  }

  /**
   * Returns the player when referenced by their ID. Throws a NoItemFoundException when the ID
   * doesn't reference a Player
   *
   * @param id A string containing the ID of the particular player we're referencing. Used primarily
   *     for internal purposes.
   * @return A Player object that corresponds to the request ID
   */
  @Override
  public Player getPlayer(String id) throws NoItemFoundException {
    if (!dataMap.containsKey(id)) {
      throw new NoItemFoundException("No Player found with corresponding ID");
    } else {
      return dataMap.get(id);
    }
  }

  /**
   * Adds a player based on their ID and uses that to place them into the database. Throws an
   * ItemAlreadyExistsException upon a failure where the ID already exists in the system.
   *
   * @param player The Player that is to be inserted into the database
   * @return A string corresponding to their ID.
   */
  @Override
  public String addPlayer(Player player) throws ItemAlreadyExistsException {
    if (dataMap.containsKey(player.getId())) {
      // ID is already in database, we can't be reasonably sure that this player already
      // exists within the database, or that two IDs are the same, so we change the ID of one of the
      // players and recurse.
      player.generateID();
      this.addPlayer(player);
      // throw new ItemAlreadyExistsException("Player to be added already exists within database. "
      //    + "Please use updatePlayer in this instance.");
    } else {
      dataMap.put(player.getId(), player);
    }
    return player.getId();
  }

  /**
   * Updates a given player within the database
   *
   * @param id The ID of the player to be updated
   * @param player The updated player to be put into the database
   * @throws NoItemFoundException Thrown if there is no such element within the database.
   */
  @Override
  public void updatePlayer(String id, Player player) throws NoItemFoundException {
    if (!dataMap.containsKey(player.getId())) {
      throw new NoItemFoundException("No player to update. Please use addPlayer in this instance.");
    } else {
      dataMap.put(player.getId(), player);
    }
  }

  /**
   * Deletes the player referenced by their ID.
   *
   * @param id A string containing the ID of the particular player we're referencing. Used primarily
   *     for internal purposes.
   * @return The player that was just deleted. Null if there was no deleted player.
   */
  @Override
  public Player deleteItem(String id) {
    if (dataMap.containsKey(id)) {
      // While I am aware that the standard HashMap returns null in these situations, it isn't
      // guaranteed. As of such I am adding a little bit of extra logic here.
      Player deletedPlayer = dataMap.get(id);
      dataMap.remove(id);
      return deletedPlayer;
    }
    return null;
  }

  /**
   * Gets a Map of players.
   *
   * @return The list of players in the form of an unmodifiable map from String ID to Player.
   */
  @Override
  public Map<String, Player> getPlayers() {
    return Collections.unmodifiableMap(dataMap);
  }

  /**
   * Takes in a json and uses it to initialize the database with information
   *
   * @param fileJson The file which is used to populate the database
   */
  @Override
  public void parseFile(String fileJson) {}
}
