/* eslint-disable react/prop-types */
import React, { Suspense, useEffect } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { connect } from "react-redux";
import { getUserInfo } from "@/store/actions";
import Loading from "@/components/Loading";
import Layout from "@/views/layout";
import Login from "@/views/login";

// Import UjianView untuk route khusus tanpa layout
const UjianView = React.lazy(() => import("@/views/ujian-view"));

const Router = ({ token, role, getUserInfo }) => {
  useEffect(() => {
    if (token && !role) {
      getUserInfo(token);
    }
  }, [token, role, getUserInfo]);

  return (
    <BrowserRouter>
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/login" element={<Login />} />
          {/* Route khusus untuk ujian tanpa navbar/layout */}
          <Route
            path="/ujian-view/*"
            element={
              token ? (
                role ? (
                  <UjianView />
                ) : (
                  <Loading />
                )
              ) : (
                <Navigate to="/login" />
              )
            }
          />
          <Route
            path="/*"
            element={
              token ? role ? <Layout /> : <Loading /> : <Navigate to="/login" />
            }
          />
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
};

export default connect((state) => state.user, { getUserInfo })(Router);
