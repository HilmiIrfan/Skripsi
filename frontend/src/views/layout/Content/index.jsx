/* eslint-disable react/prop-types */
import { Routes, Route, Navigate, useLocation } from "react-router-dom";
import { useRef } from "react";
import { connect } from "react-redux";
import { CSSTransition, TransitionGroup } from "react-transition-group";
import { Layout } from "antd";
import DocumentTitle from "react-document-title";
import { getMenuItemInMenuListByProperty } from "@/utils";
import routeList from "@/config/routeMap";
import menuList from "@/config/menuConfig";

const { Content } = Layout;

const getPageTitle = (menuList, pathname) => {
  const defaultTitle = "Bank Soal";
  const item = getMenuItemInMenuListByProperty(menuList, "path", pathname);
  return item ? item.title : defaultTitle;
};

const LayoutContent = ({ role, sidebarCollapsed }) => {
  const location = useLocation();
  const { pathname } = location;

  const nodeRef = useRef(null);

  // Filter routes based on role
  const filteredRoutes = routeList.filter(
    (route) => role === "admin" || !route.roles || route.roles.includes(role)
  );

  return (
    <DocumentTitle title={getPageTitle(menuList, pathname)}>
      <Content
        style={{
          height: "calc(100% - 100px)",
          marginLeft: sidebarCollapsed ? "80px" : "200px",
        }}
      >
        <TransitionGroup>
          <CSSTransition
            key={pathname}
            timeout={500}
            classNames="fade"
            exit={false}
            nodeRef={nodeRef}
          >
            <Routes location={location}>
              <Route path="/" element={<Navigate replace to="/dashboard" />} />
              {filteredRoutes.map((route) => (
                <Route
                  key={route.path}
                  path={route.path}
                  element={<route.component />}
                />
              ))}
              <Route path="*" element={<Navigate replace to="/error/404" />} />
            </Routes>
          </CSSTransition>
        </TransitionGroup>
      </Content>
    </DocumentTitle>
  );
};

// Map state to props
const mapStateToProps = (state) => ({
  role: state.user.role,
  sidebarCollapsed: state.app.sidebarCollapsed,
});

export default connect(mapStateToProps)(LayoutContent);
