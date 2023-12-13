package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.LinkedList;
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
  public LinkedList<Player> getPlayers();

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
