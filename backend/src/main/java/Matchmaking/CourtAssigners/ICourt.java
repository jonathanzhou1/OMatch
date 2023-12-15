package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Outcome;
import Matchmaking.Player;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import server.exceptions.NoItemFoundException;

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

  /**
   * Takes a player and uses their data to vote to end the game.
   *
   * @param player The player voting to end the game
   * @param playerWon Whether the player won that particular game. Can be "win", "tie", or "loss".
   * @return Whether their vote was enough to end the game.
   */
  boolean tryEndGame(Player player, String playerWon) throws Exception;
}
