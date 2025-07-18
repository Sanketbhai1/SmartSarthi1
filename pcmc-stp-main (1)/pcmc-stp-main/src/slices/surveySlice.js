import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  surveyData: [],
  loading: false,
};

const surveySlice = createSlice({
  name: "survey",
  initialState: initialState,
  reducers: {
    setSurveyData(state, value) {
      state.surveyData = value.payload;
    },
    setLoading(state, value) {
      state.loading = value.payload;
    },
  },
});

export const { setSurveyData, setLoading } = surveySlice.actions;
export default surveySlice.reducer;