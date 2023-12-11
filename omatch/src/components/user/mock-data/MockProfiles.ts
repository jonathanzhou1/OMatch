//INTERFACE FOR CLARITY OF PROFILE
interface UserProfile {
  id: string;
  name: string;
  position: string;
}

//TEST ACCOUNT USER PROFILE
const joemungus_burger: UserProfile = {
  id: "3UwX76jzYJNSpdYlHC4rVbreP8n2",
  name: "Joemungus Burger",
  position: "POWER_FORWARD",
};

export const joemungus_burger_json: string = JSON.stringify(joemungus_burger);

//TEST 5 ACCOUNT USER PROFILE
const sussy_baka: UserProfile = {
  id: "Zg0FeoUOtxVIq1Q1NFdq8K3AbBP2",
  name: "Sussy Baka",
  position: "POINT_GUARD",
};

export const sussy_baka_json: string = JSON.stringify(sussy_baka);

// newplayer@gmail.com USER PROFILE
const tingus_pingus: UserProfile = {
  id: "Zg0FeoUOtxVIq1Q1NFdq8K3AbBP1",
  name: "Tingus Pingus",
  position: "CENTER",
};

export const tingus_pingus_json: string = JSON.stringify(tingus_pingus);

export default UserProfile;
