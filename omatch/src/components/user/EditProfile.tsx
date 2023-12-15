import "../../styles/index.css";
import { useState } from "react";
import UserProfile from "./mock-data/MockProfiles";
import { isString } from "./ViewProfile";
import { useNavigate } from "react-router-dom";

export default function EditProfile() {
  const navigate = useNavigate();

  // Errorhandling react state variables
  const [errorStatus, setErrorStatus] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  //profile info state variables
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [position, setPosition] = useState("");

  async function edit() {
    let userID = localStorage.getItem("userID");
    if (!isString(userID)) {
      //how is this possible?? ERR
      setErrorStatus(true);
      setErrorMessage("Invalid UserID. Please re-login");
    } else if (position === "") {
      setErrorStatus(true);
      setErrorMessage("Please a select valid position");
    } else {
      //TEMP FUNCTION UNTIL WE CAN ACTUALLY UPDATE BACKEND
      const editedProfile: UserProfile = {
        id: userID,
        name: `${firstName} ${lastName}`,
        position: position,
      };
      console.log(editedProfile);
      const hostname = "http://localhost";
      const port = ":3232";
      const editProfileQuery =
        "/profile-edit?action=edit" +
        "&id=" +
        userID +
        "&name=" +
        `${firstName}%20${lastName}` +
        "&position=" +
        position;
      await fetch(hostname + port + editProfileQuery)
        .then((response) => response.json())
        .then((responseJson) => {
          if (responseJson.result !== "success") {
            setErrorStatus(true);
            setErrorMessage(responseJson.details);
            console.log(responseJson.details);
          } else {
            setErrorStatus(false);
            console.log("successful response from backend");
            return navigate("/view-profile");
          }
        });
    }
  }

  return (
    <div id="editProfile">
      <h1 id="editProfileHeader">Edit Profile</h1>
      <h2 id="lessTopMargin">First Name</h2>
      <input
        aria-label="firstNameInput"
        type="text"
        onChange={(event) => {
          setFirstName(event.target.value);
        }}
      />
      <h2>Last Name</h2>
      <input
        aria-label="lastNameInput"
        type="text"
        onChange={(event) => {
          setLastName(event.target.value);
        }}
      />
      <h2>Position</h2>
      <div id="position-select">
        <select
          aria-label="position-select"
          name="position"
          onChange={(event) => {
            setPosition(event.target.value);
            console.log(event.target.value);
          }}
        >
          <option value={""}>Choose a position</option>
          <option value={"POINT_GUARD"}>Point Guard</option>
          <option value={"SHOOTING_GUARD"}>Shooting Guard</option>
          <option value={"SMALL_FORWARD"}>Small Forward</option>
          <option value={"POWER_FORWARD"}>Power Forward</option>
          <option value={"CENTER"}>Center</option>
        </select>
      </div>
      <div id="buttonContainer">
        <button className="button singleButton" onClick={edit}>
          Edit account
        </button>
        {errorStatus && <p className="redText">{errorMessage}</p>}
      </div>
    </div>
  );
}
