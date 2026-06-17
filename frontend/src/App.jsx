import { Component } from "react";
import { Provider } from "react-redux";
import { ConfigProvider } from "antd";
import idID from "antd/es/locale/id_ID";
import store from "./store";
import Router from "./router";
import { AuthProvider } from "./contexts/AuthContext";

class App extends Component {
  render() {
    return (
      <ConfigProvider locale={idID}>
        <Provider store={store}>
          <AuthProvider>
            <Router />
          </AuthProvider>
        </Provider>
      </ConfigProvider>
    );
  }
}

export default App;
