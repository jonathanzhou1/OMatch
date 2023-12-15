import { test, expect } from "@playwright/test";
import {
  signInTestHelper,
  signOutTestHelper,
} from "../mock-tests/MockSignInTests.spec";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

test("E2E: sign into an existing user account", async ({ page }) => {
  const email = "test@gmail.com";
  const password = "1234567";

  // Get to the "Sign In" page from landing page
  const toSignInButton = page.getByRole("button", { name: "Sign In" });
  await expect(toSignInButton).toBeVisible();
  await toSignInButton.click();

  // Sign in
  await signInTestHelper(page, email, password);

  // Sign in successful
  await expect(page).toHaveURL("http://localhost:5173/dashboard");
  await expect(page.getByText(email)).toBeVisible();

  await signOutTestHelper(page);
});

test("E2E: sign out successfully", async ({ page }) => {
  await signInTestHelper(page, "test@gmail.com", "1234567");
  await signOutTestHelper(page);

  // Test for successful sign out
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("You are not logged in.", { exact: true })
  ).toBeVisible();
});
