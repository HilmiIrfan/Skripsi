/* eslint-disable react/prop-types */
import { useState, useRef, useEffect, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Scrollbars } from "react-custom-scrollbars-2";
import { Tag } from "antd";
import { useSelector, useDispatch } from "react-redux";
import { deleteTag, emptyTaglist, closeOtherTags } from "@/store/actions";
import "../index.less";

const TagList = () => {
  const tagListContainer = useRef(null);
  const contextMenuContainer = useRef(null);
  const [menuState, setMenuState] = useState({
    left: 0,
    top: 0,
    visible: false,
    currentTag: null,
  });

  const taglist = useSelector((state) => state.tagsView.taglist);
  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  const currentPath = location.pathname;

  const handleClose = useCallback(
    (tag) => {
      const path = tag.path;
      const length = taglist.length;

      // If closing the current tag, navigate to the last tag
      if (path === currentPath) {
        navigate(taglist[length - 1]?.path || "/dashboard");
      }

      // If closing the last tag, navigate to the previous tag
      if (
        path === taglist[length - 1]?.path &&
        currentPath === taglist[length - 1]?.path
      ) {
        if (length - 2 >= 0) {
          navigate(taglist[length - 2]?.path || "/dashboard");
        } else if (length === 2) {
          navigate(taglist[0]?.path || "/dashboard");
        }
      }

      dispatch(deleteTag(tag));
    },
    [taglist, currentPath, dispatch, navigate]
  );

  const handleClick = (path) => {
    navigate(path);
  };

  const openContextMenu = (tag, event) => {
    event.preventDefault();
    const menuMinWidth = 105;
    const clickX = event.clientX;
    const clickY = event.clientY;
    const clientWidth = tagListContainer.current.clientWidth;
    const maxLeft = clientWidth - menuMinWidth;

    setMenuState({
      left: clickX > maxLeft ? clickX - menuMinWidth + 15 : clickX,
      top: clickY,
      visible: true,
      currentTag: tag,
    });
  };

  const closeContextMenu = () => {
    setMenuState((prevState) => ({ ...prevState, visible: false }));
  };

  const handleClickOutside = (event) => {
    if (
      contextMenuContainer.current &&
      !contextMenuContainer.current.contains(event.target) &&
      menuState.visible
    ) {
      closeContextMenu();
    }
  };

  const handleCloseAllTags = () => {
    dispatch(emptyTaglist());
    navigate("/dashboard");
    closeContextMenu();
  };

  const handleCloseOtherTags = () => {
    const { currentTag } = menuState;
    dispatch(closeOtherTags(currentTag));
    navigate(currentTag?.path || "/dashboard");
    closeContextMenu();
  };

  useEffect(() => {
    document.addEventListener("click", handleClickOutside);
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [menuState.visible]);

  return (
    <>
      <Scrollbars
        autoHide
        autoHideTimeout={1000}
        autoHideDuration={200}
        hideTracksWhenNotNeeded
        renderView={(props) => (
          <div {...props} className="scrollbar-container" />
        )}
        renderTrackVertical={(props) => (
          <div {...props} className="scrollbar-track-vertical" />
        )}
      >
        <ul className="tags-wrap" ref={tagListContainer}>
          {taglist.map((tag) => (
            <li key={tag.path}>
              <Tag
                onClose={() => handleClose(tag)}
                closable={tag.path !== "/dashboard"}
                color={currentPath === tag.path ? "geekblue" : "gold"}
                onClick={() => handleClick(tag.path)}
                onContextMenu={(e) => openContextMenu(tag, e)}
              >
                {tag.title}
              </Tag>
            </li>
          ))}
        </ul>
      </Scrollbars>
      {menuState.visible && (
        <ul
          className="contextmenu"
          style={{ left: `${menuState.left}px`, top: `${menuState.top}px` }}
          ref={contextMenuContainer}
        >
          <li onClick={handleCloseOtherTags}>关闭其他</li>
          <li onClick={handleCloseAllTags}>关闭所有</li>
        </ul>
      )}
    </>
  );
};

export default TagList;
