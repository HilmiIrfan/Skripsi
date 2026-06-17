import { Button, Row, Col } from "antd";
import { useNavigate } from "react-router-dom";
import errImg from "@/assets/images/404.png";
import "./index.less";

const NotFound = () => {
  const navigate = useNavigate();

  const goHome = () => {
    navigate("/");
  };

  return (
    <Row className="not-found" justify="center" align="middle">
      <Col span={12}>
        <img src={errImg} alt="404" className="error-image" />
      </Col>
      <Col span={12} className="right">
        <h1>404</h1>
        <h2>抱歉，你访问的页面不存在</h2>
        <div>
          <Button type="primary" onClick={goHome}>
            回到 Beranda
          </Button>
        </div>
      </Col>
    </Row>
  );
};

export default NotFound;
