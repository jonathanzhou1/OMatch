import { test, expect, Page } from "@playwright/test";
import { createAccountTestHelper } from "../helper-functions/Create-Account.spec";

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

test("E2E, integration: create account + edit profile", async ({ page }) => {
  // Create an account
  await createAccountTestHelper(
    page,
    "giannis@gmail.com",
    "1234567",
    "Giannis",
    "Antetokounmpo",
    "CENTER"
  );

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
  // Edit profile
  const firstNameInput = page.getByLabel("firstNameInput");
  const lastNameInput = page.getByLabel("lastNameInput");
  const position = page.getByLabel("position-select");
  const editAccountButton = page.getByRole("button", { name: "Edit Account" });

  await expect(firstNameInput).toBeVisible();
  await expect(lastNameInput).toBeVisible();
  await expect(position).toBeVisible();
  await expect(editAccountButton).toBeVisible();

  await firstNameInput.fill("Greek");
  await lastNameInput.fill("Freak");
  await position.selectOption("POWER_FORWARD");

  await editAccountButton.click();

  // Confirm edits in "View Profile"
  await expect(page).toHaveURL("http://localhost:5173/view-profile");
  await expect(page.getByText("Name: Greek Freak")).toBeVisible();
  await expect(page.getByText("Position: POWER_FORWARD")).toBeVisible();

  // Delete profile
  const deleteProfileButton = page.getByRole("button", {
    name: "Delete Profile",
  });
  await expect(deleteProfileButton).toBeVisible();
  await deleteProfileButton.click();

  const yesButton = page.getByRole("button", { name: "Yes" });
  await yesButton.click();

  const passwordVerification = page.getByLabel("passwordInput");
  const submitPasswordButton = page.getByRole("button", {
    name: "Submit Password",
  });
  await passwordVerification.fill("1234567");
  await submitPasswordButton.click();
});
