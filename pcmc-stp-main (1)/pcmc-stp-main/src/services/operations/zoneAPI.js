import toast from "react-hot-toast";
import { apiConnector } from "../apiconnector";
import { zoneEndpoints } from "../apis";
import { setLoading, setZoneData } from "../../slices/zoneSlice";

const { GET_ZONES_API, ADD_ZONE_API, DELETE_ZONE_API, UPDATE_ZONE_API } =
  zoneEndpoints;

export const fetchZones = () => {
  return async (dispatch) => {
    const toastId = toast.loading("Loading...");

    try {
      // If using session, you don't need a token from localStorage
      // The session cookie will be automatically handled by the browser.
      const response = await apiConnector(
        "GET",
        GET_ZONES_API,
        null,
        {},
        {
          withCredentials: true,
        }
      );

      console.log("Zones response:", response);
      dispatch(setZoneData(response.data));
    } catch (error) {
      console.error("GET_ZONES API ERROR............", error);
    } finally {
      toast.dismiss(toastId);
    }
  };
};

export function addZone(data, navigate) {
  return async (dispatch) => {
    const toastId = toast.loading("Adding Zone...");
    dispatch(setLoading(true));
    try {
      const response = await apiConnector("POST", ADD_ZONE_API, data);

      toast.success("Zone Added Successfully");
      dispatch(fetchZones()); // ⬅️ Refresh the zone list here
      // navigate("/setting/manage-zone");
    } catch (error) {
      console.error("ADD_ZONE API ERROR:", error);

      // Check for 409 Conflict or custom message from backend
      if (
        error.response &&
        (error.response.status === 409 ||
         error.response.data?.message?.toLowerCase().includes("already exists"))
      ) {
        toast.error("Zone already exists");
      } else {
        toast.error("Failed to Add Zone");
      }
    } finally {
      dispatch(setLoading(false));
      toast.dismiss(toastId);
    }
  };
}

export function deleteZone(zoneId) {
  return async (dispatch) => {
    const toastId = toast.loading("Deleting...");

    const headers = {
      Authorization: "Bearer YOUR_TOKEN", // Replace with your actual token optional for future
    };

    try {
      await apiConnector("DELETE", `${DELETE_ZONE_API}/${zoneId}`, null, headers);
      toast.success("Zone deleted successfully");
      dispatch(fetchZones());
    } catch (error) {
      console.error("Failed to delete zone:", error);
      toast.error(error?.response?.data?.message || "Failed to delete zone");
    } finally {
      toast.dismiss(toastId);
    }
  };
}

export function updateZone(zoneId, updatedData, navigate) {
  return async (dispatch) => {
    const toastId = toast.loading("Updating zone...");

    try {
      const headers = {
        "Content-Type": "application/json",
        // Authorization: "Bearer YOUR_TOKEN", // If needed
      };

      await apiConnector(
        "PUT",
        `${UPDATE_ZONE_API}/${zoneId}`,
        updatedData,
        headers
      );

      toast.success("Zone updated successfully");
      dispatch(fetchZones()); // Refresh the list
    } catch (error) {
      console.error("Update failed:", error);
      toast.error("Failed to update zone");
    } finally {
      toast.dismiss(toastId);
    }
  };
}
