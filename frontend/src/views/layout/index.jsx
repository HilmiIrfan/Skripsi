/* eslint-disable react/prop-types */
import { Outlet } from "react-router-dom";
import { connect } from "react-redux";
import { Layout } from "antd";
import Content from "./Content";
import Header from "./Header";
import RightPanel from "./RightPanel";
import Sider from "./Sider";
import TagsView from "./TagsView";

const Main = ({ tagsView }) => {
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider />
      <Layout>
        <Header />
        {tagsView ? <TagsView /> : null}
        <Content>
          <Outlet />
        </Content>
        <RightPanel />
      </Layout>
    </Layout>
  );
};

export default connect((state) => state.settings)(Main);
