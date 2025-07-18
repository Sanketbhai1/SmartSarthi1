import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import SurveyTable from "../components/Tables/SurveyTable";
import { fetchSurveyData } from "../services/operations/surveyAPI";
import { setSurveyData } from "../slices/surveySlice";

const SurveyData = () => {
  const dispatch = useDispatch();

  const { surveyData } = useSelector((state) => state.survey);

  useEffect(() => {
    // Only fetch data if it doesn't exist in localStorage
    const storedSurveyData = localStorage.getItem("surveyData");
    if (!storedSurveyData) {
      dispatch(fetchSurveyData());  // Fetch data if not in localStorage
    } else {
      // If data is found in localStorage, directly set it in the Redux store
      dispatch(setSurveyData(JSON.parse(storedSurveyData)));
    }
  }, [dispatch]);

  return (
    <div className="relative min-h-screen bg-gray-50 p-6">
      <div>
        {/*  */}
        <SurveyTable data={surveyData} />
      </div>
    </div>
  );
};

export default SurveyData;
