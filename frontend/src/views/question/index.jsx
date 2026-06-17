/* eslint-disable no-unused-vars */
import { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider, Checkbox } from "antd";
import {
  getQuestions,
  deleteQuestion,
  editQuestion,
  addQuestion,
} from "@/api/question";
import { useNavigate, useParams } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import EditQuestionForm from "./forms/edit-question-form";
import AddQuestionForm from "./forms/add-question-form";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

const { Column } = Table;

const Question = ({ imageNames }) => {
  const [questions, setQuestions] = useState([]);
  const [editQuestionModalVisible, setEditQuestionModalVisible] =
    useState(false);
  const [editQuestionModalLoading, setEditQuestionModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addQuestionModalVisible, setAddQuestionModalVisible] = useState(false);
  const [addQuestionModalLoading, setAddQuestionModalLoading] = useState(false);
  const [images, setImages] = useState({});
  const [selectedExamTypes, setSelectedExamTypes] = useState({
    EXERCISE: false,
    QUIZ: false,
    EXAM: false,
  });

  const { rpsID, rpsDetailID } = useParams();
  const navigate = useNavigate();

  const editQuestionFormRef = useRef();
  const addQuestionFormRef = useRef();

  const getQuestionsData = async (rpsDetailID) => {
    const result = await getQuestions(rpsDetailID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setQuestions(content);
    }
  };

  const handleEditQuestion = (row) => {
    setCurrentRowData({ ...row });
    setEditQuestionModalVisible(true);
  };

  const handleDeleteQuestion = (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak bisa menghapus Admin!");
      return;
    }
    deleteQuestion({ id }).then(() => {
      message.success("Berhasil dihapus");
      getQuestionsData(rpsDetailID);
    });
  };

  const handleEditQuestionOk = () => {
    const { form } = editQuestionFormRef.current;
    form.validateFields((err, values) => {
      if (err) return;

      setEditQuestionModalLoading(true);
      editQuestion(values)
        .then(() => {
          form.resetFields();
          setEditQuestionModalVisible(false);
          setEditQuestionModalLoading(false);
          message.success("Berhasil!");
          getQuestionsData(rpsDetailID);
        })
        .catch(() => {
          message.error("Gagal");
        });
    });
  };

  const handleCancel = () => {
    setEditQuestionModalVisible(false);
    setAddQuestionModalVisible(false);
  };

  const handleAddQuestion = () => {
    setAddQuestionModalVisible(true);
  };

  const handleAddQuestionOk = () => {
    const { form } = addQuestionFormRef.current;
    form.validateFields((err, values) => {
      if (err) return;

      setAddQuestionModalLoading(true);
      const { file, ...otherValues } = values;

      const formData = new FormData();
      if (file && file.fileList.length > 0) {
        formData.append("file", file.fileList[0].originFileObj);
      }
      formData.append("rps_detail_id", rpsDetailID);
      formData.append("title", otherValues.title);
      formData.append("description", otherValues.description);
      formData.append("question_type", otherValues.question_type);
      formData.append("answer_type", otherValues.answer_type);
      formData.append("explanation", otherValues.explanation);

      if (otherValues.examType) {
        formData.append("examType", otherValues.examType);
      }
      if (otherValues.examType2) {
        formData.append("examType2", otherValues.examType2);
      }
      if (otherValues.examType3) {
        formData.append("examType3", otherValues.examType3);
      }

      addQuestion(formData)
        .then(() => {
          form.resetFields();
          setAddQuestionModalVisible(false);
          setAddQuestionModalLoading(false);
          message.success("Berhasil!");
          getQuestionsData(rpsDetailID);
        })
        .catch(() => {
          message.error("Gagal menambahkan, silakan coba lagi!");
        });
    });
  };

  const getFilteredData = () => {
    if (
      !selectedExamTypes.EXERCISE &&
      !selectedExamTypes.QUIZ &&
      !selectedExamTypes.EXAM
    ) {
      return questions;
    }

    return questions.filter(
      (question) =>
        (selectedExamTypes.EXERCISE && question.examType === "EXERCISE") ||
        (selectedExamTypes.QUIZ &&
          question.examType2 === "QUIZ" &&
          question.examType2 !== "NOTHING") ||
        (selectedExamTypes.EXAM &&
          question.examType3 === "EXAM" &&
          question.examType3 !== "NOTHING")
    );
  };

  const handleCheckboxChange = (checkedValues) => {
    setSelectedExamTypes({
      EXERCISE: checkedValues.includes("EXERCISE"),
      QUIZ: checkedValues.includes("QUIZ"),
      EXAM: checkedValues.includes("EXAM"),
    });
  };

  useEffect(() => {
    getQuestionsData(rpsDetailID);
  }, [rpsDetailID]);

  const options = ["EXERCISE", "QUIZ", "EXAM"];
  let imageElements = null;

  if (imageNames) {
    imageElements = imageNames.map((imageName) => {
      const base64 = images[imageName];
      return base64 ? (
        <img
          key={imageName}
          src={`data:image/png;base64,${base64}`}
          alt={imageName}
        />
      ) : null;
    });
  }

  const title = (
    <span>
      <Button type="primary" onClick={handleAddQuestion}>
        Tambahkan pertanyaan
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola pertanyaan di sistem, seperti menambahkan pertanyaan baru, atau mengubah pertanyaan yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Pertanyaan" source={cardContent} />
      <Checkbox.Group
        options={options}
        onChange={handleCheckboxChange}
        style={{ display: "flex", justifyContent: "flex-start" }}
      />
      <br />
      {imageElements}
      <Card title={title}>
        <Table
          variant
          rowKey="id"
          dataSource={getFilteredData()}
          pagination={false}
        >
          <Column
            title="ID"
            key="id"
            align="center"
            render={(value, record, index) => index + 1}
          />
          <Column
            title="Pertanyaan"
            dataIndex="title"
            key="title"
            align="center"
          />
          <Column
            title="Deskripsi Pertanyaan"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Tipe Jawaban"
            dataIndex="answerType"
            key="answerType"
            align="center"
          />
          <Column
            title="Tipe Soal"
            dataIndex="questionType"
            key="questionType"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Button
                  type="primary"
                  shape="circle"
                  icon="edit"
                  title="edit soal"
                  onClick={() => handleEditQuestion(row)}
                />
                <Divider type="vertical" />
                <Link to={`/rps/${rpsID}/${rpsDetailID}/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="Tambahkan Soal"
                  />
                </Link>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="Hapus soal"
                  onClick={() => handleDeleteQuestion(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditQuestionForm
        currentRowData={currentRowData}
        wrappedComponentRef={editQuestionFormRef}
        visible={editQuestionModalVisible}
        confirmLoading={editQuestionModalLoading}
        onCancel={handleCancel}
        onOk={handleEditQuestionOk}
      />

      <AddQuestionForm
        wrappedComponentRef={addQuestionFormRef}
        visible={addQuestionModalVisible}
        confirmLoading={addQuestionModalLoading}
        onCancel={handleCancel}
        onOk={handleAddQuestionOk}
      />
    </div>
  );
};

Question.propTypes = {
  imageNames: PropTypes.arrayOf(PropTypes.string),
};

export default Question;
