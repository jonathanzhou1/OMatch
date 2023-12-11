import "../../styles/index.css";
import {
  sussy_baka_json,
  joemungus_burger_json,
} from "./mock-data/MockProfiles";

export default function ViewProfile() {
  //get user id and make sure it is valid using type predicates
  let userID: string | null = localStorage.getItem("userID");
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

  return (
    <div id="viewProfile">
      <h1 className="welcomeHeader">VIEW PROFILE!</h1>
      <div id="buttonContainer">
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
        </div>
      </div>
    </div>
  );
}

function isString(temp: string | null): temp is string {
  return typeof temp === "string";
}
