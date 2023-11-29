package Matchmaking.SimpleMatching;

import java.util.List;

public class SimpleMatchMaker {

  public SimpleMatch simpleMatchmaking(List<SimplePlayer> players){
    SimpleTeam team1 = new SimpleTeam();
    SimpleTeam team2 = new SimpleTeam();
    for (int i = 0; i < players.size(); i++) {
      if(i % 2== 0){
        team1.addPlayer(players.get(i));
      }
      else{
        team2.addPlayer(players.get(i));
      }
    }
    return new SimpleMatch(team1, team2);
    }
  }
