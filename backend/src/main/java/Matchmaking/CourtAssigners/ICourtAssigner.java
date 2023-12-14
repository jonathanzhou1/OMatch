package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.LinkedList;
import java.util.List;

/**
 * Court Assigner interface for managing an arbitrary number of courts, and assigning people to
 * the best fitting court.
 */
public interface ICourtAssigner {

  /**
   * Takes in a new player and uses its positioning and skill level to add it to the most fitting
   * team.
   * @param newPlayers The new player being added to a match ahead of time.
   * @return The court containing the added player.
   */
  public Court addPlayers(List<Player> newPlayers);

  /**
   * Returns the array of courts in the assigner
   * @return the array of courts in the assigner
   */
  public ICourt[] getCourts();



}
