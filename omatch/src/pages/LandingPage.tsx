import { useEffect } from "react";
import WelcomeBox from "../components/landing/WelcomeBox";
import { useNavigate } from "react-router";
import { useLocation } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();
  const location = useLocation();

  //if user is already logged in, automatically go to dashboard
  useEffect(() => {
    if (
      localStorage.getItem("userEmail") !== null &&
      localStorage.getItem("userID") !== null
    ) {
      console.log("id", localStorage.getItem("userID"));
      navigate("/dashboard");
    }
  }, [location, navigate]);

  return (
    <div>
      <WelcomeBox />
    </div>
  );
}
