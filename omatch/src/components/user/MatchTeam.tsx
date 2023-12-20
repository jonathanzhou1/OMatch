import { useState } from "react";
import "../../styles/index.css";

interface Player {
  id: string;
  name: string;
  position: string;
  skillLevel: number;
  wins: number;
  losses: number;
}

interface Team {
  avgSkill: number;
  players: Player[];
  size: number;
}

interface Match {
  outcome: string;
  team1: Team;
  team2: Team;
}

export default function MatchTeam() {
  //react state variable to display all matches
  const [showMatches, setShowMatches] = useState(false);
  const [matches, setMatches] = useState("");
  const [liveMatches, setLiveMatches] = useState<Match[]>([]);

  //errorMessage handling
  const [displayErrorMessage, setDisplayErrorMessage] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  //successMessage
  const [displaySuccessMessage, setDisplaySuccessMessage] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  //ask for win or loss upon ending match
  const [showGameResultQuery, setShowGameResultQuery] = useState(false);
  const [gameResult, setGameResult] = useState("");

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

  function formatMatches(liveMatches: Match[]) {
    //iterate through all live matches
    for (let matchIndex = 0; matchIndex < liveMatches.length; matchIndex++) {
      const curMatch = liveMatches[matchIndex];
      //get players from both teams and display
      const team1players: string[] = [];
      const team2players: string[] = [];
      for (
        let playerIndex = 0;
        playerIndex < curMatch.team1.players.length;
        playerIndex++
      ) {
        team1players.push(curMatch.team1.players[playerIndex].name);
        team2players.push(curMatch.team2.players[playerIndex].name);
      }
    }
    return (
      <div className="centerDiv">
        {liveMatches.map((match: Match, index) => (
          <div id="currentMatches">
            <h3>{`Match ${index + 1}`}</h3>
            <table>
              <tbody>
                <tr>
                  <th>Team 1</th>
                  <th>Team 2</th>
                </tr>
                <tr>
                  <td>
                    {match.team1.players.map((player: Player, index) => (
                      <p>{player.name}</p>
                    ))}
                  </td>
                  <td>
                    {match.team2.players.map((player: Player, index) => (
                      <p>{player.name}</p>
                    ))}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        ))}
      </div>
    );
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
        setLiveMatches(liveMatches); // NEW EDIT
        // let matchDescription: string = "";
        // //iterate through all live matches
        // for (
        //   let matchIndex = 0;
        //   matchIndex < liveMatches.length;
        //   matchIndex++
        // ) {
        //   const curMatch = liveMatches[matchIndex];
        //   const status: string = curMatch.outcome;
        //   //get players from both teams and display
        //   let team1players: string[] = [];
        //   let team2players: string[] = [];
        //   for (
        //     let playerIndex = 0;
        //     playerIndex < curMatch.team1.players.length;
        //     playerIndex++
        //   ) {
        //     team1players.push(curMatch.team1.players[playerIndex].name);
        //     team2players.push(curMatch.team2.players[playerIndex].name);
        //   }
        //   //set descriptive match string
        //   matchDescription = matchDescription.concat(
        //     "MATCH STATUS: " +
        //       status +
        //       ", TEAM 1 PLAYERS: " +
        //       team1players +
        //       ", TEAM 2 PLAYERS: " +
        //       team2players +
        //       "; "
        //   );
        // }
        // if (matchDescription === "") {
        //   setMatches("No matches yet!");
        // } else {
        //   setMatches(matchDescription);
        // }
        setShowMatches(true);
      });
  }

  function queryResult() {
    setDisplayErrorMessage(false);
    setDisplaySuccessMessage(false);
    setShowGameResultQuery(true);
  }

  async function endMatch() {
    if (gameResult === "") {
      setDisplayErrorMessage(true);
      setDisplaySuccessMessage(false);
      setErrorMessage("Please select a game result.");
    } else {
      let hostname = "http://localhost";
      let port = ":3232";
      let endMatchQuery =
        "/match-end?&id=" + userID + "&playerWon=" + gameResult;
      console.log(endMatchQuery);
      await fetch(hostname + port + endMatchQuery)
        .then((response) => response.json())
        .then((responseJSON) => {
          if (responseJSON.result !== "success") {
            setDisplayErrorMessage(true);
            setDisplaySuccessMessage(false);
            setErrorMessage(responseJSON.details);
          } else {
            setDisplayErrorMessage(false);
            setDisplaySuccessMessage(true);
            setShowGameResultQuery(false);
            setSuccessMessage("Game result has been updated to your profile");
          }
        })
        .catch((_error) => {
          setDisplayErrorMessage(true);
          setDisplaySuccessMessage(false);
          setErrorMessage("Error in ending match. Please try again!");
        });
    }
  }

  return (
    <div>
      <h1 id="matchHeader">MATCH TEAM!</h1>
      <div id="buttonContainer">
        <button className="button threeButtons leftButton" onClick={matchTeam}>
          Match Team
        </button>
        <button
          className="button threeButtons middleButton"
          onClick={viewMatches}
        >
          View Matches
        </button>
        <button
          className="button threeButtons rightButton"
          onClick={queryResult}
        >
          End Match
        </button>
        <h2 id="activeMatchesHeader">Active Matches</h2>
      </div>
      {showMatches && formatMatches(liveMatches)}
      {showGameResultQuery && (
        <div id="endMatch">
          <p>Please choose the game result: </p>
          <div className="radios">
            <div className="radio">
              <label>
                <input
                  type="radio"
                  name="win"
                  value="win"
                  aria-label="win"
                  onChange={(event) => setGameResult(event.target.value)}
                  checked={gameResult === "win"}
                ></input>
                Win
              </label>
            </div>
            <div className="radio">
              <label>
                <input
                  type="radio"
                  name="loss"
                  value="lose"
                  aria-label="lose"
                  onChange={(event) => setGameResult(event.target.value)}
                  checked={gameResult === "lose"}
                ></input>
                Loss
              </label>
            </div>
            <div className="radio">
              <label>
                <input
                  type="radio"
                  name="draw"
                  value="tie"
                  aria-label="tie"
                  onChange={(event) => setGameResult(event.target.value)}
                  checked={gameResult === "tie"}
                ></input>
                Draw
              </label>
            </div>
          </div>

          <button
            className="button threeButtons"
            id="submitGameResult"
            onClick={endMatch}
          >
            Submit Result
          </button>
        </div>
      )}
      {displaySuccessMessage && <p>Success: {successMessage}</p>}
      {displayErrorMessage && (
        <p className="redText">Failure: {errorMessage}</p>
      )}
    </div>
  );
}
