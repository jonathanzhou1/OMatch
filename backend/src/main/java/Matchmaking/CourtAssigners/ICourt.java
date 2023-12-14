package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Representation of a court containing players. Encapsulates match data with other data like
 * more raw access to the players.
 */
public interface ICourt {

  /**
   * Gets the list of players currently assigned to the court.
   * @return The list of players
   */
  public List<Player> getPlayers();

  /**
   * Adds a player to the court when a match is not currently running.
   * @param player A player object representing someone's account
   */
  public void addPlayer(Player player) throws IllegalStateException;

  /**
   *
   * @return the current match that is going on.
   * @throws NoSuchElementException Thrown when the match hasn't been created yet
   */
  public Match getMatch() throws NoSuchElementException;

  /**
   * Sets the match to a new value.
   */
  public void setMatch(Match newMatch);

}
