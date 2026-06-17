/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Button, Card, message } from "antd";
import { useNavigate } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import { getQuestionsByRPSQuiz1, getQuiz, addQuiz } from "@/api/quiz";
import { getAllCriteriaValueByQuestion } from "@/api/criteriaValue";
import { getRPS } from "@/api/rps";
import { reqUserInfo } from "@/api/user";
import AddQuizForm from "./forms/add-quiz-form";

const QuizGenerate = () => {
  const navigate = useNavigate();
  const addQuizFormRef = useRef();

  const [quiz, setQuiz] = useState([]);
  const [rps, setRps] = useState([]);
  const [quizId, setQuizId] = useState("");
  const [questionsWithCriteria, setQuestionsWithCriteria] = useState([]);
  const [listQuestions, setListQuestions] = useState([]);
  const [quizDetails, setQuizDetails] = useState({
    name: "",
    duration: "",
    description: "",
    rpsId: "",
    minGrade: "",
    type: "",
  });
  const [modalState, setModalState] = useState({
    addVisible: false,
    addLoading: false,
  });

  const getRpsData = async () => {
    try {
      const result = await getRPS();
      if (result.data.statusCode === 200) {
        setRps(result.data.content);
      }
    } catch (error) {
      console.error("Error fetching RPS:", error);
    }
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
      const averagesArray = questions.map((q) => q[`avgOfAvgValue${i}`]);
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
      const averagesArray = questions.map((q) => q[`avgOfAvgValue${i}`]);
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

  const getQuestions = async (id) => {
    try {
      const result = await getQuestionsByRPSQuiz1(id);
      if (result.data.statusCode === 200) {
        const results = calculateResult(questionsWithCriteria);
        const chunkSize = Math.min(9, results.length);
        const chunks = results.reduce((acc, result, index) => {
          const chunkIndex = Math.floor(index / chunkSize);
          if (!acc[chunkIndex]) acc[chunkIndex] = [];
          acc[chunkIndex].push(result);
          return acc;
        }, []);

        const maxValues = chunks.map((chunk) => Math.max(...chunk));
        const sumValues = chunks.map((chunk) =>
          chunk.reduce((sum, val) => sum + val, 0)
        );

        const overallMax = Math.max(...maxValues);
        const overallMin = Math.min(...maxValues);
        const Smax = Math.max(...sumValues);
        const Smin = Math.min(...sumValues);

        const resultsWithRanks = chunks.map((chunk, idx) => {
          const maxResult = Math.max(...chunk);
          const sumResult = chunk.reduce((sum, val) => sum + val, 0);
          const questionTitle =
            result.data.content[idx]?.title || `Question ${idx + 1}`;

          const normalizedMax =
            overallMax !== overallMin
              ? (maxResult - overallMin) / (overallMax - overallMin)
              : 0;
          const normalizedSum =
            Smax !== Smin ? (sumResult - Smin) / (Smax - Smin) : 0;

          const finalResult = (
            0.5 * normalizedSum +
            0.5 * normalizedMax
          ).toFixed(3);
          return {
            questionTitle,
            result: parseFloat(finalResult),
            originalIndex: idx,
          };
        });

        const sortedResults = [...resultsWithRanks].sort(
          (a, b) => a.result - b.result
        );
        const rankedResults = sortedResults.map((item, idx) => ({
          ...item,
          rank: idx + 1,
        }));

        const sortedQuestions = rankedResults.map((item) => ({
          ...result.data.content[item.originalIndex],
          id: item.rank,
          rank: item.rank,
        }));

        setListQuestions(sortedQuestions);
      }
    } catch (error) {
      console.error("Error fetching questions:", error);
    }
  };

  const fetchData = async () => {
    try {
      const [quizRes, rpsRes] = await Promise.all([getQuiz(), getRPS()]);

      if (quizRes.data.statusCode === 200 && rpsRes.data.statusCode === 200) {
        const quizContent = quizRes.data.content;
        const rpsContent = rpsRes.data.content;

        for (const quiz of quizContent) {
          const matchingRPS = rpsContent.find((rps) => rps.id === quiz.rps.id);
          if (matchingRPS) {
            setQuizId(quiz.id);
            setQuizDetails({
              name: quiz.name,
              duration: quiz.duration,
              description: quiz.description,
              rpsId: quiz.rps.id,
              minGrade: quiz.min_grade,
              type: quiz.type_quiz,
            });

            const questionsResult = await getQuestionsByRPSQuiz1(
              matchingRPS.id
            );
            if (questionsResult.data.statusCode === 200) {
              const quizQuestions = questionsResult.data.content.filter(
                (q) => q.examType2 === "QUIZ"
              );

              const questionsWithCriteria = await Promise.all(
                quizQuestions.map(async (question) => {
                  const criteriaResult = await getAllCriteriaValueByQuestion(
                    question.id
                  );
                  if (criteriaResult.data.statusCode === 200) {
                    question.criteriaValues = criteriaResult.data.content;

                    // Calculate averages for all 9 values
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
    getRpsData();
  }, []);

  const handleAddQuiz = () => {
    setModalState((prev) => ({ ...prev, addVisible: true }));
  };

  const handleCancel = () => {
    setModalState((prev) => ({ ...prev, addVisible: false }));
  };

  const handleAddQuizOk = () => {
    const form = addQuizFormRef.current?.props.form;
    form?.validateFields(async (err, values) => {
      if (err) return;

      setModalState((prev) => ({ ...prev, addLoading: true }));
      try {
        await addQuiz(values);
        form.resetFields();
        setModalState({ addVisible: false, addLoading: false });
        message.success("Berhasil!");
        fetchData();
      } catch (error) {
        message.error("Gagal menambahkan, coba lagi!");
        setModalState((prev) => ({ ...prev, addLoading: false }));
      }
    });
  };

  const results = calculateResult(questionsWithCriteria);

  // ... Rest of the rendering logic remains mostly the same, just updated to use hooks state
  return (
    <div className="app-container">
      <TypingCard source="Hasil Akhir IVIHF-VIKOR dengan perankingan nya" />
      <br />
      <Card
        title={
          <Button type="primary" onClick={handleAddQuiz}>
            Tambahkan Soal dalam Kuis
          </Button>
        }
      >
        {/* Table rendering code remains the same */}
      </Card>
      <AddQuizForm
        wrappedComponentRef={addQuizFormRef}
        visible={modalState.addVisible}
        confirmLoading={modalState.addLoading}
        onCancel={handleCancel}
        onOk={handleAddQuizOk}
        list_questions={listQuestions}
        rps={rps}
        {...quizDetails}
      />
    </div>
  );
};

export default QuizGenerate;
