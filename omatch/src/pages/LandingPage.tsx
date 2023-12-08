import { useState } from "react";
import WelcomeBox from "../components/landing/WelcomeBox";
import { auth } from "../firebase-config";
import { useNavigate } from "react-router";
import { onAuthStateChanged } from "firebase/auth";

export default function LandingPage() {
  const navigate = useNavigate();

  onAuthStateChanged(auth, (user) => {
    if (user !== null) {
      navigate("/dashboard");
    }
  });

  return (
    <div>
      <WelcomeBox />
    </div>
  );
}
