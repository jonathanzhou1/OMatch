import { test, expect, Page } from "@playwright/test";
import { createAccountTestHelper } from "../helper-functions/Create-Account.spec";
import { deleteAccountTestHelper } from "../helper-functions/Delete-Account.spec";

// /**
//  * Helper function for testing edit profile functionality. This function assumes that page starts on the Dashboard page.
//  * @param page Playwright page
//  * @param firstName
//  * @param lastName
//  * @param position
//  */
// async function editProfileTestHelper(
//   page: Page,
//   firstName: string,
//   lastName: string,
//   position: string
// ) {}

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

  await expect(
    page.getByText("Current Matches: No matches yet!")
  ).toBeVisible();

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
  await expect(
    page.getByText(
      "Current Matches: MATCH STATUS: ONGOING, TEAM 1 PLAYERS: Josh Joshington,Josh Joshington,Josh Joshington,Josh Joshington,Josh Joshington, TEAM 2 PLAYERS: Josh Joshington,Josh Joshington,Josh Joshington,Josh Joshington,Jonathan Zhou;"
    )
  ).toBeVisible();

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
  await deleteAccountTestHelper(page);
});
