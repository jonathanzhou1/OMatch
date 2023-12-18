package server;

import Matchmaking.CourtAssigners.ICourtAssigner;
import datastorage.DataStore;

/**
 * Shared state for the server class and for testing. Contains two methods for getting and mutating
 * the values carried within.
 */
public class ServerSharedState {

  /**
   * Constructs the shared state
   *
   * @param dataStore The datastore used by the server
   * @param courtAssigner The courtAssigner Used by the server
   */
  public ServerSharedState(DataStore dataStore, ICourtAssigner courtAssigner) {
    this.courtAssigner = courtAssigner;
    this.dataStore = dataStore;
  }

  private DataStore dataStore;
  private ICourtAssigner courtAssigner;

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
}
