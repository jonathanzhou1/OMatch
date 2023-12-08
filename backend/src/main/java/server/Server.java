package server;

import Matchmaking.IMatch;
import datastorage.DataStore;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import server.handlers.MatchHandler;
import server.handlers.ProfileAddHandler;
import server.handlers.ProfileEditHandler;
import spark.Spark;


public class Server {
  static final int port = 3232;

  private DataStore dataStore;
  private ArrayList<IMatch> matches;


  // Shared state variables

  /**
   *
   * @param playerJson A filepath to a JSON which contains the requisite data
   */
  public Server(String playerJson){
    // load the Player data from the JSON into a hashmap from player to ID

    //try {
      // TODO: Parse the player data into a Map<String (id), Player>. Should be mostly just loading the data into a reader and then running a bit of Moshi on it.

    //}catch(FileNotFoundException e){
    //  System.err.println("Player Json file: " + playerJson + " could not be found. Server start aborted: " + e.getMessage());
    //}

    // Set up handlers:
    Spark.get("profile-add", new ProfileAddHandler(this));
    Spark.get("profile-edit", new ProfileEditHandler(this));
    Spark.get("match", new MatchHandler());

    Spark.awaitInitialization();


    System.out.println("Server started at http://localhost:" + port);

    System.out.println("\n- - - - - - - ENDPOINTS - - - - - - -\n");

    System.out.println("For Profile Adding:  http://localhost:" + port + "/profile-add");
    System.out.println("For Profile Editing:  http://localhost:" + port + "/profile-edit");
    System.out.println("For Matching:         http://localhost:" + port + "/searchcsv");
  }

  /**
   * Gets the list of current matches
   * @return The list of current matches
   */
  public ArrayList<IMatch> getMatches(){
    return this.matches;
  }

  /**
   * Gets the data store
   * @return The data store used by the server
   */
  public DataStore getDataStore(){
    return this.dataStore;
  }

}
