import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Card, Button, Table, message, Divider } from "antd";
import { getAnswers, deleteAnswer, editAnswer, addAnswer } from "@/api/answer";
import TypingCard from "@/components/TypingCard";
import EditAnswerForm from "./forms/edit-answer-form";
import AddAnswerForm from "./forms/add-answer-form";

const { Column } = Table;

const Answer = () => {
  const [answers, setAnswers] = useState([]);
  const [editAnswerModalVisible, setEditAnswerModalVisible] = useState(false);
  const [editAnswerModalLoading, setEditAnswerModalLoading] = useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addAnswerModalVisible, setAddAnswerModalVisible] = useState(false);
  const [addAnswerModalLoading, setAddAnswerModalLoading] = useState(false);

  const { rpsID, rpsDetailID, questionID } = useParams();

  let editAnswerFormRef = null;
  let addAnswerFormRef = null;

  const fetchAnswers = async (questionID) => {
    const result = await getAnswers(questionID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setAnswers(content);
    }
  };

  useEffect(() => {
    fetchAnswers(questionID);
  }, [questionID]);

  const handleEditAnswer = (row) => {
    setCurrentRowData({ ...row });
    setEditAnswerModalVisible(true);
  };

  const handleDeleteAnswer = (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("error");
      return;
    }
    deleteAnswer({ id }).then((res) => {
      message.success("berhasil dihapus");
      fetchAnswers(questionID);
    });
  };

  const handleEditAnswerOk = () => {
    const { form } = editAnswerFormRef.props;
    form.validateFields((err, values) => {
      if (err) return;

      setEditAnswerModalLoading(true);
      editAnswer(values)
        .then((response) => {
          form.resetFields();
          setEditAnswerModalVisible(false);
          setEditAnswerModalLoading(false);
          message.success("berhasil!");
          fetchAnswers(questionID);
        })
        .catch((e) => {
          message.error("gagal");
        });
    });
  };

  const handleCancel = () => {
    setEditAnswerModalVisible(false);
    setAddAnswerModalVisible(false);
  };

  const handleAddAnswer = () => {
    setAddAnswerModalVisible(true);
  };

  const handleAddAnswerOk = () => {
    const { form } = addAnswerFormRef.props;
    form.validateFields((err, values) => {
      if (err) return;

      setAddAnswerModalLoading(true);
      const { file, ...otherValues } = values;
      const formData = new FormData();

      formData.append("title", otherValues.title);
      formData.append("description", otherValues.description);
      formData.append("type", otherValues.type);
      formData.append("is_right", otherValues.is_right);
      formData.append("question_id", questionID);

      if (file !== undefined) {
        formData.append("file", file.fileList[0].originFileObj);
      }

      addAnswer(formData)
        .then((response) => {
          form.resetFields();
          setAddAnswerModalVisible(false);
          setAddAnswerModalLoading(false);
          message.success("Berhasil!");
          fetchAnswers(questionID);
        })
        .catch((e) => {
          message.error("Gagal, silakan coba lagi!");
        });
    });
  };

  const questionTitle =
    answers.length > 0 ? answers[0].question.title : "No question available";
  const title = (
    <span>
      <Button type="primary" onClick={handleAddAnswer}>
        Tambahkan jawaban
      </Button>
    </span>
  );
  const cardContent = `Di sini, Anda dapat mengelola jawaban di sistem, seperti menambahkan jawaban baru, atau mengubah jawaban yang sudah ada di sistem.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen Jawaban" source={cardContent} />
      <br />
      <Card title={title}>
        <h3>{questionTitle}</h3>
        <Table variant rowKey="id" dataSource={answers} pagination={false}>
          <Column title="ID Jawaban" dataIndex="id" key="id" align="center" />
          <Column
            title="Jawaban"
            dataIndex="title"
            key="title"
            align="center"
          />
          <Column
            title="Deskripsi Jawaban"
            dataIndex="description"
            key="description"
            align="center"
          />
          <Column
            title="Tipe Soal"
            dataIndex="type"
            key="type"
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
                  title="mengedit"
                  onClick={() => handleEditAnswer(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteAnswer(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>
      <EditAnswerForm
        currentRowData={currentRowData}
        wrappedComponentRef={(formRef) => (editAnswerFormRef = formRef)}
        visible={editAnswerModalVisible}
        confirmLoading={editAnswerModalLoading}
        onCancel={handleCancel}
        onOk={handleEditAnswerOk}
      />
      <AddAnswerForm
        wrappedComponentRef={(formRef) => (addAnswerFormRef = formRef)}
        visible={addAnswerModalVisible}
        confirmLoading={addAnswerModalLoading}
        onCancel={handleCancel}
        onOk={handleAddAnswerOk}
      />
    </div>
  );
};

export default Answer;
