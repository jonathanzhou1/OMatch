import "../../styles/index.css";
import { Link } from "react-router-dom";
import { useEffect } from "react";

export default function WelcomeBox() {
  //react state variable keeping track of which user is currently logged in
  let loggedInUser = localStorage.getItem("userEmail");

  //upon changing users, update local storage
  useEffect(() => {
    loggedInUser = localStorage.getItem("userEmail");
  }, [localStorage.getItem("userEmail")]);

  return (
    <div id="welcome">
      <h1 className="welcomeHeader">Welcome to OMatch!</h1>
      <h2>
        If you have an account, please sign in. <br></br> Otherwise, please
        create an account.
      </h2>
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
          ? `${loggedInUser} is logged in.`
          : "You are not logged in."}{" "}
      </p>
    </div>
  );
}
