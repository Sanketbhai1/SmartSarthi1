import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  zoneData: [],
};

const zoneSlice = createSlice({
  name: "zone",
  initialState: initialState,
  reducers: {
    setZoneData(state, value) {
      state.zoneData = value.payload;
    },
    setLoading(state, value) {
      state.loading = value.payload;
    },
  },
});

export const { setZoneData, setLoading } = zoneSlice.actions;
export default zoneSlice.reducer;