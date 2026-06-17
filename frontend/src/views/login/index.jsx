/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import { useState } from "react";
import { Navigate } from "react-router-dom";
import { Form, Input, Button, message, Spin } from "antd";
import { connect } from "react-redux";
import DocumentTitle from "react-document-title";
import "./index.less";
import { login, getUserInfo } from "@/store/actions";
import { LockOutlined, UserOutlined } from "@ant-design/icons";

const Login = (props) => {
  const { token, login, getUserInfo } = props;

  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm(); // Gunakan hook Form

  const handleLogin = (usernameOrEmail, password) => {
    setLoading(true);
    login(usernameOrEmail, password)
      .then((data) => {
        message.success("Login berhasil");
        handleUserInfo(data.accessToken);
      })
      .catch((_error) => {
        setLoading(false);
        message.error("Username atau password salah");
      });
  };

  // Mengambil informasi pengguna
  const handleUserInfo = (token) => {
    getUserInfo(token)
      .then((_data) => {})
      .catch((error) => {
        message.error(error);
      });
  };

  const handleSubmit = () => {
    form
      .validateFields()
      .then((values) => {
        const { usernameOrEmail, password } = values;
        handleLogin(usernameOrEmail, password);
      })
      .catch((errorInfo) => {
        console.log("Validasi gagal:", errorInfo);
      });
  };

  if (token) {
    return <Navigate to="/dashboard" />;
  }

  return (
    <DocumentTitle title={"Login Pengguna"}>
      <div className="login-container">
        <Form form={form} onFinish={handleSubmit} className="content">
          <div className="title">
            <h2>Login Pengguna</h2>
          </div>
          <Spin spinning={loading} tip="Sedang masuk...">
            <Form.Item
              name="usernameOrEmail"
              // initialValue="admin"
              rules={[
                {
                  required: true,
                  whitespace: true,
                  message: "Masukkan username",
                },
              ]}
            >
              <Input
                prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
                placeholder="Username"
              />
            </Form.Item>
            <Form.Item
              name="password"
              // initialValue="password"
              rules={[
                {
                  required: true,
                  whitespace: true,
                  message: "Masukkan password",
                },
              ]}
            >
              <Input
                prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
                type="password"
                placeholder="Password"
              />
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Masuk
              </Button>
            </Form.Item>
          </Spin>
        </Form>
      </div>
    </DocumentTitle>
  );
};

export default connect((state) => state.user, { login, getUserInfo })(Login);
