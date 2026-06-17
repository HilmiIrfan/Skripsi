import React from "react";
import { useLocation } from "react-router-dom";
import { Breadcrumb } from "antd";
import menuList from "@/config/menuConfig";
import "./index.less";

/**
 * Menentukan breadcrumb berdasarkan URL saat ini.
 */
const getPath = (menuList, pathname) => {
  let temppath = [];
  try {
    function getNodePath(node) {
      temppath.push({ title: node.title, path: node.path });
      if (node.path === pathname) {
        throw new Error("GOT IT!");
      }
      if (node.children && node.children.length > 0) {
        for (let i = 0; i < node.children.length; i++) {
          getNodePath(node.children[i]);
        }
        temppath.pop();
      } else {
        temppath.pop();
      }
    }
    for (let i = 0; i < menuList.length; i++) {
      getNodePath(menuList[i]);
    }
  } catch (e) {
    return temppath;
  }
};

const BreadCrumb = () => {
  const location = useLocation();
  const { pathname } = location;

  let path = getPath(menuList, pathname);
  const first = path && path[0];

  if (first && first.title.trim() !== "Beranda") {
    path = [{ title: "Beranda", path: "/dashboard" }, ...path];
  }

  return (
    <div className="Breadcrumb-container">
      <Breadcrumb
        items={(path || []).map((item) => ({
          title:
            item.title === "Beranda" ? (
              <a href={`#${item.path}`}>{item.title}</a>
            ) : (
              item.title
            ),
        }))}
      />
    </div>
  );
};

export default BreadCrumb;
