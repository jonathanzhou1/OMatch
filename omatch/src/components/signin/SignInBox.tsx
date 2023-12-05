import { auth } from "../../firebase-config";
import "../../styles/index.css";
import { useState } from "react";
import { signInWithEmailAndPassword, signOut } from "firebase/auth";
import { Link, useNavigate } from "react-router-dom";
import { error } from "console";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function SignInBox() {
  //state variables to show progress logging in
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  //state variable to store whether there is an error logging in
  const [errorStatus, setErrorStatus] = useState(false);

  //used for navigation between website endpoints
  const navigate = useNavigate();

  async function login() {
    try {
      //log into firebase auth
      const user = await signInWithEmailAndPassword(
        auth,
        loginEmail,
        loginPassword
      );
      console.log(user); //Remove this later
      //localStorage acts as a KV-store locally on the browser
      localStorage.setItem("userEmail", loginEmail);
      //go to /dashboard endpoint, with Dashboard tsx
      return navigate("/dashboard");
    } catch (error: any) {
      //show descriptive error message
      setErrorStatus(true);
      console.log(error.message); //Remove this later
    }
  }

  // async function logout() {
  //   await signOut(auth);
  // }

  return (
    <div id="signIn">
      <h1>Sign In</h1>
      <h2>Email</h2>
      <input
        type="email"
        onChange={(event) => {
          setLoginEmail(event.target.value);
        }}
      ></input>
      <h2>Password</h2>
      <input
        type="password"
        onChange={(event) => {
          setLoginPassword(event.target.value);
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
        {errorStatus && (
          <p className="redText">
            Invalid login information inputted. Please try again!
          </p>
        )}
      </div>
    </div>
  );
}
