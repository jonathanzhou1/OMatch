import { test, expect } from "@playwright/test";
import { signInTestHelper } from "./SignInTests.spec";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

// Remember to delete the new account in Firebase before running this test!
test("integration: create a new account + confirm by signing in again", async ({
  page,
}) => {
  const email = "newaccount@gmail.com";
  const password = "newaccount";

  // Get to the "Create Account" page from landing page
  const toCreateAccountButton = page.getByRole("button", {
    name: "Create Account",
  });
  await expect(toCreateAccountButton).toBeVisible();
  await toCreateAccountButton.click();

  // Ensure that we have navigated to the correct page
  await page.waitForURL("http://localhost:5173/create-account");
  await expect(page).toHaveURL("http://localhost:5173/create-account");

  // Enter user email and password + sign in
  const emailInput = page.getByLabel("emailInput");
  const passwordInput = page.getByLabel("passwordInput");
  const createAccountButton = page.getByRole("button", {
    name: "Create Account",
  });

  await expect(emailInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await expect(createAccountButton).toBeVisible();

  await emailInput.fill(email);
  await passwordInput.fill(password);
  await createAccountButton.click();

  // Ensure that we have navigated to the "Dashboard" page
  await page.waitForURL("http://localhost:5173/dashboard");
  await expect(page).toHaveURL("http://localhost:5173/dashboard");
  await expect(page.getByText(email)).toBeVisible();

  // Sign out
  const signOutButton = page.getByRole("button", { name: "Sign Out" });
  await expect(signOutButton).toBeVisible();
  await signOutButton.click();

  // Log in to confirm that the account actually exists
  await signInTestHelper(page, email, password);
  await expect(page.getByText(email)).toBeVisible();
});
