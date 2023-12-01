import "../../styles/index.css";

export default function WelcomeBox() {
  function CreateProfile() {
    {
      <h1>Change to Create Profile link here</h1>;
    }
  }
  function LogIn() {
    {
      <h1>Go to Log In Page Here</h1>;
    }
  }
  return (
    <div id="welcome">
      <h1>Welcome to OMatch!</h1>
      <h2>[WELCOME MESSAGE + INSTRUCTIONS]</h2>
      <div id="buttonContainer">
        <button
          id="leftButton"
          className="button"
          onClick={() => CreateProfile()}
        >
          Create Account
        </button>
        <button id="rightButton" className="button" onClick={() => LogIn()}>
          Log In
        </button>
      </div>
    </div>
  );
}
