/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import { useState, useEffect, useCallback } from "react";
import { Menu } from "antd";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { Scrollbars } from "react-custom-scrollbars-2";
import { useSelector, useDispatch } from "react-redux";
import { addTag } from "@/store/actions";
import { getMenuItemInMenuListByProperty } from "@/utils";
import menuList from "@/config/menuConfig";
import "./index.less";

const SidebarMenu = () => {
  const location = useLocation();
  const dispatch = useDispatch();
  const role = useSelector((state) => state.user.role);
  const navigate = useNavigate();

  const [menuItems, setMenuItems] = useState([]);
  const [openKeys, setOpenKeys] = useState([]);

  // Filter menu berdasarkan peran pengguna
  const filterMenuItem = useCallback(
    (item) => {
      if (!item.roles || item.roles.includes(role) || role === "admin") {
        return true;
      }
      return item.children?.some((child) => child.roles?.includes(role));
    },
    [role]
  );

  // Membuat menu berdasarkan menuList
  const generateMenuItems = useCallback(
    (menuList) => {
      return menuList.filter(filterMenuItem).map((item) => ({
        key: item.path,
        label: (
          <Link
            to={item.path}
            style={{ textDecoration: "none", color: "inherit" }}
          >
            {item.title}
          </Link>
        ),
        icon: item.icon ? <item.icon /> : null,
        children: item.children ? generateMenuItems(item.children) : undefined,
      }));
    },
    [filterMenuItem]
  );

  // Handle seleksi menu
  const handleMenuSelect = ({ key }) => {
    const menuItem = getMenuItemInMenuListByProperty(menuList, "path", key);
    if (menuItem) {
      dispatch(addTag(menuItem));
    }
  };

  // Inisialisasi menu items
  useEffect(() => {
    setMenuItems(generateMenuItems(menuList));
  }, [generateMenuItems]);

  // Menangani state menu yang terbuka
  useEffect(() => {
    const path = location.pathname;
    const openPaths = menuList
      .filter((item) =>
        item.children?.some((child) => path.startsWith(child.path))
      )
      .map((item) => item.path);

    setOpenKeys(openPaths);
  }, [location.pathname]);

  const onOpenChange = (keys) => {
    setOpenKeys(keys);
  };

  return (
    <div className="sidebar-menu-container">
      <Scrollbars autoHide autoHideTimeout={1000} autoHideDuration={200}>
        <Menu
          mode="inline"
          theme="dark"
          onSelect={handleMenuSelect}
          selectedKeys={[location.pathname]}
          openKeys={openKeys}
          onOpenChange={onOpenChange}
          items={menuItems}
        />
      </Scrollbars>
    </div>
  );
};

export default SidebarMenu;
