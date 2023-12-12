import { test, expect } from "@playwright/test";
import { signInTestHelper } from "./SignInTests.spec";

test.beforeEach(async ({ page }) => {
  await page.goto("http://localhost:5173/");
});

// Remember to delete the new account in Firebase before running this test!
test("integration: create a new account w/ no profile + confirm by signing in again", async ({
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

  // Enter user email, password, name, and position + sign in
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

// Remember to delete the new account in Firebase before running this test!
test("integration: create a new account w/ profile + confirm profile", async ({
  page,
}) => {
  const email = "newplayer@gmail.com";
  const password = "newplayer";
  const firstName = "Tingus";
  const lastName = "Pingus";
  const position = "CENTER";

  // Get to the "Create Account" page from landing page
  const toCreateAccountButton = page.getByRole("button", {
    name: "Create Account",
  });
  await expect(toCreateAccountButton).toBeVisible();
  await toCreateAccountButton.click();

  // Ensure that we have navigated to the correct page
  await page.waitForURL("http://localhost:5173/create-account");
  await expect(page).toHaveURL("http://localhost:5173/create-account");

  // Enter user email, password, name, and position + sign in
  const emailInput = page.getByLabel("emailInput");
  const passwordInput = page.getByLabel("passwordInput");
  const createAccountButton = page.getByRole("button", {
    name: "Create Account",
  });
  const firstNameInput = page.getByLabel("firstNameInput");
  const lastNameInput = page.getByLabel("lastNameInput");
  const positionSelect = page.getByLabel("position-select");

  await expect(emailInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await expect(createAccountButton).toBeVisible();
  await expect(firstNameInput).toBeVisible();
  await expect(lastNameInput).toBeVisible();
  await expect(positionSelect).toBeVisible();

  await emailInput.fill(email);
  await passwordInput.fill(password);
  await firstNameInput.fill(firstName);
  await lastNameInput.fill(lastName);
  await positionSelect.selectOption({ value: position });
  await createAccountButton.click();

  // Ensure that we have navigated to the "Dashboard" page
  await page.waitForURL("http://localhost:5173/dashboard");
  await expect(page).toHaveURL("http://localhost:5173/dashboard");
  await expect(page.getByText(email)).toBeVisible();

  // View profile
  const viewProfileButton = page.getByRole("button", { name: "View Profile" });
  await expect(viewProfileButton).toBeVisible();
  await viewProfileButton.click();

  // Check that the correct profile shows
  await expect(page).toHaveURL("http://localhost:5173/view-profile");
  await expect(page.getByText(`Name: ${firstName} ${lastName}`)).toBeVisible();
  await expect(page.getByText(`Position: ${position}`)).toBeVisible();
});
