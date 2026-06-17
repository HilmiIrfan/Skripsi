/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, Divider, Checkbox } from "antd";
import { useParams } from "react-router-dom";
import { getAllQuestionsByRPS } from "@/api/criteriaValue";
import TypingCard from "@/components/TypingCard";
import { Link } from "react-router-dom";

const { Column } = Table;

const QuestionIndex = () => {
  const [criteriaValue, setCriteriaValue] = useState([]);
  const [rpsDetail, setRpsDetail] = useState([]);
  const [allQuestions, setAllQuestions] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [selectedQuestionID, setSelectedQuestionID] = useState(null);
  const { rpsID } = useParams();

  const fetchQuestions = async (rpsDetailID) => {
    try {
      const result = await getAllQuestionsByRPS(rpsDetailID);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setQuestions(content);
      }
    } catch (error) {
      console.error("Error fetching questions:", error);
    }
  };

  const getQuestionsQuiz = async (rpsID) => {
    try {
      const result = await getAllQuestionsByRPS(rpsID);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        return content.filter((question) => question.examType2 === "QUIZ");
      }
      return [];
    } catch (error) {
      console.error("Error fetching quiz questions:", error);
      return [];
    }
  };

  const getQuestionsExam = async (rpsID) => {
    try {
      const result = await getAllQuestionsByRPS(rpsID);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        return content.filter((question) => question.examType3 === "EXAM");
      }
      return [];
    } catch (error) {
      console.error("Error fetching exam questions:", error);
      return [];
    }
  };

  const getQuestionsExercise = async (rpsID) => {
    try {
      const result = await getAllQuestionsByRPS(rpsID);
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        return content.filter((question) => question.examType === "EXERCISE");
      }
      return [];
    } catch (error) {
      console.error("Error fetching exercise questions:", error);
      return [];
    }
  };

  const fetchAllQuestions = async () => {
    try {
      const [quizQuestions, examQuestions, exerciseQuestions] =
        await Promise.all([
          getQuestionsQuiz(rpsID),
          getQuestionsExam(rpsID),
          getQuestionsExercise(rpsID),
        ]);

      const combinedQuestions = [
        ...quizQuestions,
        ...examQuestions,
        ...exerciseQuestions,
      ];
      setAllQuestions(combinedQuestions);
      setQuestions(combinedQuestions);
    } catch (error) {
      console.error("Error fetching all questions:", error);
    }
  };

  useEffect(() => {
    if (!allQuestions.length) {
      fetchQuestions(rpsID);
    } else {
      fetchAllQuestions();
    }
  }, [rpsID]);

  const handleLinkClick = (id) => {
    setSelectedQuestionID(id);
  };

  const handleCheckboxChange = (checkedValues) => {
    const filteredQuestions = allQuestions.filter(
      (question) =>
        checkedValues.includes(question.examType) ||
        checkedValues.includes(question.examType2) ||
        checkedValues.includes(question.examType3)
    );
    setQuestions(filteredQuestions);
  };

  const options = ["EXERCISE", "QUIZ 1", "UTS", "QUIZ 2", "UAS"];
  const title = <div>Pilih pertanyaan yang akan dinilai</div>;
  const cardContent = `Di sini, Anda dapat memilih pertanyaan di sistem, lalu memberinya nilai masing masing kriteria.`;

  return (
    <div>
      <TypingCard title={title} source={cardContent} />
      <Checkbox.Group
        options={options}
        onChange={handleCheckboxChange}
        style={{ display: "flex", justifyContent: "flex-start" }}
      />
      <br />
      <Card title="Pertanyaan">
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

export default QuestionIndex;
