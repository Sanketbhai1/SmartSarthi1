import React from "react";
import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import Login from "./components/Auth/Login";
import PrivateRoute from "./components/Auth/PrivateRoute";
import Dashboard from "./pages/Dashboard";
import Navbar from "./components/Common/Navbar";
import Error from "./pages/Error";
import ManageZone from "./pages/ManageZone";
import StpConfiguration from "./pages/StpConfiguration";
import PrivateSTP from "./pages/PrivateStp";
import ManageUser from "./pages/ManageUser";
import SurveyData from "./pages/SurveyData";

const App = () => {
  const location = useLocation();
  const hideNavbar = location.pathname === "/login";
  // Add more paths if needed
  // const hideNavbar = ["/login", "/register"].includes(location.pathname);

  return (
    <div className="  p-0 m-0 box-border">
      {!hideNavbar && <Navbar />}
      <Routes>
        <Route path="/">
          <Route index element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login />} />

          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />

          <Route
            path="/private-stp"
            element={
              <PrivateRoute>
                <PrivateSTP />
              </PrivateRoute>
            }
          />
          <Route
            path="/setting/pvt-stp-config"
            element={
              <PrivateRoute>
                <StpConfiguration />
              </PrivateRoute>
            }
          />
          <Route
            path="/setting/manage-zone"
            element={
              <PrivateRoute>
                <ManageZone />
              </PrivateRoute>
            }
          />
          <Route
            path="/setting/manage-user"
            element={
              <PrivateRoute>
                <ManageUser />
              </PrivateRoute>
            }
          />
          <Route
            path="/survey"
            element={
              <PrivateRoute>
                <SurveyData />
              </PrivateRoute>
            }
          />

          <Route path="*" element={<Error />} />
        </Route>
      </Routes>
    </div>
  );
};

export default App;
