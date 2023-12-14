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
public class Court implements ICourt{

  private Match match;
  private List<Player> players;

  public Court(){
    this.match = null;
    players = new LinkedList<>();
  }

  public Court(Match match, List<Player> players){
    this.match = match;
    this.players = players;
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
    if(this.match == null){
      throw new IllegalStateException("Match is currently going on, cannot currently "
          + "add more players");
    }else {
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

  /**
   * Sets the match to a new value.
   */
  @Override
  public void setMatch(Match newMatch) {
    this.match = newMatch;
  }
}
