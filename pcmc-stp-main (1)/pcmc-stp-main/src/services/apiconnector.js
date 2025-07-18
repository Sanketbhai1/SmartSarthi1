import axios from "axios";
import store from "../store"; 
import { logoutUser } from "../slices/authSlice";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
});

// --- REQUEST HANDLER ---
export const apiConnector = async (
  method,
  url,
  bodyData = null,
  headers = {},
  params = {}
) => {
  try {
    const response = await axiosInstance({
      method,
      url,
      data: bodyData,
      headers,
      params,
    });

    return response; // ✅ API call successful
  } catch (error) {
    console.error("API CONNECTOR ERROR >>>", error);

    // If server responds with 401 (Unauthorized), logout the user
    if (error.response && error.response.status === 401) {
      console.warn("Unauthorized! Logging out...");
      store.dispatch(logoutUser());
      sessionStorage.clear();
      localStorage.clear();
      window.location.href = "/login"; 
    }

    throw error; 
  }
};
