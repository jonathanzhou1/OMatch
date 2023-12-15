import { useState } from "react";
import "../../styles/index.css";
import { match } from "assert";

export default function MatchTeam() {
  //react state variable to display all matches
  const [showMatches, setShowMatches] = useState(false);
  const [matches, setMatches] = useState("");

  //errorMessage handling
  const [displayErrorMessage, setDisplayErrorMessage] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  //successMessage
  const [displaySuccessMessage, setDisplaySuccessMessage] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const userID = localStorage.getItem("userID");
  //call backend to match team
  async function matchTeam() {
    let hostname = "http://localhost";
    let port = ":3232";
    let addMatchQuery = "/queue-add?id=" + userID;
    await fetch(hostname + port + addMatchQuery)
      .then((response) => response.json())
      .then((responseJSON) => {
        //if successful query, then display success and remove error message
        if (responseJSON.result !== "success") {
          setErrorMessage(responseJSON.details);
          setDisplayErrorMessage(true);
          setDisplaySuccessMessage(false);
        } else {
          //if error message, then show it
          setSuccessMessage(responseJSON.Message);
          setDisplaySuccessMessage(true);
          setDisplayErrorMessage(false);
        }
      });
  }

  async function viewMatches() {
    let hostname = "http://localhost";
    let port = ":3232";
    let viewMatchesQuery = "/match-view";
    //match-view is always a successful query lol
    await fetch(hostname + port + viewMatchesQuery)
      .then((response) => response.json())
      .then((responseJSON) => {
        const curMatches = responseJSON.matches;
        let liveMatches = [];
        for (let courtIndex = 0; courtIndex < 6; courtIndex++) {
          if (curMatches[courtIndex] !== null) {
            liveMatches.push(curMatches[courtIndex]);
          }
        }
        let matchDescription: string = "";
        //iterate through all live matches
        for (
          let matchIndex = 0;
          matchIndex < liveMatches.length;
          matchIndex++
        ) {
          const curMatch = liveMatches[matchIndex];
          const status: string = curMatch.outcome;
          //get players from both teams and display
          let team1players: string[] = [];
          let team2players: string[] = [];
          for (
            let playerIndex = 0;
            playerIndex < curMatch.team1.players.length;
            playerIndex++
          ) {
            team1players.push(curMatch.team1.players[playerIndex].name);
            team2players.push(curMatch.team2.players[playerIndex].name);
          }
          //set descriptive match string
          matchDescription = matchDescription.concat(
            "MATCH STATUS: " +
              status +
              ", TEAM 1 PLAYERS: " +
              team1players +
              ", TEAM 2 PLAYERS: " +
              team2players +
              "; "
          );
        }
        if (matchDescription === "") {
          setMatches("No matches yet!");
        } else {
          setMatches(matchDescription);
        }
        setShowMatches(true);
      });
  }

  return (
    <div id="welcome">
      <h1 className="welcomeHeader">MATCH TEAM!</h1>
      <div id="buttonContainer">
        <button className="button twoButtons leftButton" onClick={matchTeam}>
          Match Team
        </button>
        <button className="button twoButtons rightButton" onClick={viewMatches}>
          View Matches
        </button>
      </div>
      {displaySuccessMessage && <p>Success: {successMessage}</p>}
      {displayErrorMessage && (
        <p className="redText">Failure: {errorMessage}</p>
      )}
      {showMatches && (
        <p>
          <b>Current Matches:</b> {matches}
        </p>
      )}
    </div>
  );
}
