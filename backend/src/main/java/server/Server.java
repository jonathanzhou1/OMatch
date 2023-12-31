package server;

import static spark.Spark.after;

import Matchmaking.CourtAssigners.CourtAssigner;
import Matchmaking.CourtAssigners.ICourtAssigner;
import Matchmaking.MatchAlgs.SimpleMatchMaker;
import Matchmaking.MatchAlgs.SortSkillMatchMaker;
import Matchmaking.Player;
import Matchmaking.Position;
import Matchmaking.SkillCalculators.EloSkill;
import Matchmaking.SkillCalculators.SimpleSkill;
import datastorage.DataStore;
import datastorage.SimpleDataStore;
import server.exceptions.ItemAlreadyExistsException;
import server.exceptions.NoItemFoundException;
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

  private ServerSharedState serverSharedState;

  // Shared state variables

  /** */
  public Server(DataStore dataStore, ICourtAssigner courtAssigner) {

    this.serverSharedState = new ServerSharedState(dataStore, courtAssigner);

    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Set up handlers:
    Spark.get("profile-add", new ProfileAddHandler(this.serverSharedState));
    Spark.get("profile-edit", new ProfileEditHandler(this.serverSharedState));
    Spark.get("profile-view", new ProfileViewHandler(this.serverSharedState));
    Spark.get("match-end", new MatchEndHandler(this.serverSharedState));
    Spark.get("match-view", new MatchViewHandler(this.serverSharedState));
    Spark.get("queue-add", new QueueAddHandler(this.serverSharedState));
    Spark.get("queue-view", new QueueViewHandler(this.serverSharedState));

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

  /** */
  public Server(DataStore dataStore, ICourtAssigner courtAssigner, int numPlayersPerCourt) {

    this.serverSharedState = new ServerSharedState(dataStore, courtAssigner, numPlayersPerCourt);

    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Set up handlers:
    Spark.get("profile-add", new ProfileAddHandler(this.serverSharedState));
    Spark.get("profile-edit", new ProfileEditHandler(this.serverSharedState));
    Spark.get("profile-view", new ProfileViewHandler(this.serverSharedState));
    Spark.get("match-end", new MatchEndHandler(this.serverSharedState));
    Spark.get("match-view", new MatchViewHandler(this.serverSharedState));
    Spark.get("queue-add", new QueueAddHandler(this.serverSharedState));
    Spark.get("queue-view", new QueueViewHandler(this.serverSharedState));

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
   * Main method. Run to initialize server. Instantiates the server with a mocked data store, court
   * assigner, matchmaker, and skill assigner.
   *
   * @param args The first index in args represents how the server is built. Can be empty for a
   *     simple configuration, 'complex' for the final planned implementation, or any other string,
   *     in which case the server will start out with 9 profiles already added in the simple
   *     configuration
   */
  public static void main(String[] args) throws ItemAlreadyExistsException, NoItemFoundException {

    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("complex")) {
        Server server =
            new Server(
                new SimpleDataStore(),
                new CourtAssigner(6, new SortSkillMatchMaker(), new EloSkill()));
      } else if(args[0].equalsIgnoreCase("JoshJoshington")){
        SimpleDataStore simple = new SimpleDataStore();

        // This is kinda bad tbh
        String[] ids = {
          "1234567890",
          "123456789",
          "12345678",
          "1234567",
          "123456",
          "12345",
          "1234",
          "123",
          "12",
          "1"
        };
        Position[] positions = {
          Position.CENTER, Position.POINT_GUARD, Position.SHOOTING_GUARD, Position.SMALL_FORWARD,
          Position.POWER_FORWARD, Position.CENTER, Position.POINT_GUARD, Position.SHOOTING_GUARD,
          Position.SMALL_FORWARD, Position.POWER_FORWARD
        };
        for (int i = 0; i < 9; i++) {
          Player temp = new Player("Josh Joshington", positions[i]);
          temp.setId(ids[i]);
          simple.addPlayer(temp);
          simple.addQueue(ids[i]);
        }
        Server server =
            new Server(simple, new CourtAssigner(6, new SimpleMatchMaker(), new SimpleSkill()));
      }else{
        Server server =
            new Server(new SimpleDataStore(), new CourtAssigner(6, new SimpleMatchMaker(), new SimpleSkill()), 1);
      }
    } else {
      Server server =
          new Server(
              new SimpleDataStore(),
              new CourtAssigner(6, new SimpleMatchMaker(), new SimpleSkill()));
    }
  }
}
