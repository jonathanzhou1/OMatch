import "../../styles/index.css";
import { useState } from "react";
import {
  createUserWithEmailAndPassword,
  AuthErrorCodes,
  AuthError,
} from "firebase/auth";
import { auth } from "../../firebase-config";
import { useNavigate } from "react-router-dom";
import UserProfile from "../user/mock-data/MockProfiles";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function CreateAccountBox() {
  const [registerEmail, setRegisterEmail] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");

  const [errorStatus, setErrorStatus] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [position, setPosition] = useState("");

  const navigate = useNavigate();

  async function register() {
    await createUserWithEmailAndPassword(auth, registerEmail, registerPassword)
      .then((userCredential) => {
        const user = userCredential.user;
        const newProfile: UserProfile = {
          id: user.uid,
          name: `${firstName} ${lastName}`,
          position: position,
        };
        console.log(user);
        console.log(newProfile);
        localStorage.setItem("userEmail", registerEmail);
        // localStorage acts as a KV-store locally on the browser
        localStorage.setItem("userID", user.uid);
        return navigate("/dashboard");
      })
      .catch((error: AuthError) => {
        const errorCode = error.code;
        switch (errorCode) {
          case AuthErrorCodes.EMAIL_EXISTS: {
            setErrorMessage("Account with this email already exists!");
            break;
          }
          case AuthErrorCodes.INVALID_EMAIL: {
            setErrorMessage("Invalid email. Please try again!");
            break;
          }
          case AuthErrorCodes.WEAK_PASSWORD: {
            setErrorMessage("Password should be at least 6 characters.");
            break;
          }
          default: {
            setErrorMessage(
              "Invalid account information inputted. Please try again!"
            );
          }
        }
        setErrorStatus(true);
        console.log(error.message);
      });
  }

  return (
    <div id="createAccount">
      <h1 className="welcomeHeader">Create Account</h1>
      <h2>Email</h2>
      <input
        aria-label="emailInput"
        type="email"
        onChange={(event) => {
          setRegisterEmail(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            register();
          }
        }}
      ></input>
      <h2>Password</h2>
      {/* Consider adding minLength and pattern properties for more secure passwords */}
      <input
        aria-label="passwordInput"
        type="password"
        onChange={(event) => {
          setRegisterPassword(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            register();
          }
        }}
      ></input>
      <h2>First Name</h2>
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
        <button className="button singleButton" onClick={register}>
          Create account
        </button>
        {errorStatus && <p className="redText">{errorMessage}</p>}
      </div>
    </div>
  );
}
