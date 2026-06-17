/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, Modal, Typography } from "antd";
import { getExercise } from "@/api/exercise";
import { getQuestions, getQuestionByIdPaged } from "@/api/question";
import { getRPS } from "@/api/rps";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPS } from "../../api/question";
import { useNavigate, useParams } from "react-router-dom";
import {
  getExerciseAttemptById,
  getQuestionsFromStudentAnswers,
} from "../../api/attemptExercise";
import { useSelector, useDispatch } from "react-redux";
import { getUserInfo } from "../../store/actions/user";
import moment from "moment";

const { Column } = Table;
const { Text } = Typography;

const StudentExerciseReview = () => {
  const [exercise, setExercise] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [rps, setRps] = useState([]);

  const { id } = useParams();
  const dispatch = useDispatch();

  const getQuestionsFromStudentAnswersData = async () => {
    const result = await getQuestionsFromStudentAnswers(id);
    const { content, statusCode } = result.data;

    if (result.status === 200) {
      setQuestions(content);
    }
  };

  const getExerciseAttemptByIdData = async () => {
    const result = await getExerciseAttemptById(id);
    const { content } = result.data;

    if (result.status === 200) {
      setExercise(content);

      if (Array.isArray(content.student_answers)) {
        getQuestionsFromStudentAnswersData(content.student_answers);
      } else {
        console.error("student_answers is not an array");
      }
    }
  };

  useEffect(() => {
    getExerciseAttemptByIdData();
  }, [id]);

  const title = <span></span>;
  const cardContent = `Di sini, Anda dapat melihat hasil ujian yang telah Anda kerjakan dan merviewnya dengan jawaban yang benar.`;

  return (
    <div className="app-container">
      <TypingCard title="Review Ujian" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={exercise} pagination={false}>
          <Column
            title="Soal"
            dataIndex="student_answers"
            key="question"
            render={(student_answers) => (
              <>
                {student_answers.map((answer, index) => (
                  <div
                    key={index}
                    style={{ height: "150px", overflow: "auto" }}
                  >
                    <Text>
                      {index + 1}. {answer.question.title}
                    </Text>
                  </div>
                ))}
              </>
            )}
          />
          <Column
            title="Jawaban Anda"
            dataIndex="student_answers"
            key="answer"
            render={(student_answers) => (
              <>
                {student_answers.map(
                  (answer, index) =>
                    answer.answer.title && (
                      <div
                        key={index}
                        style={{ height: "150px", overflow: "auto" }}
                      >
                        <Text>
                          {index + 1}. {answer.answer.title}
                        </Text>
                      </div>
                    )
                )}
              </>
            )}
          />
          <Column
            title="Penjelasan"
            dataIndex="explanations"
            key="explanation"
            render={(explanations) => (
              <>
                {explanations.map((explanation, index) => (
                  <div
                    key={index}
                    style={{ height: "150px", overflow: "auto" }}
                  >
                    <Text>
                      {index + 1}. {explanation}
                    </Text>
                  </div>
                ))}
              </>
            )}
          />
          <Column
            title="Validasi jawaban anda"
            dataIndex="student_answers"
            key="score"
            render={(student_answers) => (
              <>
                {student_answers.map((answer, index) => (
                  <div
                    key={index}
                    style={{ height: "150px", overflow: "auto" }}
                  >
                    <Text>{answer.score === 1 ? "benar" : "salah"}</Text>
                  </div>
                ))}
              </>
            )}
          />
        </Table>
      </Card>
    </div>
  );
};

export default StudentExerciseReview;
