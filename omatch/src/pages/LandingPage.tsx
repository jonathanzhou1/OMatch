import { useEffect, useState } from "react";
import WelcomeBox from "../components/landing/WelcomeBox";
import { auth } from "../firebase-config";
import { useNavigate } from "react-router";
import { onAuthStateChanged } from "firebase/auth";
import { useLocation } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();
  const location = useLocation();

  // onAuthStateChanged(auth, (user) => {
  //   if (user !== null) {
  //     navigate("/dashboard");
  //   }
  // });

  useEffect(() => {
    if (localStorage.getItem("userEmail") !== null) {
      navigate("/dashboard");
    }
  }, [location, navigate])

  return (
    <div>
      <WelcomeBox />
    </div>
  );
}
