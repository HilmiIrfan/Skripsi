import { createContext, useContext, useState, useEffect } from "react";
import { reqUserInfo } from "@/api/user";
import { getToken, removeToken } from "@/utils/auth";
import PropTypes from "prop-types";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchUserInfo = async () => {
    try {
      const token = getToken();
      if (!token) {
        setLoading(false);
        return;
      }

      const response = await reqUserInfo();
      if (response.data) {
        setUser(response.data);
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
      // If token is invalid, remove it
      removeToken();
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const logout = () => {
    removeToken();
    setUser(null);
  };

  const value = {
    user,
    loading,
    logout,
    refetchUser: fetchUserInfo,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
