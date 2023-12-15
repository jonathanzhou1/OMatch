package Matchmaking.CourtAssigners;

import Matchmaking.Player;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import server.exceptions.NoItemMadeException;

/**
 * Court Assigner interface for managing an arbitrary number of courts, and assigning people to the
 * best fitting court.
 */
public interface ICourtAssigner {

  /**
   * Takes in a new player and uses its positioning and skill level to add it to the most fitting
   * team.
   *
   * @param newPlayers The new player being added to a match ahead of time.
   * @return The index of the court containing the added player.
   */
  public int addPlayers(Queue<Player> newPlayers) throws NoItemMadeException, IOException;

  /**
   * Returns the array of courts in the assigner
   *
   * @return the array of courts in the assigner
   */
  public ICourt[] getCourts();

  /**
   * @param index
   */
  public Map<String, Player> removeInternalCourt(int index);
}
