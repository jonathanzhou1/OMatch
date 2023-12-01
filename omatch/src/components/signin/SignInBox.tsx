// This page uses Google Firebase Authentication to allow users to create accounts and sign in

export default function SignInBox() {
  return (
    <div id="signIn">
      <h1>Sign In</h1>
      <h2>Email</h2>
      <input type="email"></input>
      <h2>Password</h2>
      {/* Consider adding minLength and pattern properties for more secure passwords */}
      <input type="password"></input> <button>Sign In</button>
    </div>
  );
}
