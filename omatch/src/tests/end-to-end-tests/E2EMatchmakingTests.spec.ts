import { test, expect } from "@playwright/test";
import { createAccountTestHelper } from "../helper-functions/Create-Account.spec";
import { deleteAccountTestHelper } from "../helper-functions/Delete-Account.spec";
import { signOutTestHelper } from "../helper-functions/SignOut.spec";
import { signInTestHelper } from "../helper-functions/SignIn.spec";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

test("E2E, integration: create account + view empty matches", async ({
  page,
}) => {
  // Create an account
  await createAccountTestHelper(
    page,
    "giannis@gmail.com",
    "1234567",
    "Giannis",
    "Antetokounmpo",
    "CENTER"
  );
  // Click "Match Team" button
  const matchTeamButton = page.getByRole("button", { name: "Match a Team" });
  await expect(matchTeamButton).toBeVisible();
  await matchTeamButton.click();

  await expect(page).toHaveURL("http://localhost:5173/match-team");
  // View Matches
  const viewMatchesButton = page.getByRole("button", { name: "View Matches" });

  await expect(viewMatchesButton).toBeVisible();

  await viewMatchesButton.click();

  await expect(page.getByText("No live matches!")).toBeVisible();

  // delete account
  await deleteAccountTestHelper(page);
});

test("E2E, integration: create account + match team + view matches + end match", async ({
  page,
}) => {
  // Create an account
  await createAccountTestHelper(
    page,
    "giannis@gmail.com",
    "1234567",
    "Giannis",
    "Antetokounmpo",
    "CENTER"
  );
  // Click "Match Team" button
  const matchTeamButton = page.getByRole("button", { name: "Match a Team" });
  await expect(matchTeamButton).toBeVisible();
  await matchTeamButton.click();

  await expect(page).toHaveURL("http://localhost:5173/match-team");

  // Match Team
  const matchButton = page.getByRole("button", { name: "Match Team" });
  await expect(matchButton).toBeVisible();
  await matchButton.click();
  await expect(page.getByText("Success: Player added to queue")).toBeVisible();

  // View Matches
  const viewMatchesButton = page.getByRole("button", { name: "View Matches" });
  await expect(viewMatchesButton).toBeVisible();
  await viewMatchesButton.click();
  await expect(page.getByText("Josh Joshington")).toHaveCount(9);
  await expect(page.getByText("Giannis Antetokounmpo")).toHaveCount(1);

  //End Match
  const endMatchButton = page.getByRole("button", { name: "End Match" });
  await expect(endMatchButton).toBeVisible();
  await endMatchButton.click();

  const confirmButton = page.getByRole("button", { name: "Submit Result" });
  const winRadio = page.getByLabel("win");
  const loseRadio = page.getByLabel("lose");
  const tieRadio = page.getByLabel("tie");
  await expect(winRadio).toBeVisible();
  await expect(loseRadio).toBeVisible();
  await expect(tieRadio).toBeVisible();
  await expect(confirmButton).toBeVisible();

  await winRadio.click();
  await confirmButton.click();

  await expect(
    page.getByText("Success: Game result has been updated to your profile")
  ).toBeVisible();

  // delete account
  await deleteAccountTestHelper(page);
});

test("E2E, integration: creating a full match from scratch", async ({
  page,
}) => {
  test.setTimeout(120000);
  // create 11 different accounts to add to the queue
  for (let i = 0; i < 11; i++) {
    // declare the new account information
    const email = `b${i}@gmail.com`;
    const password = "1234567";
    const firstName = "Baller";
    const lastName = `${i}`;
    const positions = [
      "POINT_GUARD",
      "SHOOTING_GUARD",
      "SMALL_FORWARD",
      "POWER_FORWARD",
      "CENTER",
    ];
    const position = positions[i % 5];

    // create the account
    await createAccountTestHelper(
      page,
      email,
      password,
      firstName,
      lastName,
      position
    );

    // navigate to the "Match a Team" page
    const matchButton = page.getByRole("button", { name: "Match a Team" });
    await matchButton.click();

    // match a team
    const matchTeamButton = page.getByRole("button", { name: "Match Team" });
    await matchTeamButton.click();
    await expect(
      page.getByText("Success: Player added to queue")
    ).toBeVisible();

    // if it's the last account, view matches; otherwise, sign out
    if (i === 10) {
      // view matches
      const viewMatchesButton = page.getByRole("button", {
        name: "View Matches",
      });
      await viewMatchesButton.click();

      // confirm existing matches
      await expect(page.getByText("Match 1")).toBeVisible();
      await expect(page.getByText("Josh Joshington")).toHaveCount(9, {
        timeout: 20000,
      });
      await expect(page.getByText("Baller 0")).toHaveCount(1);

      await expect(page.getByText("Match 2")).toBeVisible();
      for (let j = 0; j < 11; j++) {
        await expect(
          page.getByText(`Baller ${j}`, { exact: true })
        ).toHaveCount(1);
      }
    }

    // sign out
    await page.goto("http://localhost:5173/dashboard");
    await signOutTestHelper(page);
  }

  // delete each account
  for (let i = 0; i < 11; i++) {
    // sign in information
    const email = `b${i}@gmail.com`;
    const password = "1234567";

    // sign in
    await signInTestHelper(page, email, password);

    // go to "View Profile"
    const viewProfileButton = page.getByRole("button", {
      name: "View Profile",
    });
    await viewProfileButton.click();

    // delete account
    await deleteAccountTestHelper(page);
  }
});

// test.afterEach(async ({ page }) => {
//   await deleteAccountTestHelper(page);
// });
