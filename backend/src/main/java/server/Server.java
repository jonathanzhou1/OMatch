package server;

import Matchmaking.IMatch;
import Matchmaking.MatchAlgs.IMatchMaker;
import Matchmaking.SkillCalculators.SkillUpdater;
import datastorage.DataStore;
import datastorage.SimpleDataStore;
import java.util.ArrayList;
import server.handlers.MatchViewHandler;
import server.handlers.ProfileAddHandler;
import server.handlers.ProfileEditHandler;
import server.handlers.ProfileViewHandler;
import spark.Spark;

public class Server {
  static final int port = 3232;

  private DataStore dataStore;
  private ArrayList<IMatch> matches;
  private IMatchMaker matchMaker;
  private SkillUpdater skillUpdater;

  // Shared state variables

  /** */
  public Server() {

    this.dataStore = new SimpleDataStore();

    Spark.port(port);

    // Set up handlers:
    Spark.get("profile-add", new ProfileAddHandler(this));
    Spark.get("profile-edit", new ProfileEditHandler(this));
    Spark.get("profile-view", new ProfileViewHandler(this));
    Spark.get("match-view", new MatchViewHandler(this));

    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);

    System.out.println("\n- - - - - - - ENDPOINTS - - - - - - -\n");

    System.out.println("For Profile Viewing:  http://localhost:" + port + "/profile-view");
    System.out.println("For Profile Adding:   http://localhost:" + port + "/profile-add");
    System.out.println("For Profile Editing:  http://localhost:" + port + "/profile-edit");
    System.out.println("For Match Editing:    http://localhost:" + port + "/match-edit");
    System.out.println("For Match Viewing:    http://localhost:" + port + "/match-view");
  }

  /**
   * Gets the list of current matches
   *
   * @return The list of current matches
   */
  public ArrayList<IMatch> getMatches() {
    return this.matches;
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
   * Gets the skill updater used by this server
   *
   * @return The skill updater used by this server
   */
  public SkillUpdater getSkillUpdater() {
    return this.skillUpdater;
  }

  /**
   * Gets the matchmaker used by this server
   *
   * @return The matchmaker used by this server
   */
  public IMatchMaker getMatchMaker() {
    return this.matchMaker;
  }


  /**
   * Main method. Run to initialize server.
   * @param args
   */
  public static void main(String[] args) {
    Server server = new Server();
  }
}
