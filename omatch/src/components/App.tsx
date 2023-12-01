import { BrowserRouter, Routes, Route } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import SignInPage from "../pages/SignInPage";
import CreateAccountBox from "./signin/CreateAccountBox";

function App() {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route index element={<LandingPage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/create-account" element={<CreateAccountBox />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
