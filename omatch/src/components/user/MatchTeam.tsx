import { useState } from "react";
import "../../styles/index.css";

export default function MatchTeam() {
  //react state variable to display all matches
  const [showMatches, setShowMatches] = useState(false);
  const [matches, setMatches] = useState("");

  const userID = localStorage.getItem("userId");
  //call backend to match team
  async function matchTeam() {
    let hostname = "http://localhost";
    let port = ":3232";
    let addMatchQuery = "/match-add?id=" + userID;
    //even if backend fails to delete, still continue
    //because account is still deleted by firebase
    await fetch(hostname + port + addMatchQuery);
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
        if (curMatches === "") {
          setMatches("There are no current matches");
        } else {
          setMatches(curMatches);
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
      {showMatches && (
        <p id="showMatches">
          <b>Current Matches:</b> {matches}
        </p>
      )}
    </div>
  );
}
