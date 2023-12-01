package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import java.util.List;

public interface IMatchMaker {
  public Match matchmaker(List<Player> players);
}
