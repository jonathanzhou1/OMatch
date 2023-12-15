package server;

import static spark.Spark.after;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.CourtAssigners.ICourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.SkillCalculators.SimpleSkill;
import datastorage.DataStore;
import datastorage.SimpleDataStore;
import server.handlers.match.MatchEndHandler;
import server.handlers.match.MatchViewHandler;
import server.handlers.profile.ProfileAddHandler;
import server.handlers.profile.ProfileEditHandler;
import server.handlers.profile.ProfileViewHandler;
import server.handlers.queue.QueueAddHandler;
import server.handlers.queue.QueueViewHandler;
import spark.Spark;

public class Server {
  static final int port = 3232;

  private DataStore dataStore;
  private ICourtAssigner courtAssigner;

  // Shared state variables

  /** */
  public Server(DataStore dataStore, ICourtAssigner courtAssigner) {

    this.dataStore = dataStore;
    this.courtAssigner = courtAssigner;

    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Set up handlers:
    Spark.get("profile-add", new ProfileAddHandler(this));
    Spark.get("profile-edit", new ProfileEditHandler(this));
    Spark.get("profile-view", new ProfileViewHandler(this));
    Spark.get("match-end", new MatchEndHandler(this));
    Spark.get("match-view", new MatchViewHandler(this));
    Spark.get("queue-add", new QueueAddHandler(this));
    Spark.get("queue-view", new QueueViewHandler(this));

    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);

    System.out.println("\n- - - - - - - ENDPOINTS - - - - - - -\n");

    System.out.println("For Profile Viewing:  http://localhost:" + port + "/profile-view");
    System.out.println("For Profile Adding:   http://localhost:" + port + "/profile-add");
    System.out.println("For Profile Editing:  http://localhost:" + port + "/profile-edit");
    System.out.println("For Match Ending:     http://localhost:" + port + "/match-end");
    System.out.println("For Match Viewing:    http://localhost:" + port + "/match-view");
    System.out.println("For Queue Viewing:    http://localhost:" + port + "/queue-view");
    System.out.println("For Queue Adding:     http://localhost:" + port + "/queue-add");
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
   * Main method. Run to initialize server. Instantiates the server with a mocked data store, court
   * assigner, matchmaker, and skill assigner.
   *
   * @param args
   */
  public static void main(String[] args) {
    Server server =
        new Server(
            new SimpleDataStore(), new CourtAssigner(6, new SimpleMatchMaker(), new SimpleSkill()));
  }
}
