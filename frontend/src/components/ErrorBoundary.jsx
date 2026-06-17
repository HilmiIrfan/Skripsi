import React from "react";
import { Result, Button } from "antd";
import PropTypes from "prop-types";

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null, errorInfo: null };
  }

  static getDerivedStateFromError() {
    // Update state so the next render will show the fallback UI
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    // Log the error to console or error reporting service
    console.error("Error Boundary caught an error:", error, errorInfo);
    this.setState({
      error: error,
      errorInfo: errorInfo,
    });
  }

  render() {
    if (this.state.hasError) {
      return (
        <Result
          status="500"
          title="Terjadi Kesalahan"
          subTitle="Maaf, terjadi kesalahan yang tidak terduga. Silakan refresh halaman atau hubungi administrator."
          extra={
            <Button type="primary" onClick={() => window.location.reload()}>
              Refresh Halaman
            </Button>
          }
        />
      );
    }

    return this.props.children;
  }
}

ErrorBoundary.propTypes = {
  children: PropTypes.node.isRequired,
};

export default ErrorBoundary;
