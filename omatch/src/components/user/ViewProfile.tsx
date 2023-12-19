import { Link, useNavigate } from "react-router-dom";
import "../../styles/index.css";
import {
  sussy_baka_json,
  joemungus_burger_json,
  tingus_pingus_json,
} from "./mock-data/MockProfiles";
import { useEffect, useState } from "react";
import {
  AuthError,
  AuthErrorCodes,
  EmailAuthProvider,
  deleteUser,
  reauthenticateWithCredential,
} from "firebase/auth";
import { auth } from "../../firebase-config";

function MockView() {
  //get user id and make sure it is valid using type predicates
  let userID: string | null = localStorage.getItem("userID");
  let userEmail: string | null = localStorage.getItem("userEmail");
  //use type predicate to narrow type of userID down
  if (!isString(userID)) {
    userID = "invalid ID smh my head";
    //do some error message here
  }
  //get user profile data using userID
  //fetch() -- when integrated, fetch data from backend rather than mock
  let profileInfo: string;
  let existingMock: boolean = true;
  if (userID === "3UwX76jzYJNSpdYlHC4rVbreP8n2") {
    profileInfo = joemungus_burger_json;
  } else if (userID === "Zg0FeoUOtxVIq1Q1NFdq8K3AbBP2") {
    profileInfo = sussy_baka_json;
  } else if (userEmail === "newplayer@gmail.com") {
    profileInfo = tingus_pingus_json;
  } else {
    profileInfo = "THIS ID IS UNMOCKED :(";
    existingMock = false;
  }

  //parse data and get profile information
  let parsedJSON;
  let name: string = "PLACEHOLDER";
  let position: string = "PLACEHOLDER";
  if (existingMock === true) {
    parsedJSON = JSON.parse(profileInfo);
    name = parsedJSON.name;
    position = parsedJSON.position;
  }
}

export default function ViewProfile() {
  //React state variable to keep track of password
  const [password, setPassword] = useState("");
  const [needPassword, setNeedPassword] = useState(false);
  //React state variables for delete profile confirmation
  const [displayDeleteConfirmation, setDisplayDeleteConfirmation] =
    useState(false);
  //redMessage = errorMessage
  const [displayRedMessage, setDisplayRedMessage] = useState(false);
  const [redMessage, setRedMessage] = useState("");
  //React state variable to keep track of profile info
  const [name, setName] = useState("");
  const [wins, setWins] = useState("");
  const [position, setPosition] = useState("");
  const [losses, setLosses] = useState("");

  const navigate = useNavigate();
  //get user id and make sure it is valid using type predicates
  let userID: string | null = localStorage.getItem("userID");
  let userEmail: string | null = localStorage.getItem("userEmail");

  useEffect(() => {
    //load profile information from backend
    //initialize variables used
    const localhost = "http://localhost";
    const port = ":3232";
    const viewQuery = "/profile-view?id=" + userID;
    fetch(localhost + port + viewQuery)
      .then((response) => response.json())
      .then((responseJSON) => {
        console.log(responseJSON);
        if (responseJSON.result !== "success") {
          setDisplayRedMessage(true);
          setRedMessage(responseJSON.details);
        } else {
          setDisplayRedMessage(false);
          setName(responseJSON.player.name);
          setWins(responseJSON.player.wins);
          setLosses(responseJSON.player.losses);
          setPosition(responseJSON.player.position);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  //ask for confirmation to delete
  function doubleCheckDelete() {
    setDisplayDeleteConfirmation(true);
    setRedMessage("Are you sure you want to delete your profile?");
    setDisplayRedMessage(true);
  }

  //reset confirmation with state variable
  function resetConfirmation() {
    setDisplayDeleteConfirmation(false);
    setDisplayRedMessage(false);
  }

  //get password for reauthentication
  function getPassword() {
    setDisplayDeleteConfirmation(false);
    setDisplayRedMessage(false);
    setNeedPassword(true);
  }

  //function that runs only upon password being submitted
  async function deleteProfile() {
    const curUser = auth.currentUser;
    if (!isString(userEmail)) {
      userEmail = "";
    }
    const curCredential = EmailAuthProvider.credential(userEmail, password);
    if (curUser != null) {
      //reauthenticate with email and password
      await reauthenticateWithCredential(curUser, curCredential)
        .then(async (_curUserCredential) => {
          //delete newly authenticated user
          await deleteUser(curUser)
            .then(async () => {
              //remove data from backend
              let hostname = "http://localhost";
              let port = ":3232";
              let deleteProfileQuery =
                "/profile-edit?action=delete&id=" + userID;
              //even if backend fails to delete, still continue
              //because account is still deleted by firebase
              await fetch(hostname + port + deleteProfileQuery);

              //remove local storage data
              localStorage.removeItem("userEmail");
              localStorage.removeItem("userID");
              //return back to landing page
              return navigate("/");
            })
            .catch(() => setRedMessage("Failed to delete user."));
        })
        .catch((error: AuthError) => {
          //upon error, check what type of error and return descriptive error message.
          const errorCode = error.code;
          switch (errorCode) {
            case AuthErrorCodes.INVALID_LOGIN_CREDENTIALS: {
              setRedMessage(
                "Invalid password given for the account associated with email: " +
                  userEmail +
                  "."
              );
              break;
            }
            default: {
              setRedMessage(
                "Invalid account authentication. Please try again!"
              );
            }
          }
          //display error
          setDisplayRedMessage(true);
        });
    }
  }

  return (
    <div id="viewProfile">
      <h1 id="viewProfileHeader">VIEW PROFILE!</h1>
      <div id="buttonContainer">
        {/* *USE FOR MOCKS
        <p>NOTE: Mocked Profile</p>
        <div id="profileInfo">
          <h3>User Info</h3>
          {existingMock && (
            <p>
              <b>Name: </b> {name}
              <br></br>
              <b>Position: </b> {position}
            </p>
          )}
          {!existingMock && <p>{profileInfo}</p>}
        </div> */}
        <div id="profileInfo">
          <h3>User Info</h3>
          <p>
            <b>Name: </b> {name}
            <br></br>
            <b>Position: </b> {position}
            <br></br>
            <b>Wins: </b> {wins}
            <br></br>
            <b>Losses: </b> {losses}
          </p>
        </div>
        <div>
          <Link to="/edit-profile">
            <button className="button singleButton" id="editProfileButton">
              Edit Profile
            </button>
          </Link>
        </div>
        <div>
          <button
            className="button singleButton"
            id="deleteProfileButton"
            onClick={doubleCheckDelete}
          >
            Delete Profile
          </button>
        </div>
        {displayRedMessage && <p className="redText">{redMessage}</p>}
        {displayDeleteConfirmation && (
          <div>
            <button
              className="button twoButtons leftButton"
              onClick={getPassword}
            >
              Yes
            </button>
            <button
              className="button twoButtons rightButton"
              onClick={resetConfirmation}
            >
              No
            </button>
          </div>
        )}
        {needPassword && (
          <div>
            <p>Please reinput your password: </p>
            <input
              className="input"
              aria-label="passwordInput"
              type="password"
              onChange={(event) => {
                setPassword(event.target.value);
              }}
              onKeyDown={(event) => {
                if (event.key === "Enter") {
                  event.preventDefault();
                  deleteProfile();
                }
              }}
            ></input>
            <button
              className="button singleButton"
              id="submitPassword"
              onClick={deleteProfile}
            >
              Submit Password
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

//Type predicate for string
export function isString(temp: string | null): temp is string {
  return typeof temp === "string";
}
