import "../../styles/index.css";
import { useState } from "react";
import {
  createUserWithEmailAndPassword,
  AuthErrorCodes,
  AuthError,
  EmailAuthProvider,
  reauthenticateWithCredential,
  deleteUser,
} from "firebase/auth";
import { auth } from "../../firebase-config";
import { useNavigate } from "react-router-dom";

// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function CreateAccountBox() {
  //react state variables to store email and password
  const [registerEmail, setRegisterEmail] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");
  let uid = "";

  //react state variables to store error message + manage error status
  const [errorStatus, setErrorStatus] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  //react state variables for profile information
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [position, setPosition] = useState("");

  //navigate is for routing through various links
  const navigate = useNavigate();

  //register called on button click
  async function register() {
    //register email and password with firebase and create account
    await createUserWithEmailAndPassword(auth, registerEmail, registerPassword)
      .then(async (userCredential) => {
        //create user profile -- eventually replace when integrated with backend
        const user = userCredential.user;
        uid = user.uid;
      })
      .catch((error: AuthError) => {
        //upon error, check what type of error and return descriptive error message.
        const errorCode = error.code;
        switch (errorCode) {
          case AuthErrorCodes.EMAIL_EXISTS: {
            setErrorMessage("Account with this email already exists!");
            break;
          }
          case AuthErrorCodes.INVALID_EMAIL: {
            setErrorMessage("Invalid email. Please try again!");
            break;
          }
          case AuthErrorCodes.WEAK_PASSWORD: {
            setErrorMessage("Password should be at least 6 characters.");
            break;
          }
          default: {
            setErrorMessage(
              "Invalid account information inputted. Please try again!"
            );
          }
        }
        setErrorStatus(true);
        return;
      });

    //query backend
    const hostname = "http://localhost";
    const port = ":3232";
    const createProfileQuery =
      "/profile-add?name=" +
      firstName +
      "%20" +
      lastName +
      "&position=" +
      position +
      "&id=" +
      uid;
    await fetch(hostname + port + createProfileQuery)
      .then((response) => response.json())
      .then(async (responseObject) => {
        if (responseObject.result !== "success") {
          setErrorStatus(true);
          setErrorMessage(
            "Please make sure to fill out every field in your profile."
          );

          // delete the account just created upon to maintain consistency
          const curUser = auth.currentUser;
          const curCredential = EmailAuthProvider.credential(
            registerEmail,
            registerPassword
          );
          if (curUser != null) {
            //reauthenticate with email and password
            await reauthenticateWithCredential(curUser, curCredential)
              .then(async (_curUserCredential) => {
                //delete newly authenticated user
                await deleteUser(curUser)
                  .then(async () => {
                    //remove data from backend
                    const hostname = "http://localhost";
                    const port = ":3232";
                    const deleteProfileQuery =
                      "/profile-edit?action=delete&id=" + uid;
                    //even if backend fails to delete, still continue
                    //because account is still deleted by firebase
                    await fetch(hostname + port + deleteProfileQuery);

                    //remove local storage data
                    localStorage.removeItem("userEmail");
                    localStorage.removeItem("userID");
                  })
                  .catch(() => setErrorMessage("Failed to delete user."));
              })
              .catch((_error: AuthError) => {
                setErrorMessage("Account reauthentication failed.");
                setErrorStatus(true);
              });
          }
        } else {
          setErrorStatus(false);
          //localStorage acts as a KV-store locally on the browser
          //store user email and user id locally on browser for later access
          localStorage.setItem("userEmail", registerEmail);
          localStorage.setItem("userID", uid);
          //go to dashboard upon successful account creation
          return navigate("/dashboard");
        }
      });
  }

  return (
    <div id="createAccount">
      <h1 id="createAccountHeader">Create Account</h1>
      <h2 id="lessTopMargin">Email</h2>
      <input
        className="input"
        aria-label="emailInput"
        type="email"
        onChange={(event) => {
          setRegisterEmail(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            register();
          }
        }}
      ></input>
      <h2>Password</h2>
      {/* minimum length requirement for more secure passwords */}
      <input
        className="input"
        aria-label="passwordInput"
        type="password"
        onChange={(event) => {
          setRegisterPassword(event.target.value);
        }}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            event.preventDefault();
            register();
          }
        }}
      ></input>
      <h2>First Name</h2>
      <input
        className="input"
        aria-label="firstNameInput"
        type="text"
        onChange={(event) => {
          setFirstName(event.target.value);
        }}
      />
      <h2>Last Name</h2>
      <input
        className="input"
        aria-label="lastNameInput"
        type="text"
        onChange={(event) => {
          setLastName(event.target.value);
        }}
      />
      <h2>Position</h2>
      <div id="position-select">
        <select
          aria-label="position-select"
          name="position"
          onChange={(event) => {
            setPosition(event.target.value);
          }}
        >
          <option value={""}>Choose a position</option>
          <option value={"POINT_GUARD"}>Point Guard</option>
          <option value={"SHOOTING_GUARD"}>Shooting Guard</option>
          <option value={"SMALL_FORWARD"}>Small Forward</option>
          <option value={"POWER_FORWARD"}>Power Forward</option>
          <option value={"CENTER"}>Center</option>
        </select>
      </div>
      <div id="buttonContainer">
        <button className="button singleButton" onClick={register}>
          Create account
        </button>
        {errorStatus && <p className="redText">{errorMessage}</p>}
      </div>
    </div>
  );
}
