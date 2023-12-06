import "../../styles/index.css";
import { Link } from "react-router-dom";
import { auth } from "../../firebase-config";
import { onAuthStateChanged } from "firebase/auth";
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
      <div id="buttonContainer">
        <Link to="/signin">
          <button
            className="button leftButton twoButtons"
            id="toSignInPageButton"
          >
            Sign In
          </button>
        </Link>
        <Link to="/create-account">
          <button
            className="button rightButton twoButtons"
            id="toCreateAccountPageButton"
          >
            Create Account
          </button>
        </Link>
      </div>
      <p>
        {" "}
        {loggedInUser
          ? `${loggedInUser.email} is logged in.`
          : "You are not logged in."}{" "}
      </p>
    </div>
  );
}
