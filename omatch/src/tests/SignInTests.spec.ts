import { test, expect, Page } from "@playwright/test";

/**
 * Helper function for Playwright tests to sign in as a test user
 * @param page Playwright page
 * @param userEmail
 * @param userPassword
 */
export async function signInTestHelper(
  page: Page,
  userEmail: string,
  userPassword: string
) {
  // Get to the "Sign In" page from landing page
  const toSignInButton = page.getByRole("button", { name: "Sign In" });
  await expect(toSignInButton).toBeVisible();
  await toSignInButton.click();

  // Ensure that we have navigated to the correct page
  await page.waitForURL("http://localhost:5173/signin");
  await expect(page).toHaveURL("http://localhost:5173/signin");

  // Enter user email and password + sign in
  const emailInput = page.getByLabel("emailInput");
  const passwordInput = page.getByLabel("passwordInput");
  const signInButton = page.getByRole("button", { name: "Sign In" });

  await expect(emailInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await expect(signInButton).toBeVisible();

  await emailInput.fill(userEmail);
  await passwordInput.fill(userPassword);
  await signInButton.click();
}

async function signOutTestHelper(page: Page) {
  const signOutButton = page.getByRole("button", { name: "Sign Out" });
  await expect(signOutButton).toBeVisible();
  await signOutButton.click();
}

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

test("sign into an existing user account", async ({ page }) => {
  const email = "test@gmail.com";
  const password = "1234567";
  await signInTestHelper(page, email, password);

  // Sign in successful
  await expect(page).toHaveURL("http://localhost:5173/dashboard");
  await expect(page.getByText(email)).toBeVisible();

  await signOutTestHelper(page);
});

test("sign out successfully", async ({ page }) => {
  await signInTestHelper(page, "test@gmail.com", "1234567");
  await signOutTestHelper(page);

  // Test for successful sign out
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("You are not logged in.", { exact: true })
  ).toBeVisible();
});
