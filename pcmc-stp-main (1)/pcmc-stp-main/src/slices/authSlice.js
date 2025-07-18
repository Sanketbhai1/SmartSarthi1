import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  role: null,
  loading: false,
  token: sessionStorage.getItem("token") || null, 
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setRole(state, action) {
      state.role = action.payload;
    },
    setLoading(state, action) {
      state.loading = action.payload;
    },
    setToken(state, action) {
      state.token = action.payload;
    },
    logoutUser(state) {
      state.token = null;
      state.role = null;
    },
  },
});

export const { setRole, setLoading, setToken, logoutUser } = authSlice.actions;
export default authSlice.reducer;
