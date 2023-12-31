package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.IMatchMaker;
import Matchmaking.Player;
import Matchmaking.SkillCalculators.SkillUpdater;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import server.exceptions.NoItemMadeException;

public class CourtAssigner implements ICourtAssigner {

  // Using an array here to minimize chances of swapping things around.
  private final ICourt[] courts;
  boolean[] courtsFilled;
  private IMatchMaker matchMaker;
  private SkillUpdater skillUpdater;
  int numPlayersPerCourt = 10;

  /**
   * Constructs a CourtAssigner object for managing an arbitrary number of courts.
   *
   * @param numCourts The total number of courts that the assigner will be managing
   * @param matchMaker The matchmaker used across all courts
   * @param skillUpdater The skill updater used across all courts
   */
  public CourtAssigner(int numCourts, IMatchMaker matchMaker, SkillUpdater skillUpdater) {
    courts = new ICourt[numCourts];
    for (int i = 0; i < numCourts; i++) {
      courts[i] = new Court();
    }
    this.matchMaker = matchMaker;
    this.skillUpdater = skillUpdater;
    this.courtsFilled = new boolean[numCourts];
  }

  /**
   * Constructs a CourtAssigner object for managing an arbitrary number of courts.
   *
   * @param numCourts The total number of courts that the assigner will be managing
   * @param matchMaker The matchmaker used across all courts
   * @param skillUpdater The skill updater used across all courts
   */
  public CourtAssigner(
      int numCourts, IMatchMaker matchMaker, SkillUpdater skillUpdater, int numPlayersPerCourt) {
    courts = new ICourt[numCourts];
    for (int i = 0; i < numCourts; i++) {
      courts[i] = new Court();
    }
    this.matchMaker = matchMaker;
    this.skillUpdater = skillUpdater;
    this.courtsFilled = new boolean[numCourts];
    this.numPlayersPerCourt = numPlayersPerCourt;
  }

  /**
   * Takes in a new player and uses its positioning and skill level to add it to the most fitting
   * team.
   *
   * @param newPlayers The new player being added to a match ahead of time.
   * @return The index of the court containing the added player.
   */
  @Override
  public int addPlayers(Queue<Player> newPlayers) throws NoItemMadeException, IOException {
    List<Player> queuePlayers = List.of(newPlayers.toArray(new Player[0]));
    if (queuePlayers.size() < this.numPlayersPerCourt) {
      throw new NoItemMadeException("Queue length is not yet long enough for matchmaking.");
    } else {
      List<Match> matches =
          this.matchMaker.matchmaker(queuePlayers.subList(0, this.numPlayersPerCourt), 2);
      if (matches.size() == 1) {
        Court court = new Court(matches.get(0), queuePlayers.subList(0, this.numPlayersPerCourt));
        int courtMade = this.addInternalCourt(court);
        if (courtMade >= 0) {
          return courtMade;
        } else {
          throw new NoItemMadeException(
              "All Courts are full. Please wait for other players to finish");
        }
      } else {
        throw new NoItemMadeException("Matchmaker made more matches than expected.");
      }
    }
  }

  /**
   * Returns the array of courts in the assigner
   *
   * @return the array of courts in the assigner
   */
  @Override
  public ICourt[] getCourts() {
    return this.courts;
  }

  /**
   * Takes in an index and overwrites the court at that location, before updating the players based
   * on how that match went.
   *
   * @param index the index of the court to be overwritten
   * @return The list of newly updated players
   */
  @Override
  public Map<String, Player> removeInternalCourt(int index) {
    // Update all the players before adding them into the system.
    try {
      Match match = this.courts[index].getMatch();
      this.skillUpdater.skillUpdater(match);
    } catch (Exception ignored) {
    }

    Map<String, Player> playerMap = new HashMap<>();
    for (Player i : this.courts[index].getPlayers()) {
      playerMap.put(i.getId(), i);
    }
    this.courts[index] = new Court();
    return playerMap;
  }

  /**
   * Takes in a court and adds it into the list of courts
   *
   * @param court The court to be added
   * @return The index in the array where the court was added. -1 if addition was unsuccessful.
   */
  private int addInternalCourt(Court court) {
    int courtAddedIndex = -1;
    for (int i = 0; i < this.courtsFilled.length; i++) {
      if (!this.courtsFilled[i]) {
        this.courts[i] = court;
        courtAddedIndex = i;
        this.courtsFilled[i] = true;
        break;
      }
    }
    return courtAddedIndex;
  }
}
