/* eslint-disable react/prop-types */
import React from "react";
import { PropTypes } from "prop-types";
import "./index.less";

const PanThumb = ({
  image,
  zIndex = 1,
  width = "150px",
  height = "150px",
  className = "",
  children,
}) => {
  return (
    <div
      className={`pan-item ${className}`}
      style={{
        zIndex,
        height,
        width,
      }}
    >
      <div className="pan-info">
        <div className="pan-info-roles-container">
          {React.Children.count(children) > 1 ? <>{children}</> : children}
        </div>
      </div>
      <img src={image} className="pan-thumb" alt="" />
    </div>
  );
};

PanThumb.propTypes = {
  image: PropTypes.string.isRequired,
  zIndex: PropTypes.number,
  width: PropTypes.string,
  height: PropTypes.string,
  className: PropTypes.string,
};

export default PanThumb;
