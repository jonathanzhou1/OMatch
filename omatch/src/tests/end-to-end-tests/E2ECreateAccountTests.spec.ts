import { test, expect } from "@playwright/test";
import { createAccountTestHelper } from "../helper-functions/Create-Account.spec";
import { signInTestHelper } from "../helper-functions/SignIn.spec";
import { signOutTestHelper } from "../helper-functions/SignOut.spec";
import { deleteAccountTestHelper } from "../helper-functions/Delete-Account.spec";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

test("create a new account + confirm by signing in again", async ({ page }) => {
  const email = "newaccount@gmail.com";
  const password = "1234567";
  const position = "POINT_GUARD";

  // Create an account
  await createAccountTestHelper(page, email, password, "", "", position);

  // Sign out
  await signOutTestHelper(page);

  // Log in to confirm that the account actually exists
  await signInTestHelper(page, email, password);
  await expect(page.getByText(email)).toBeVisible();
});

test("E2E, integration: create a new account w/ no name + confirm that it is successful", async ({
  page,
}) => {
  const email = "newaccount@gmail.com";
  const password = "1234567";
  const position = "POINT_GUARD";

  // Create an account
  await createAccountTestHelper(page, email, password, "", "", position);

  // Confirm profile in "View Profile"
  const viewProfileButton = page.getByRole("button", { name: "View Profile" });
  await viewProfileButton.click();
  await expect(page).toHaveURL("http://localhost:5173/view-profile");

  await expect(page.getByText("Name: ")).toBeVisible();
  await expect(page.getByText("Position: POINT_GUARD")).toBeVisible();
  await expect(page.getByText("Wins: 0")).toBeVisible();
  await expect(page.getByText("Losses: 0")).toBeVisible();
});

test("E2E, integration: create a new account w/ name & position + confirm profile", async ({
  page,
}) => {
  const email = "newplayer@gmail.com";
  const password = "1234567";
  const firstName = "Tingus";
  const lastName = "Pingus";
  const position = "CENTER";

  // Create an account
  await createAccountTestHelper(
    page,
    email,
    password,
    firstName,
    lastName,
    position
  );

  // View profile
  const viewProfileButton = page.getByRole("button", { name: "View Profile" });
  await expect(viewProfileButton).toBeVisible();
  await viewProfileButton.click();

  // Check that the correct profile shows
  await expect(page).toHaveURL("http://localhost:5173/view-profile");
  await expect(page.getByText(`Name: ${firstName} ${lastName}`)).toBeVisible();
  await expect(page.getByText(`Position: ${position}`)).toBeVisible();
  await expect(page.getByText("Wins: 0")).toBeVisible();
  await expect(page.getByText("Losses: 0")).toBeVisible();
});

test.afterEach(async ({ page }) => {
  await deleteAccountTestHelper(page);
});
