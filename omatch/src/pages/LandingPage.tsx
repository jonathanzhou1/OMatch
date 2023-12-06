import WelcomeBox from "../components/landing/WelcomeBox";
import { auth } from "../firebase-config";
import { useNavigate } from "react-router";

export default function LandingPage() {
  const navigate = useNavigate();
  if (auth.currentUser !== null) {
    navigate("/dashboard");
  }

  return (
    <div>
      <WelcomeBox />
    </div>
  );
}
