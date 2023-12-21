import "../../styles/index.css";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { signOut } from "@firebase/auth";
import { auth } from "../../firebase-config";
import { useEffect } from "react";

export default function Dashboard() {
  //get user email from local storage
  const userEmail = localStorage.getItem("userEmail");

  //navigate and location just used for navigating to links in router
  const navigate = useNavigate();
  const location = useLocation();

  //have dashboard update accordingly whenever changes are made
  useEffect(() => {
    if (localStorage.getItem("userEmail") == null) {
      navigate("/");
    }
  }, [location, navigate]);

  //log out functionality -- make sure to sign out from firebase, reset local storage
  async function logOut() {
    await signOut(auth);
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userID");
    navigate("/");
  }

  return (
    <div>
      <div>
        <h1 className="dashboardHeader">
          Welcome to OMatch, User: {userEmail}!
        </h1>
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
      {/**OMAC schedule imported from the OMAC facilities website page */}
      <div id="calendarHolder">
        <embed
          id="calendar"
          src="https://25live.collegenet.com/pro/brown/embedded/calendar?comptype=calendar&compsubject=location&itemTypeId=4&queryId=548963&embeddedConfigToken=4C7BA58F-4540-4940-AF8D-25D1A23A3C00#!/home/calendar"
        ></embed>
      </div>
    </div>
  );
}
