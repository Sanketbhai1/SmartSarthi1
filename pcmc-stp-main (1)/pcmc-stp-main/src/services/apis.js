const BASE_URL = import.meta.env.VITE_BASE_URL;

export const endpoints = {
  LOGIN_API: `${BASE_URL}/auth/login`,
  LOGOUT_API: `${BASE_URL}/auth/logout`
};

export const zoneEndpoints = {
  GET_ZONES_API: `${BASE_URL}/zone/list`,
  ADD_ZONE_API: `${BASE_URL}/zone/add`,
  DELETE_ZONE_API: `${BASE_URL}/zone/disable`,
  UPDATE_ZONE_API: `${BASE_URL}/zone/updateZone`,
};

export const stpRecordsEndpoints = {
  GET_STP_RECORDS_API: `${BASE_URL}/privateStp/list`,
  ADD_NEW_STP_API: `${BASE_URL}/privateStp/add`,
  DELETE_STP_API: `${BASE_URL}/privateStp/disable`,
  UPDATE_STP_API: `${BASE_URL}/privateStp/updatePrivateStp`,
}

export const userEndpoints = {
  GET_USERS_API: `${BASE_URL}/user/getAll`,
  ADD_USER_API: `${BASE_URL}/user/add`,
  DELETE_USER_API: `${BASE_URL}/user/disable`,
  UPDATE_USER_API: `${BASE_URL}/user/update`,
}

export const surveyEndpoints = {
  GET_SURVEY_API: `${BASE_URL}/stpform/allSTPForm/list`,
  GET_SURVEY_FORM_API: `${BASE_URL}/stpform/downloadStpForm`,
  GET_STP_PHOTO_API: `${BASE_URL}/stpform/download/stpPhoto`,
}