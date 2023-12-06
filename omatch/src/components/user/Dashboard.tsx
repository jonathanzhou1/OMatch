import "../../styles/index.css";
import { Link } from "react-router-dom";

export default function Dashboard() {
  //get user email from local storage
  const userEmail = localStorage.getItem("userEmail");
  return (
    <div id="welcome">
      <h1>Welcome to OMatch, User: {userEmail}!</h1>
      <div id="buttonContainer">
        <Link to="/view-profile">
          <button
            className="button leftButton twoButtons"
            id="toViewProfileButton"
          >
            View Profile
          </button>
        </Link>
        <Link to="/match-team">
          <button
            className="button rightButton twoButtons"
            id="toMatchATeamButton"
          >
            Match a Team
          </button>
        </Link>
      </div>
    </div>
  );
}
