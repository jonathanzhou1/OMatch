import "../../styles/index.css";
import { useState } from "react";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth } from "../../firebase-config";
import { Link, useNavigate } from "react-router-dom";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function CreateAccountBox() {
  const [registerEmail, setRegisterEmail] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");

  const [errorStatus, setErrorStatus] = useState(false);
  const navigate = useNavigate();

  async function register() {
    try {
      const user = await createUserWithEmailAndPassword(
        auth,
        registerEmail,
        registerPassword
      );
      console.log(user);
      return navigate("/");
    } catch (error: any) {
      console.log(error.message);
      setErrorStatus(true);
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
      <div id="buttonContainer">
        <button className="button singleButton" onClick={register}>
          Create account
        </button>
        {errorStatus && (
          <p className="redText">
            Invalid account information inputted. Please try again!
          </p>
        )}
      </div>
    </div>
  );
}
