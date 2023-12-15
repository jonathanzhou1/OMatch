package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.Outcome;
import Matchmaking.Player;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import server.exceptions.NoItemFoundException;

/**
 * Representation of a court containing players. Encapsulates match data with other data like more
 * raw access to the players.
 */
public class Court implements ICourt {

  private Match match;
  private final List<Player> players;
  private final Set<Player> leavingPlayers;
  private int outcome;

  public Court() {
    this.match = null;
    this.players = new LinkedList<>();
    this.leavingPlayers = new HashSet<>();
    outcome = 0;
  }

  public Court(Match match, List<Player> players) {
    this.match = match;
    this.players = players;
    this.leavingPlayers = new HashSet<>();
    outcome = 0;
  }

  /**
   * Gets the list of players currently assigned to the court.
   *
   * @return The list of players
   */
  @Override
  public List<Player> getPlayers() {
    return this.players;
  }

  /**
   * Adds a player to the court when a match is not currently running.
   *
   * @param player A player object representing someone's account
   */
  @Override
  public void addPlayer(Player player) throws IllegalStateException {
    if (this.match != null) {
      throw new IllegalStateException(
          "Match is currently going on, cannot currently " + "add more players");
    } else {
      this.players.add(player);
    }
  }

  /**
   * @return the current match that is going on.
   * @throws NoSuchElementException Thrown when the match hasn't been created yet
   */
  @Override
  public Match getMatch() throws NoSuchElementException {
    return this.match;
  }

  /** Sets the match to a new value. */
  @Override
  public void setMatch(Match newMatch) {
    this.match = newMatch;
  }

  /**
   * Takes a player and uses their data to vote to end the game.
   *
   * @param player The player voting to end the game
   * @param playerWon Whether the player won that particular game. Can be "win", "tie", or "loss".
   * @return Whether their vote was enough to end the game.
   */
  @Override
  public boolean tryEndGame(Player player, String playerWon) throws Exception {
    // If the player is leaving for the first time, Add their win/loss to the running tally
    if (this.leavingPlayers.add(player)) {
      // find out which team that player is on.
      boolean t1 = false;
      boolean t2 = false;
      try {
        match.getTeam1().getPlayer(player.getId());
        t1 = true;
      } catch (IOException ignored) {
      }

      try {
        match.getTeam2().getPlayer(player.getId());
        t2 = true;
      } catch (IOException ignored) {
      }

      if (!t1 && !t2) {
        throw new NoItemFoundException("Player is not in match");
      } else if (t1 && t2) {
        throw new Exception("Player found on both teams");
      }

      // Once we know what team the person is on, whether they won or lost can be
      // added to the running total
      if (!playerWon.equalsIgnoreCase("tie")) {
        if (t1) {
          if (playerWon.equalsIgnoreCase("win")) {
            this.outcome++;
          } else {
            this.outcome--;
          }
        }
        if (t2) {
          if (playerWon.equals("win")) {
            this.outcome--;
          } else {
            this.outcome++;
          }
        }
      }
    }

    // if all the players have left, return true so that the decommission of this
    // match object can begin

    if (this.leavingPlayers.size() == this.players.size()) {

      if (this.outcome > 0) {
        this.match.setOutcome(Outcome.TEAM1WIN);
      } else if (this.outcome < 0) {
        this.match.setOutcome(Outcome.TEAM2WIN);
      } else {
        this.match.setOutcome(Outcome.TIE);
      }

      return true;
    } else {
      return false;
    }
  }
}
