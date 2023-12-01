package server;

import java.io.FileNotFoundException;
import server.handlers.MatchHandler;
import server.handlers.ProfileHandler;
import spark.Spark;


public class Server {
  static final int port = 3232;

  // Shared state variables

  /**
   *
   * @param playerJson A filepath to a JSON which contains the requisite data
   */
  public Server(String playerJson){
    // load the Player data from the JSON into a hashmap from player to ID
    try {
      // TODO: Parse the player data into a Map<String (id), Player>. Should be mostly just loading the data into a reader and then running a bit of Moshi on it.

    }catch(FileNotFoundException e){
      System.err.println("Player Json file: " + playerJson + " could not be found. Server start aborted: " + e.getMessage());
    }

    // Set up handlers:
    Spark.get("profile", new ProfileHandler());
    Spark.get("match", new MatchHandler());

    Spark.awaitInitialization();


    System.out.println("Server started at http://localhost:" + port);

    System.out.println("\n- - - - - - - ENDPOINTS - - - - - - -\n");

    System.out.println("For Profile Actions:  http://localhost:" + port + "/profile");
    System.out.println("For Matching:         http://localhost:" + port + "/searchcsv");
  }

}
