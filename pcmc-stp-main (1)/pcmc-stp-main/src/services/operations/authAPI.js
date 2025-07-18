import { toast } from "react-hot-toast";
import { setLoading, setRole, setToken, logoutUser } from "../../slices/authSlice";
import { apiConnector } from "../apiconnector";
import { endpoints } from "../apis";

const { LOGIN_API, LOGOUT_API } = endpoints;

// --- LOGIN FUNCTION ---
export function login(username, password, navigate) {
  return async (dispatch) => {
    const toastId = toast.loading("Logging in...");
    dispatch(setLoading(true));

    try {
      const response = await apiConnector("POST", LOGIN_API, { username, password });
      console.log("LOGIN RESPONSE >>>", response);

      const { token, roles } = response.data;

      // Save token in sessionStorage
      sessionStorage.setItem("token", token);

      dispatch(setRole(roles));
      dispatch(setToken(token));

      toast.success("Login Successful");
      navigate("/dashboard");
    } catch (error) {
      console.error("LOGIN ERROR >>>", error);
      toast.error("Login Failed");
    } finally {
      dispatch(setLoading(false));
      toast.dismiss(toastId);
    }
  };
}

// --- LOGOUT FUNCTION ---
export function logout(navigate) {
  return async (dispatch) => {
    const toastId = toast.loading("Logging out...");

    try {
      await apiConnector("POST", LOGOUT_API);

      // Clear sessionStorage
      sessionStorage.removeItem("token");

      dispatch(logoutUser());

      toast.success("Logged out successfully");
      navigate("/");
    } catch (error) {
      console.error("LOGOUT ERROR >>>", error);
      toast.error("Logout Failed");
    } finally {
      toast.dismiss(toastId);
    }
  };
}
