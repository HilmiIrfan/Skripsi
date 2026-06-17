/* eslint-disable no-unused-vars */

import { useSelector } from "react-redux";
import React, { useEffect, useState } from "react";
import { Layout } from "antd";
import Logo from "./Logo";
import SidebarMenu from "./Menu";

const { Sider } = Layout;

const LayoutSider = () => {
  const { sidebarCollapsed, sidebarLogo } = useSelector((state) => ({
    sidebarCollapsed: state.app.sidebarCollapsed,
    sidebarLogo: state.settings.sidebarLogo,
  }));

  const [isFullscreen, setIsFullscreen] = useState(false);

  useEffect(() => {
    const handleFullscreenChange = () => {
      setIsFullscreen(
        !!(
          document.fullscreenElement ||
          document.webkitFullscreenElement ||
          document.mozFullScreenElement ||
          document.msFullscreenElement
        )
      );
    };
    document.addEventListener("fullscreenchange", handleFullscreenChange);
    document.addEventListener("webkitfullscreenchange", handleFullscreenChange);
    document.addEventListener("mozfullscreenchange", handleFullscreenChange);
    document.addEventListener("MSFullscreenChange", handleFullscreenChange);

    return () => {
      document.removeEventListener("fullscreenchange", handleFullscreenChange);
      document.removeEventListener(
        "webkitfullscreenchange",
        handleFullscreenChange
      );
      document.removeEventListener(
        "mozfullscreenchange",
        handleFullscreenChange
      );
      document.removeEventListener(
        "MSFullscreenChange",
        handleFullscreenChange
      );
    };
  }, []);

  if (isFullscreen) return null;

  return (
    <Sider
      collapsible
      collapsed={sidebarCollapsed}
      trigger={null}
      style={{
        position: "fixed",
        left: 0,
        top: 0,
        bottom: 0,
        zIndex: 10,
        height: "100vh",
      }}
    >
      {sidebarLogo && <Logo />}
      <SidebarMenu />
    </Sider>
  );
};

export default LayoutSider;
