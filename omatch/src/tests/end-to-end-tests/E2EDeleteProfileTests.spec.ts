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

test("E2E, integration: create account + delete profile", async ({ page }) => {
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
  // Click "Delete Profile" button
  const deleteProfileButton = page.getByRole("button", {
    name: "Delete Profile",
  });
  await expect(deleteProfileButton).toBeVisible();
  await deleteProfileButton.click();

  // Confirm Intent to Delete
  const confirmButton = page.getByRole("button", { name: "Yes" });
  await expect(confirmButton).toBeVisible();
  await confirmButton.click();

  //Fill in password
  const password = page.getByLabel("passwordInput");
  const submitPasswordButton = page.getByRole("button", {
    name: "Submit Password",
  });

  await expect(password).toBeVisible();
  await expect(submitPasswordButton).toBeVisible();

  await password.fill("1234567");
  await submitPasswordButton.click();

  // Confirm deletion by page navigating back to home page
  await expect(page).toHaveURL("http://localhost:5173/");

  //try to log in and see error message
  await page.goto("http://localhost:5173/signin");
  // Enter user email and password + sign in
  const emailInput = page.getByLabel("emailInput");
  const passwordInput = page.getByLabel("passwordInput");
  const signInButton = page.getByRole("button", { name: "Sign In" });

  await expect(emailInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await expect(signInButton).toBeVisible();

  await emailInput.fill("giannis@gmail.com");
  await passwordInput.fill("1234567");
  await signInButton.click();

  //see error message
  await expect(
    page.getByText("Invalid login. Please try again!", { exact: true })
  ).toBeVisible();
});
