import PropTypes from "prop-types";
import { connect } from "react-redux";
import logo from "@/assets/images/logo.svg";
import "./index.less";

const Logo = ({ sidebarCollapsed }) => {
  return (
    <div className="sidebar-logo-container">
      <img src={logo} className="sidebar-logo" alt="logo" />
      {!sidebarCollapsed && <h1 className="sidebar-title">Bank Soal</h1>}
    </div>
  );
};

// Add PropTypes for better validation
Logo.propTypes = {
  sidebarCollapsed: PropTypes.bool.isRequired,
};

// Map state to props
const mapStateToProps = (state) => ({
  sidebarCollapsed: state.app.sidebarCollapsed,
});

// Connect the component to Redux
export default connect(mapStateToProps)(Logo);
