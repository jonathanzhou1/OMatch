package Matchmaking.CourtAssigners;

import Matchmaking.Match;
import Matchmaking.MatchAlgs.IMatchMaker;
import Matchmaking.Player;
import Matchmaking.SkillCalculators.SkillUpdater;
import java.util.LinkedList;

public class CourtAssigner implements ICourtAssigner{

  // Using an array here to minimize chances of swapping things around.
  private final ICourt[] courts;
  private IMatchMaker matchMaker;
  private SkillUpdater skillUpdater;

  /**
   * Constructs a CourtAssigner object for managing an arbitrary number of courts.
   * @param numCourts The total number of courts that the assigner will be managing
   * @param matchMaker The matchmaker used across all courts
   * @param skillUpdater The skill updater used across all courts
   */
  public CourtAssigner(int numCourts, IMatchMaker matchMaker, SkillUpdater skillUpdater){
    courts = new ICourt[numCourts];
    this.matchMaker = matchMaker;
    this.skillUpdater = skillUpdater;
  }

  /**
   * Takes in a new player and uses its positioning and skill level to add it to the most fitting
   * team.
   *
   * @param newPlayer The new player being added to a match ahead of time.
   * @return The court containing the added player.
   */
  @Override
  public Court addPlayer(Player newPlayer) {
    return null;
  }
}
