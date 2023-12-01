import "../../styles/index.css";
import { useState } from "react";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth } from "../../firebase-config";
import { Link } from "react-router-dom";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function CreateAccountBox() {
  const [registerEmail, setRegisterEmail] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");

  async function register() {
    try {
      const user = await createUserWithEmailAndPassword(
        auth,
        registerEmail,
        registerPassword
      );
      console.log(user);
    } catch (error: any) {
      console.log(error.message);
    }
  }

  return (
    <div id="createAccount">
      <h1>Create Account</h1>
      <h2>Email</h2>
      <input
        type="email"
        onChange={(event) => {
          setRegisterEmail(event.target.value);
        }}
      ></input>
      <h2>Password</h2>
      {/* Consider adding minLength and pattern properties for more secure passwords */}
      <input
        type="password"
        onChange={(event) => {
          setRegisterPassword(event.target.value);
        }}
      ></input>
      <Link to="/">
        <button onClick={register}>Create account</button>
      </Link>
    </div>
  );
}
