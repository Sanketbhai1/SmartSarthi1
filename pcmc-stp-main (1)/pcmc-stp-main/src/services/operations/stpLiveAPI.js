import toast from "react-hot-toast";
import { apiConnector } from "../apiconnector";
import { stpRecordsEndpoints } from "../apis";
import { setLoading, setStpData } from "../../slices/stpSlice";

const { GET_STP_RECORDS_API, ADD_NEW_STP_API, UPDATE_STP_API, DELETE_STP_API } = stpRecordsEndpoints;


export function fetchStp () {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");

    try {
      // If using session, you don't need a token from localStorage
      // The session cookie will be automatically handled by the browser.
      const response = await apiConnector("GET", GET_STP_RECORDS_API, null, {}, {
        withCredentials: true,
      });

      console.log("STP response:", response);
      dispatch(setStpData(response.data));
    } catch (error) {
      console.error("GET_STP_RECORDS API ERROR............", error);
    } finally {
      toast.dismiss(toastId);
    }
  };
};

export function addStp(stpData, navigate) {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");
    dispatch(setLoading(true));
    try {

      console.log("Sending payload:", stpData);

      const response = await apiConnector("POST", ADD_NEW_STP_API, stpData);

      console.log("STP API RESPONSE............", response);

      toast.success("STP Data Added Successfully");
      dispatch(fetchStp());

      // navigate("/setting/manage-zone");
    } catch (error) {
      console.log("STP API ERROR............", error);
      toast.error("STP Addition Failed");
    } finally {
      dispatch(setLoading(false));
      toast.dismiss(toastId);
    }
  };
}


export function updateStp(updatedData, stpId) {
  return async (dispatch) => {
    const toastId = toast.loading("Updating STP...");

    try {
      const headers = {
        "Content-Type": "application/json",
        // Authorization: "Bearer YOUR_TOKEN", // If needed
      };

      await apiConnector(
        "PUT",
        `${UPDATE_STP_API}/${stpId}`,
        updatedData,
        headers
      );

      toast.success("STP updated successfully");
      dispatch(fetchStp()); // Refresh the list
    } catch (error) {
      console.error("Update failed:", error);
      toast.error("Failed to update STP");
    } finally {
      toast.dismiss(toastId);
    }
  };
}


export function deleteStp(zoneId) {
  return async (dispatch) => {
    const toastId = toast.loading("Deleting...");

    const headers = {
      Authorization: "Bearer YOUR_TOKEN",  // Replace with your actual token optional for future
    };

    try {
      await apiConnector("DELETE", `${DELETE_STP_API}/${zoneId}`, null, headers);
      toast.success("STP deleted successfully");
      dispatch(fetchStp());
    } catch (error) {
      console.error("Failed to delete STP:", error);
      toast.error(error?.response?.data?.message || "Failed to delete STP");
    } finally {
      toast.dismiss(toastId);
    }
  };
}
