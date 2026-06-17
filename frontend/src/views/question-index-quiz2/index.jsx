/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, message, Divider, Checkbox } from "antd";
import { getQuestionsByRPSQuiz2 } from "@/api/quiz";
import { getRPSDetail } from "@/api/rpsDetail";
import TypingCard from "@/components/TypingCard";
import { useNavigate, useParams, Link } from "react-router-dom";

const { Column } = Table;

const QuestionIndexQuiz2 = () => {
  const [criteriaValue, setCriteriaValue] = useState([]);
  const [rpsDetail, setRpsDetail] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [currentRowData, setCurrentRowData] = useState({});
  const [selectedQuestionID, setSelectedQuestionID] = useState(null);

  const { rpsID } = useParams();
  const navigate = useNavigate();

  const getQuestionsQuiz = async (rpsID) => {
    const result = await getQuestionsByRPSQuiz2(rpsID);
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      return content.filter((question) => question.examType2 === "QUIZ");
    }
    return [];
  };

  useEffect(() => {
    const fetchQuestions = async () => {
      const questionsList = await getQuestionsQuiz(rpsID);
      setQuestions(questionsList);
    };

    fetchQuestions();
  }, [rpsID]);

  const handleLinkClick = (id) => {
    setSelectedQuestionID(id);
  };

  return (
    <div>
      <TypingCard source="Daftar Pertanyaan Quiz" />
      <Card title="Daftar Pertanyaan Quiz">
        <Table dataSource={questions} rowKey="id">
          <Column
            title="ID Pertanyaan"
            dataIndex="id"
            key="id"
            align="center"
          />
          <Column
            title="Pertanyaan"
            dataIndex="title"
            key="title"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Divider type="vertical" />
                <Link
                  to={`/index/criteria/${row.id}`}
                  onClick={() => handleLinkClick(row.id)}
                >
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="menghapus"
                  />
                </Link>
                <Divider type="vertical" />
              </span>
            )}
          />
        </Table>
      </Card>
    </div>
  );
};

export default QuestionIndexQuiz2;
