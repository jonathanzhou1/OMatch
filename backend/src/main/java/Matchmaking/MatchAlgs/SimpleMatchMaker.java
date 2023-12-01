package Matchmaking.MatchAlgs;

import Matchmaking.Match;
import Matchmaking.Player;
import Matchmaking.Team;
import java.util.List;

public class SimpleMatchMaker implements IMatchMaker {

  public Match matchmaker(List<Player> players){
    Team team1 = new Team();
    Team team2 = new Team();
    for (int i = 0; i < players.size(); i++) {
      if(i % 2== 0){
        team1.addPlayer(players.get(i));
      }
      else{
        team2.addPlayer(players.get(i));
      }
    }
    return new Match(team1, team2);
    }
  }
