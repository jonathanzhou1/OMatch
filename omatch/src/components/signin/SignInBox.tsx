import { auth } from "../../firebase-config";
import "../../styles/index.css";
import { useState } from "react";
import {
  AuthError,
  AuthErrorCodes,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { useNavigate } from "react-router-dom";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function SignInBox() {
  //state variables to show progress logging in
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  //state variable to store whether there is an error logging in
  const [errorStatus, setErrorStatus] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  //used for navigation between website endpoints
  const navigate = useNavigate();

  async function login() {
    await signInWithEmailAndPassword(auth, loginEmail, loginPassword)
      .then((userCredential) => {
        const user = userCredential.user;
        // localStorage acts as a KV-store locally on the browser
        //store email and user id locally for later use
        localStorage.setItem("userEmail", loginEmail);
        localStorage.setItem("userID", user.uid);
        return navigate("/dashboard");
      })
      .catch((error: AuthError) => {
        //return descriptive error message based on type of error
        const errorCode = error.code;
        switch (errorCode) {
          case AuthErrorCodes.INVALID_EMAIL: {
            setErrorMessage("Invalid email. Please try again!");
            break;
          }
          case AuthErrorCodes.INVALID_LOGIN_CREDENTIALS: {
            setErrorMessage("Invalid login. Please try again!");
            break;
          }
          default: {
            setErrorMessage(
              "Invalid account information inputted. Please try again!"
            );
          }
        }
        setErrorStatus(true);
      });
  }

  return (
    <div id="signIn">
      <h1 className="welcomeHeader">Sign In</h1>
      <h2 id="lessTopMargin">Email</h2>
      <input
        className="input"
        aria-label="emailInput"
        type="email"
        onChange={(event) => {
          setLoginEmail(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            login();
          }
        }}
      ></input>
      <h2>Password</h2>
      <input
        className="input"
        aria-label="passwordInput"
        type="password"
        onChange={(event) => {
          setLoginPassword(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            login();
          }
        }}
      ></input>
      <div id="buttonContainer">
        <button
          className="button singleButton"
          id="signInButton"
          onClick={login}
        >
          Sign In
        </button>
        {errorStatus && <p className="redText">{errorMessage}</p>}
      </div>
    </div>
  );
}
