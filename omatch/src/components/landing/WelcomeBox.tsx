import "../../styles/index.css";
import { Link } from "react-router-dom";
import { auth } from "../../firebase-config";
import { User, onAuthStateChanged } from "firebase/auth";
import { useState } from "react";

export default function WelcomeBox() {
  const [loggedInUser, setLoggedInUser] = useState(auth.currentUser);
  console.log("before entering observer");
  console.log(loggedInUser);
  onAuthStateChanged(auth, (user) => {
    setLoggedInUser(user);
    console.log("entered onAuthStateChanged function");
    console.log(user);
  });

  return (
    <div id="welcome">
      <h1>Welcome to OMatch!</h1>
      <h2>[WELCOME MESSAGE + INSTRUCTIONS]</h2>
      <Link to="/signin">
        <button id="toSignInPageButton">Sign In</button>
      </Link>
      <Link to="/create-account">
        <button id="toCreateAccountPageButton">Create Account</button>
      </Link>
      <p> {loggedInUser?.email} are logged in.</p>
    </div>
  );
}
