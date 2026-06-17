/* eslint-disable no-unused-vars */
import { setUserToken, resetUser } from "./user";
import { reqLogin, reqLogout } from "@/api/login";
import { setToken, removeToken } from "@/utils/auth";
export const login = (username, password) => (dispatch) => {
  return new Promise((resolve, reject) => {
    reqLogin({ usernameOrEmail: username.trim(), password: password })
      .then((response) => {
        console.log(response);
        const { data, status } = response;
        if (status === 200) {
          const token = data.accessToken;
          dispatch(setUserToken(token));
          setToken(token);
          resolve(data);
        } else {
          const msg = "Username / password salah";
          reject(msg);
        }
      })
      .catch((error) => {
        reject(error);
      });
  });
};

export const logout = (token) => (dispatch) => {
  return new Promise((resolve, reject) => {
    console.log(token);
    // reqLogout(token)
    //   .then((response) => {
    //     const { data } = response
    //     if (data.status === 0) {
    dispatch(resetUser());
    removeToken();
    //     resolve(data)
    //   } else {
    //     const msg = data.message
    //     reject(msg)
    //   }
    // })
    // .catch((error) => {
    //   reject(error)
    // })
  });
};
