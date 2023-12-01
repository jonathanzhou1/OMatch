import { auth } from "../../firebase-config";
import "../../styles/index.css";
import { useState } from "react";
import { signInWithEmailAndPassword, signOut } from "firebase/auth";
import { Link } from "react-router-dom";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function SignInBox() {
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");

  async function login() {
    try {
      const user = await signInWithEmailAndPassword(
        auth,
        loginEmail,
        loginPassword
      );
      console.log(user);
    } catch (error: any) {
      console.log(error.message);
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
      <Link to="/">
        <button id="signInButton" onClick={login}>
          Sign In
        </button>
      </Link>
    </div>
  );
}
