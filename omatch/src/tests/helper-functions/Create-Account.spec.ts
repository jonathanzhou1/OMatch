import { expect, Page } from "@playwright/test";

/**
 * Starting from the landing page, this test helper creates a new account.
 * @param page Playwright page
 * @param email
 * @param password
 * @param firstName
 * @param lastName
 * @param position
 */
export async function createAccountTestHelper(
  page: Page,
  email: string,
  password: string,
  firstName: string,
  lastName: string,
  position: string
) {
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
}
