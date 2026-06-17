/* eslint-disable no-unused-vars */
import React, { useEffect } from "react";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import Lottie from "lottie-react";
import animationData from "@/assets/lottie/loading-animation.json";
import "./Loading.css";

NProgress.configure({ showSpinner: false });

const Loading = () => {
  useEffect(() => {
    NProgress.start();
    return () => {
      NProgress.done();
    };
  }, []);

  return (
    <div className="loading-wrapper">
      <div className="lottie-container">
        <Lottie animationData={animationData} loop={true} />
        <p className="loading-text">Loading, please wait...</p>
      </div>
    </div>
  );
};

export default Loading;
