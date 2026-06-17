/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Button, Table } from "antd";
import { useNavigate } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPSQuiz1 } from "@/api/quiz";
import { getAllCriteriaValueByQuestion } from "@/api/criteriaValue";
import { getQuiz } from "@/api/quiz";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";

const QuizGenerate = () => {
  const navigate = useNavigate();
  const [quiz, setQuiz] = useState([]);
  const [quizId, setQuizId] = useState("");
  const [questionsWithCriteria, setQuestionsWithCriteria] = useState([]);
  const [devLecturers, setDevLecturers] = useState([]);

  const handleNextPage = () => {
    navigate(`/setting-quiz/generate-quiz-step6/${quizId}`);
  };

  const handlePreviousPage = () => {
    navigate(`/setting-quiz/generate-quiz-step4/${quizId}`);
  };

  const calculateMinMax = (questions) => {
    const calculateTop3MinMax = (array) => {
      const top3 = array.slice(0, 3);
      return {
        min: Math.min(...top3),
        max: Math.max(...top3),
      };
    };

    const result = {};
    for (let i = 1; i <= 9; i++) {
      const averagesArray = questions.map(
        (question) => question[`avgOfAvgValue${i}`]
      );
      const { min, max } = calculateTop3MinMax(averagesArray);
      result[`minValue${i}`] = min;
      result[`maxValue${i}`] = max;
    }

    return result;
  };

  const calculateResult = (questions) => {
    const calculateResultForArray = (array, index, minValue, maxValue) => {
      const q = array.slice(index, index + 1);
      const avgOfAvgValue = q.reduce((sum, avg) => sum + avg, 0) / q.length;
      return (0.11 * (maxValue - avgOfAvgValue)) / (maxValue - minValue);
    };

    const minMaxValues = calculateMinMax(questions);
    const results = Array.from({ length: questions.length }, () => []);

    for (let i = 1; i <= 9; i++) {
      const averagesArray = questions.map(
        (question) => question[`avgOfAvgValue${i}`]
      );
      for (let j = 0; j < questions.length; j++) {
        const result = calculateResultForArray(
          averagesArray,
          j,
          minMaxValues[`minValue${i}`],
          minMaxValues[`maxValue${i}`]
        );
        results[j].push(result);
      }
    }

    return results.flat();
  };

  const fetchData = async () => {
    try {
      const [quizResponse, rpsResponse] = await Promise.all([
        getQuiz(),
        getRPS(),
      ]);

      const { content: quizContent, statusCode: quizStatusCode } =
        quizResponse.data;
      const { content: rpsContent, statusCode: rpsStatusCode } =
        rpsResponse.data;

      if (quizStatusCode === 200 && rpsStatusCode === 200) {
        for (const quiz of quizContent) {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            setQuizId(quiz.id);
            setDevLecturers((prevLecturers) => [
              ...prevLecturers,
              ...matchingRPS.dev_lecturers,
            ]);

            const result = await getQuestionsByRPSQuiz1(matchingRPS.id);
            if (result.data.statusCode === 200) {
              const quizQuestions = result.data.content.filter(
                (question) => question.examType2 === "QUIZ"
              );

              const questionsWithCriteria = await Promise.all(
                quizQuestions.map(async (question) => {
                  const criteriaResult = await getAllCriteriaValueByQuestion(
                    question.id
                  );

                  if (criteriaResult.data.statusCode === 200) {
                    question.criteriaValues = criteriaResult.data.content;

                    for (let i = 1; i <= 9; i++) {
                      const totalAvg = question.criteriaValues.reduce(
                        (sum, response) =>
                          sum + parseFloat(response[`value${i}`].average),
                        0
                      );
                      question[`avgOfAvgValue${i}`] =
                        totalAvg / question.criteriaValues.length;
                    }
                  } else {
                    question.criteriaValues = [];
                  }
                  return question;
                })
              );

              setQuestionsWithCriteria(questionsWithCriteria);
            }
          }
        }
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const results = calculateResult(questionsWithCriteria);
  const chunks = results.reduce((acc, result, index) => {
    const chunkIndex = Math.floor(index / 9);
    if (!acc[chunkIndex]) acc[chunkIndex] = [];
    acc[chunkIndex].push(result);
    return acc;
  }, []);

  const maxValues = chunks.map((chunk) => Math.max(...chunk));
  const sumValues = chunks.map((chunk) =>
    chunk.reduce((sum, value) => sum + value, 0)
  );
  const overallMax = Math.max(...maxValues);
  const overallMin = Math.min(...maxValues);
  const Smax = Math.max(...sumValues);
  const Smin = Math.min(...sumValues);

  const tableStyle = {
    width: "100%",
    borderCollapse: "collapse",
    margin: "20px 0",
  };

  const cellStyle = {
    border: "1px solid #ddd",
    padding: "8px",
  };

  const headerCellStyle = {
    ...cellStyle,
    backgroundColor: "#f2f2f2",
  };

  const summaryCellStyle = {
    ...cellStyle,
    backgroundColor: "#f9f9f9",
  };

  return (
    <div>
      <TypingCard source="Hasil Nilai S dan R IVIHF-VIKOR untuk perhitungan perankingan nya" />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginTop: 20,
        }}
      >
        <Button type="primary" onClick={handlePreviousPage}>
          Tahap 4
        </Button>
        <Button type="primary" onClick={handleNextPage}>
          Hasil Akhir
        </Button>
      </div>
      <br />
      <br />
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={headerCellStyle}>Question</th>
            <th style={headerCellStyle}>Nilai R</th>
            <th style={headerCellStyle}>Nilai S</th>
          </tr>
        </thead>
        <tbody>
          {results.length === 0 ? (
            <tr>
              <td colSpan="3" style={{ ...cellStyle, textAlign: "center" }}>
                No results available
              </td>
            </tr>
          ) : (
            <>
              {chunks.map((chunk, index) => (
                <tr key={index}>
                  <td style={cellStyle}>
                    {questionsWithCriteria[index]?.title ||
                      `Question ${index + 1}`}
                  </td>
                  <td style={cellStyle}>{maxValues[index].toFixed(3)}</td>
                  <td style={cellStyle}>{sumValues[index].toFixed(3)}</td>
                </tr>
              ))}
              <tr>
                <td colSpan="3" style={summaryCellStyle}>
                  Nilai maximal R: {overallMax.toFixed(3)}
                </td>
                <td colSpan="3" style={summaryCellStyle}>
                  Nilai minimal R: {overallMin.toFixed(3)}
                </td>
              </tr>
              <tr>
                <td colSpan="3" style={summaryCellStyle}>
                  Nilai maximal S: {Smax.toFixed(3)}
                </td>
                <td colSpan="3" style={summaryCellStyle}>
                  Nilai minimal S: {Smin.toFixed(3)}
                </td>
              </tr>
            </>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default QuizGenerate;
