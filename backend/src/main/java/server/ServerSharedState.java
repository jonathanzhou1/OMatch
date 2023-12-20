package server;

import Matchmaking.CourtAssigners.ICourtAssigner;
import datastorage.DataStore;

/**
 * Shared state for the server class and for testing. Contains two methods for getting and mutating
 * the values carried within.
 */
public class ServerSharedState {

  private int playersPerCourt;
  private DataStore dataStore;
  private ICourtAssigner courtAssigner;

  /**
   * Constructs the shared state
   *
   * @param dataStore The datastore used by the server
   * @param courtAssigner The courtAssigner Used by the server
   */
  public ServerSharedState(DataStore dataStore, ICourtAssigner courtAssigner) {
    this.courtAssigner = courtAssigner;
    this.dataStore = dataStore;
    this.playersPerCourt = 10;
  }

  /**
   * Constructs the shared state
   *
   * @param dataStore The datastore used by the server
   * @param courtAssigner The courtAssigner Used by the server
   * @param numPlayersPerCourt the max number of players per court, usually set at 10
   */
  public ServerSharedState(
      DataStore dataStore, ICourtAssigner courtAssigner, int numPlayersPerCourt) {
    this.courtAssigner = courtAssigner;
    this.dataStore = dataStore;
    this.playersPerCourt = numPlayersPerCourt;
  }

  /**
   * Gets the list of current matches
   *
   * @return The list of current matches
   */
  public ICourtAssigner getCourtAssigner() {
    return this.courtAssigner;
  }

  /**
   * Gets the data store
   *
   * @return The data store used by the server
   */
  public DataStore getDataStore() {
    return this.dataStore;
  }

  /**
   * Gets the number of players within a court
   *
   * @return The number of players assigned to each court
   */
  public int getPlayersPerCourt() {
    return this.playersPerCourt;
  }
}
