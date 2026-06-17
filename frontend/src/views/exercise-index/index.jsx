/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Card, Button, Table, message } from "antd";
import { getRPSDetail } from "@/api/rpsDetail";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { Link } from "react-router-dom";
import { getRPS } from "@/api/rps";
import AddExerciseForm from "./forms/add-exercise-index-form";
import { getExerciseByID, getExercise, addExercise } from "@/api/exercise";
import { getQuestionsByRPS } from "../../api/question";

const { Column } = Table;

const ExerciseIndex = () => {
  const [criteriaValue, setCriteriaValue] = useState([]);
  const [rpsDetail, setRpsDetail] = useState([]);
  const [rps, setRps] = useState([]);
  const [exercise, setExercise] = useState([]);
  const [allQuestions, setAllQuestions] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [weekDetails, setWeekDetails] = useState([]);
  const [editCriteriaValueModalVisible, setEditCriteriaValueModalVisible] =
    useState(false);
  const [editCriteriaValueModalLoading, setEditCriteriaValueModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addExerciseModalVisible, setAddExerciseModalVisible] = useState(false);
  const [addExerciseModalLoading, setAddExerciseModalLoading] = useState(false);
  const [exerciseID, setExerciseID] = useState("");
  const [rpsID, setRpsID] = useState("");
  const [selectedOption, setSelectedOption] = useState("");
  const [addExerciseFormRef, setAddExerciseFormRef] = useState(null);

  const navigate = useNavigate();
  const { exerciseID: paramExerciseID } = useParams();
  const location = useLocation();

  const getRpsData = async () => {
    const result = await getRPS();
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      setRps(content);
    }
  };

  const getExerciseData = async () => {
    const result = await getExercise();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setExercise(content);
    }
  };

  const getQuestionsByRPSData = async (id) => {
    const result = await getQuestionsByRPS(id);
    const { content, statusCode } = result.data;

    if (statusCode === 200) {
      const filteredQuestions = content.filter(
        (question) => question.examType2 === "QUIZ"
      );
      setQuestions(filteredQuestions);
    }
  };

  const getExerciseByIDData = async (ExerciseID) => {
    const result = await getExerciseByID(ExerciseID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setExercise(content);
      setRpsID(content.rps.id);
      getQuestionsByRPSData(content.rps.id);
    }
  };

  const handleAddExercise = (row) => {
    setAddExerciseModalVisible(true);
    setCurrentRowData({ ...row });
  };

  const handleAddExerciseOk = () => {
    const { form } = addExerciseFormRef.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      setAddExerciseModalLoading(true);
      addExercise(values)
        .then((response) => {
          form.resetFields();
          setAddExerciseModalVisible(false);
          setAddExerciseModalLoading(false);
          message.success("Berhasil!");
          getExerciseData();
        })
        .catch((e) => {
          message.error("Gagal menambahkan, coba lagi!");
        });
    });
  };

  const handleCancel = () => {
    setAddExerciseModalVisible(false);
  };

  const handleSelectChange = (value) => {
    setSelectedOption(value);
  };

  const handleOptionChange = (selectedOption) => {
    const filteredQuestions = allQuestions.filter(
      (question) => question.option === selectedOption
    );
    setQuestions(filteredQuestions);
  };

  useEffect(() => {
    setExerciseID(paramExerciseID);
    getExerciseByIDData(paramExerciseID);
    getQuestionsByRPSData("RPS-PBO-001");
    getRpsData();
  }, [paramExerciseID]);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddExercise}>
        Pilih Soal Untuk Latihan
      </Button>
    </span>
  );

  const data = questions.map((question, index) => ({
    questionId: question.id,
    questionTitle: question.title,
    rpsId: rps[index]?.id,
    rpsTitle: rps[index]?.title,
    devLecturers: rps[index]?.dev_lecturers
      ?.map((lecturer) => `${lecturer.name} (${lecturer.id})`)
      .join(", "),
  }));

  const devLecturerData = rps.map((rp) => ({
    devLecturers:
      rp.dev_lecturers?.map((lecturer) => ({
        name: lecturer.name,
        id: lecturer.id,
      })) || [],
  }));

  return (
    <div>
      <Card title={title}>
        <Table dataSource={devLecturerData} rowKey={(record, index) => index}>
          <Column
            title="Dev Lecturers"
            key="devLecturers"
            render={(text, record) => (
              <table>
                <thead>
                  <tr>
                    <th>Lecturer Name</th>
                    <th>Lecturer ID</th>
                  </tr>
                </thead>
                <tbody>
                  {record.devLecturers.map((lecturer, index) => (
                    <tr key={index}>
                      <td>{lecturer.name}</td>
                      <td>{lecturer.id}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          />
        </Table>
        <Table dataSource={data} rowKey="questionId">
          <Column
            title="ID Pertanyaan"
            dataIndex="questionId"
            key="questionId"
            align="center"
          />
          <Column
            title="Pertanyaan"
            dataIndex="questionTitle"
            key="questionTitle"
            align="center"
          />
          <Column title="ID RPS" dataIndex="rpsId" key="rpsId" align="center" />
          <Column
            title="RPS"
            dataIndex="rpsTitle"
            key="rpsTitle"
            align="center"
          />
          <Column
            title="Lecturers"
            dataIndex="devLecturers"
            key="devLecturers"
            align="center"
          />
        </Table>
      </Card>

      <AddExerciseForm
        exercise={exercise}
        wrappedComponentRef={setAddExerciseFormRef}
        visible={addExerciseModalVisible}
        currentRowData={currentRowData}
        onOptionChange={handleOptionChange}
        confirmLoading={addExerciseModalLoading}
        onCancel={handleCancel}
        onOk={handleAddExerciseOk}
        questions={questions}
        rps={rps}
      />
    </div>
  );
};

export default ExerciseIndex;
