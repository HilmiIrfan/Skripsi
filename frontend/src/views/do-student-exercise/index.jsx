/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Modal, Row, Col, Radio, message } from "antd";
import { getExerciseByID } from "@/api/exercise";
import { addAttemptExercise } from "@/api/attemptExercise";
import { getStudentByUser } from "@/api/student";
import { getAnswers } from "@/api/answer";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo } from "../../store/actions/user";

const DoStudentExercise = () => {
  const [exercise, setExercise] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [currentQuestion, setCurrentQuestion] = useState(1);
  const [timerInterval, setTimerInterval] = useState(null);
  const [remainingTime, setRemainingTime] = useState(0);
  const [addAppraisalFormModalLoading, setAddAppraisalFormModalLoading] =
    useState(false);

  const hadoopURL = "http://hadoop-primary:9870/";
  const navigate = useNavigate();
  const { id } = useParams();
  const dispatch = useDispatch();
  const { idUser } = useSelector((state) => state.user);

  const startTimer = () => {
    const interval = setInterval(() => {
      setRemainingTime((prevTime) => {
        if (prevTime > 0) {
          const newTime = prevTime - 1;
          localStorage.setItem("remainingTime", newTime.toString());
          return newTime;
        }
        clearInterval(interval);
        return prevTime;
      });
    }, 1000);

    setTimerInterval(interval);
  };

  const getAnswers = async (questionID) => {
    const result = await getAnswers(questionID);
    const { content } = result.data;
    if (result.status === 200) {
      return content;
    }
  };

  const getExercise = async (exerciseId) => {
    const result = await getExerciseByID(exerciseId);
    const { content } = result.data;
    if (result.status === 200) {
      const questionsWithNumber = content.questions.map((question, index) => ({
        ...question,
        number: index + 1,
      }));

      const answerPromises = questionsWithNumber.map((question) =>
        getAnswers(question.id)
      );

      const answers = await Promise.all(answerPromises);
      const questionsWithAnswers = questionsWithNumber.map(
        (question, index) => ({
          ...question,
          answers: answers[index],
        })
      );

      const duration = content.duration * 60;

      setExercise(content);
      setQuestions(questionsWithAnswers);
      setRemainingTime(duration);

      localStorage.setItem("exerciseDuration", duration.toString());
      startTimer();
    }
  };

  const saveSelectedAnswer = (questionId, answerId) => {
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionId]: answerId,
    }));
  };

  const handleNextQuestion = () => {
    if (currentQuestion < questions.length) {
      setCurrentQuestion((prev) => prev + 1);
    }
  };

  const handlePreviousQuestion = () => {
    if (currentQuestion > 1) {
      setCurrentQuestion((prev) => prev - 1);
    }
  };

  const handleQuestionClick = (questionNumber) => {
    setCurrentQuestion(questionNumber);
  };

  const handleSubmit = () => {
    Modal.confirm({
      title: "Konfirmasi",
      content: "Apakah Anda yakin ingin mengakhiri ujian?",
      okText: "OK",
      cancelText: "Batal",
      onOk: async () => {
        setAddAppraisalFormModalLoading(true);
        const result = await getStudentByUser(idUser);
        const { content } = result.data;

        if (result.status === 200) {
          const values = {
            exercise_id: id,
            user_id: idUser,
            student_id: content[0].id,
            duration: Math.floor(remainingTime / 60),
            studentAnswers: Object.values(selectedAnswers),
          };

          try {
            await addAttemptExercise(values);
            message.success("Berhasil menyimpan data!");
            navigate.push("/exercise");
          } catch (e) {
            message.error("Gagal menyimpan data!");
          }
        }
      },
    });
  };

  useEffect(() => {
    const storedDuration = localStorage.getItem("exerciseDuration");

    if (storedDuration) {
      const duration = parseInt(storedDuration, 10);
      const storedRemainingTime = localStorage.getItem("remainingTime");
      let timeRemaining = duration;

      if (storedRemainingTime) {
        timeRemaining = parseInt(storedRemainingTime, 10);
      } else {
        localStorage.setItem("remainingTime", timeRemaining.toString());
      }

      setRemainingTime(timeRemaining);
      startTimer();
    }

    getExercise(id);

    const handleBeforeUnload = () => {
      localStorage.setItem("remainingTime", remainingTime.toString());
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      clearInterval(timerInterval);
      localStorage.setItem("remainingTime", remainingTime.toString());
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  const currentQuestionData = questions.find(
    (question) => question.number === currentQuestion
  );

  const isFirstQuestion = currentQuestion === 1;
  const isLastQuestion = currentQuestion === questions.length;
  const currentAnswers = currentQuestionData?.answers || [];
  const minutes = Math.floor(remainingTime / 60);
  const seconds = remainingTime % 60;

  return (
    <div className="app-container">
      <Card
        title={
          "Waktu tersisa: " +
          minutes.toString().padStart(2, "0") +
          ":" +
          seconds.toString().padStart(2, "0")
        }
      >
        <Row>
          <Col span={18}>
            <Card>
              <Row>
                <Col span={4}>
                  <p>No. Soal</p>
                  <h1>{currentQuestion}</h1>
                </Col>
                <Col span={20}>
                  {currentQuestionData ? (
                    <>
                      <h2>
                        Pertanyaan:
                        <br />
                        {currentQuestionData?.title}
                      </h2>
                      {currentQuestionData?.question_type === "VIDEO" && (
                        <video
                          src={hadoopURL + currentQuestionData?.file_path}
                          controls
                          style={{ width: "100%" }}
                        />
                      )}
                      {currentQuestionData?.question_type === "AUDIO" && (
                        <audio
                          src={hadoopURL + currentQuestionData?.file_path}
                          controls
                          style={{ width: "100%" }}
                        />
                      )}
                      {currentQuestionData?.question_type === "IMAGE" && (
                        <img
                          src={hadoopURL + currentQuestionData?.file_path}
                          alt={currentQuestionData?.title}
                          style={{ width: "100%" }}
                        />
                      )}
                      <br />
                      <br />
                      <h1>Pilih salah satu jawaban:</h1>
                      <br />

                      <Radio.Group
                        name="answer"
                        value={selectedAnswers[currentQuestionData.id]}
                        onChange={(e) =>
                          saveSelectedAnswer(
                            currentQuestionData?.id,
                            e.target.value
                          )
                        }
                      >
                        {currentAnswers.map((answer) => (
                          <div
                            key={answer.id}
                            style={{
                              display: "grid",
                              gridTemplateRows: "auto 1fr",
                              paddingBottom: "20px",
                            }}
                          >
                            <Radio value={answer.id}>{answer.title}</Radio>
                            {answer.type === "VIDEO" && (
                              <video
                                src={hadoopURL + answer.file_path}
                                controls
                                style={{ width: "200px" }}
                              />
                            )}
                            {answer.type === "AUDIO" && (
                              <audio
                                src={hadoopURL + answer.file_path}
                                controls
                              />
                            )}
                            {answer.type === "IMAGE" && (
                              <img
                                src={hadoopURL + answer.file_path}
                                alt={answer.title}
                                style={{ width: "200px" }}
                              />
                            )}
                          </div>
                        ))}
                      </Radio.Group>
                    </>
                  ) : (
                    <p>Tidak ada pertanyaan yang tersedia.</p>
                  )}
                </Col>
              </Row>
              <Row justify="space-between">
                <Col span={12} align="start">
                  <Button
                    type="secondary"
                    onClick={handlePreviousQuestion}
                    disabled={isFirstQuestion}
                  >
                    Sebelumnya
                  </Button>
                </Col>
                <Col span={12} align="end">
                  <Button
                    type="primary"
                    onClick={isLastQuestion ? handleSubmit : handleNextQuestion}
                  >
                    {isLastQuestion ? "Simpan" : "Selanjutnya"}
                  </Button>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col span={6}>
            <Card title={"Nomor"}>
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  flexWrap: "wrap",
                }}
              >
                {questions.map((question) => (
                  <div
                    key={question.number}
                    className={`question-number ${
                      question.number === currentQuestion ? "active" : ""
                    }`}
                    onClick={() => handleQuestionClick(question.number)}
                    style={{
                      margin: "5px",
                      padding: "10px",
                      border: "1px solid #ccc",
                      borderRadius: "5px",
                      cursor: "pointer",
                    }}
                  >
                    {question.number}
                  </div>
                ))}
              </div>
            </Card>
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default DoStudentExercise;
