import toast from "react-hot-toast";
import { apiConnector } from "../apiconnector";
import { userEndpoints } from "../apis";
import { setLoading, setUserData } from "../../slices/userSlice";

const { GET_USERS_API, ADD_USER_API, UPDATE_USER_API, DELETE_USER_API } = userEndpoints;


export function addUser(userData) {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");
    dispatch(setLoading(true));
    try {

      console.log("Sending payload:", userData);

      const response = await apiConnector("POST", ADD_USER_API, userData); // Don't include withCredentials here

      console.log("USER API RESPONSE............", response);

      toast.success("USER Data Added Successfully");
      dispatch(fetchUser());

    } catch (error) {
      console.log("USER API ERROR............", error);
      toast.error("USER Addition Failed");
    } finally {
      dispatch(setLoading(false));
      toast.dismiss(toastId);
    }
  };
}

export function deleteUser(zoneId) {
  return async (dispatch) => {
    const toastId = toast.loading("Deleting...");

    const headers = {
      Authorization: "Bearer YOUR_TOKEN", // Replace with your actual token optional for future
    };

    try {
      await apiConnector("DELETE", `${DELETE_USER_API}/${zoneId}`, null, headers);
      toast.success("USER deleted successfully");
      dispatch(fetchUser());
    } catch (error) {
      console.error("Failed to delete USER:", error);
      toast.error(error?.response?.data?.message || "Failed to delete USER");
    } finally {
      toast.dismiss(toastId);
    }
  };
}

export function updateUser(userId, userData) {
  return async (dispatch) => {
    const toastId = toast.loading("Updating STP...");

    try {
      const headers = {
        "Content-Type": "application/json",
        // Authorization: "Bearer YOUR_TOKEN", // If needed
      };

      await apiConnector(
        "PUT", // Or "POST" if your backend uses POST for updates
        `${UPDATE_USER_API}/${userId}`,
        userData,
        headers
      );

      toast.success("STP updated successfully");
      dispatch(fetchUser()); // Refresh the list
    } catch (error) {
      console.error("Update failed:", error);
      toast.error("Failed to update STP");
    } finally {
      toast.dismiss(toastId);
    }
  };
}

export function fetchUser() {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");

    try {
      // If using session, you don't need a token from localStorage
      // The session cookie will be automatically handled by the browser.
      const response = await apiConnector(
        "GET",
        GET_USERS_API,
        null,
        {},
        {
          withCredentials: true,
        }
      );

      console.log("USER response:", response);
      dispatch(setUserData(response.data));
    } catch (error) {
      console.error("GET_USER API ERROR............", error);
    } finally {
      toast.dismiss(toastId);
    }
  };
}