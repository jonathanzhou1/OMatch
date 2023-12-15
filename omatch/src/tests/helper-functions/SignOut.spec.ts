import { Page, expect } from "@playwright/test";

export async function signOutTestHelper(page: Page) {
  const signOutButton = page.getByRole("button", { name: "Sign Out" });
  await expect(signOutButton).toBeVisible();
  await signOutButton.click();
}
