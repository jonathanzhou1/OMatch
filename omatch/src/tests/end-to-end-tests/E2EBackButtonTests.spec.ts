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

test("E2E, integration: using back on view profile, edit profile, and matchmaking", async ({
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

  // Press Back
  let backButton = page.getByRole("button", { name: "Back" });
  await expect(backButton).toBeVisible();
  await backButton.click();
  await expect(page).toHaveURL("http://localhost:5173/dashboard");

  // Click "View Profile" button
  const viewProfileButton = page.getByRole("button", { name: "View Profile" });
  await expect(viewProfileButton).toBeVisible();
  await viewProfileButton.click();

  await expect(page).toHaveURL("http://localhost:5173/view-profile");

  // Click "Edit Profile" button
  const editProfileButton = page.getByRole("button", { name: "Edit Profile" });
  await expect(editProfileButton).toBeVisible();
  await editProfileButton.click();

  await expect(page).toHaveURL("http://localhost:5173/edit-profile");

  // Press Back
  backButton = page.getByRole("button", { name: "Back" });
  await expect(backButton).toBeVisible();
  await backButton.click();
  await expect(page).toHaveURL("http://localhost:5173/view-profile");

  // Press Back
  backButton = page.getByRole("button", { name: "Back" });
  await expect(backButton).toBeVisible();
  await backButton.click();
  await expect(page).toHaveURL("http://localhost:5173/dashboard");
});

test.afterEach(async ({ page }) => {
  await deleteAccountTestHelper(page);
});
