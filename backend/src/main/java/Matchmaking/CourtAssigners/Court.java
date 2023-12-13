package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Representation of a court containing players. Encapsulates match data with other data like
 * more raw access to the players.
 */
public class Court implements ICourt{

  private Match match;
  private LinkedList<Player> players;

  /**
   * Gets the list of players currently assigned to the court.
   *
   * @return The list of players
   */
  @Override
  public LinkedList<Player> getPlayers() {
    return this.players;
  }

  /**
   * @return the current match that is going on.
   * @throws NoSuchElementException Thrown when the match hasn't been created yet
   */
  @Override
  public Match getMatch() throws NoSuchElementException {
    return this.match;
  }

  /**
   * Sets the match to a new value.
   */
  @Override
  public void setMatch(Match newMatch) {
    this.match = newMatch;
  }
}
