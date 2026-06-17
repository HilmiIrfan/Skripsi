import { Row, Col } from "antd";
import CountUp from "react-countup";
import "./index.less";
import { getQuiz as fetchQuiz } from "@/api/quiz";
import { useState, useEffect } from "react";
import { BarChartOutlined } from "@ant-design/icons";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const PanelGroup = (props) => {
  const { handleSetLineChartData } = props;
  const [chartList, setChartList] = useState([]);
  const [quizMessages, setQuizMessages] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const quizResponse = await fetchQuiz();
        const { content: quizContent, statusCode: quizStatusCode } =
          quizResponse.data;

        const userInfoResponse = await reqUserInfo();
        const { content: userInfoContent, statusCode: userInfoStatusCode } =
          userInfoResponse.data;

        const rpsResponse = await getRPS();
        const { content: rpsContent, statusCode: rpsStatusCode } =
          rpsResponse.data;

        // Extract the list of devLecturerIds from the rpsResponse
        const devLecturerIds = rpsContent[0].dev_lecturers.map(
          (lecturer) => lecturer.id
        );

        if (
          quizStatusCode === 200 &&
          devLecturerIds.includes(userInfoResponse.data.id)
        ) {
          setQuizMessages(quizContent.map((item) => item.message));
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="panel-group-container">
      {quizMessages.map((message, index) => (
        <div
          key={index}
          style={{
            textAlign: "center",
            fontSize: "20px",
            marginBottom: "20px",
          }}
        >
          {message}
        </div>
      ))}
      <Row gutter={40} className="panel-group">
        {chartList.map((chart, i) => (
          <Col
            key={i}
            lg={6}
            sm={12}
            xs={12}
            onClick={handleSetLineChartData.bind(this, chart.type)}
            className="card-panel-col"
          >
            <div className="card-panel">
              <div className="card-panel-icon-wrapper">
                {chart.icon && (
                  <BarChartOutlined
                    className={chart.type}
                    style={{
                      fontSize: chart.type === quizMessages ? "1px" : "1px",
                      color: chart.color,
                    }}
                    type={chart.icon}
                  />
                )}
                <h2>{chart.type}</h2>
                {chart.type === "Quiz Message" ? (
                  <div>{chart.message}</div>
                ) : (
                  <CountUp start={0} end={chart.num} duration={2.75} />
                )}
              </div>
            </div>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default PanelGroup;
