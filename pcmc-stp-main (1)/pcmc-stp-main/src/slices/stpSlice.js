import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  stpData: [],
};

const stpSlice = createSlice({
  name: "stp",
  initialState: initialState,
  reducers: {
    setStpData(state, value) {
      state.stpData = value.payload;
    },
    setLoading(state, value) {
      state.loading = value.payload;
    },
  },
});

export const { setStpData, setLoading } = stpSlice.actions;
export default stpSlice.reducer;