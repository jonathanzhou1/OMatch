import "../../styles/index.css";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { signOut } from "@firebase/auth";
import { auth } from "../../firebase-config";
import { useEffect } from "react";

export default function Dashboard() {
  //get user email from local storage
  const userEmail = localStorage.getItem("userEmail");
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (userEmail == null) {
      console.log(`Dashboard currentUser: ${userEmail}`);
      navigate("/");
    }
  }, [location, userEmail, localStorage]);

  async function logOut() {
    await signOut(auth);
    localStorage.removeItem("userEmail");
    navigate("/");
  }

  return (
    <div id="welcome">
      <h1 className="userHeader">Welcome to OMatch, User: {userEmail}!</h1>
      <div id="buttonContainer">
        <Link to="/view-profile">
          <button
            className="button leftButton threeButtons"
            id="toViewProfileButton"
          >
            View Profile
          </button>
        </Link>
        <Link to="/match-team">
          <button
            className="button middleButton threeButtons"
            id="toMatchATeamButton"
          >
            Match a Team
          </button>
        </Link>
        <button
          className="button rightButton threeButtons"
          id="signOutButton"
          style={{ display: "inline-block" }}
          onClick={logOut}
        >
          Sign Out
        </button>
      </div>
    </div>
  );
}
