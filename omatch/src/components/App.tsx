import { BrowserRouter, Routes, Route } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import SignInPage from "../pages/SignInPage";
import CreateAccountBox from "./signin/CreateAccountBox";
import Dashboard from "./user/Dashboard";
import ViewProfile from "./user/ViewProfile";
import MatchTeam from "./user/MatchTeam";
import Profile from "./signin/Profile";
import { useState } from "react";

function App() {
  const [profile, setProfile] = useState<Profile | null>(null);

  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route index element={<LandingPage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route
            path="/create-account"
            element={<CreateAccountBox setProfile={setProfile} />}
          />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route
            path="/view-profile"
            element={<ViewProfile profile={profile} />}
          />
          <Route path="/match-team" element={<MatchTeam />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
