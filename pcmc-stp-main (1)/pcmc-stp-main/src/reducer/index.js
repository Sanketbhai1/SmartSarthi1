import {combineReducers} from "@reduxjs/toolkit";
import authReducer from "../slices/authSlice";
import zoneReducer from "../slices/zoneSlice";
import stpReducer from "../slices/stpSlice";
import userReducer from "../slices/userSlice";
import surveyReducer from "../slices/surveySlice";

const rootReducer  = combineReducers({
    auth: authReducer,
    zone: zoneReducer,
    stp: stpReducer,
    user: userReducer,
    survey: surveyReducer,
})

export default rootReducer