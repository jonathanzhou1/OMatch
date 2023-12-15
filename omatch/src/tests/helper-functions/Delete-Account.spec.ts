import { expect, Page } from "@playwright/test";

/**
 * This test helper deletes the account.
 */
export async function deleteAccountTestHelper(page: Page) {
  //go to profile
  await page.goto("http://localhost:5173/view-profile");

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

  //Further extensive tests to see if delete account works in E2RDeleteProfileTests
}
