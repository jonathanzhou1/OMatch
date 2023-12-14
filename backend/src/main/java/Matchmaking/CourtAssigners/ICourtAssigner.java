package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.LinkedList;

/**
 * Court Assigner interface for managing an arbitrary number of courts, and assigning people to
 * the best fitting court.
 */
public interface ICourtAssigner {

  /**
   * Takes in a new player and uses its positioning and skill level to add it to the most fitting
   * team.
   * @param newPlayer The new player being added to a match ahead of time.
   * @return The court containing the added player.
   */
  public Court addPlayer(Player newPlayer);



}
