import { useState, useEffect } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { useParams } from "react-router-dom";
import {
  getAllCriteriaValueByQuestion,
  addCriteriaValue,
  deleteCriteriavalue,
} from "@/api/criteriaValue";
import { getAnswers } from "@/api/answer";
import { getLinguisticValues } from "@/api/linguisticValue";
import { getQuestionsByRPS } from "../../api/question";
import { reqUserInfo, getUserById } from "@/api/user";
import TypingCard from "@/components/TypingCard";
import AddCriteriaValueForm from "./form/add-criteria-value-form";

const { Column } = Table;

const CriteriaIndex = () => {
  const [criteriaValues, setCriteriaValues] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [linguisticValues, setLinguisticValues] = useState([]);
  const [userInfo, setUserInfo] = useState([]);
  const [userIdJson, setUserIdJson] = useState("");
  const [answers, setAnswers] = useState([]);
  const [addCriteriaValueModalVisible, setAddCriteriaValueModalVisible] =
    useState(false);
  const [addCriteriaValueModalLoading, setAddCriteriaValueModalLoading] =
    useState(false);
  const [selectedQuestionID, setSelectedQuestionID] = useState("");
  const [selectedQuestionTitle, setSelectedQuestionTitle] = useState("");
  const [answerTitle, setAnswerTitle] = useState("");
  const [answerTitle1, setAnswerTitle1] = useState("");
  const [answerTitle2, setAnswerTitle2] = useState("");
  const [answerTitle3, setAnswerTitle3] = useState("");
  const [userId, setUserId] = useState("");
  const [deleting, setDeleting] = useState(false);

  const { questionID } = useParams();
  let addCriteriaValueFormRef = null;

  const getCriteriaValues = async (questionID) => {
    const result = await getAllCriteriaValueByQuestion(questionID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      const filteredContent = content.filter(
        (item) => item.user === userIdJson
      );
      setCriteriaValues(filteredContent);
      setSelectedQuestionID(questionID);
    }
  };

  const handleDeleteCriteriaValue = (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Cannot delete admin");
      return;
    }
    setDeleting(true);
    deleteCriteriavalue({ id })
      .then((res) => {
        message.success("Berhasil dihapus");
        getCriteriaValues(questionID);
      })
      .catch((error) => {
        message.error("Delete operation failed");
        console.error(error.response || error);
      })
      .finally(() => {
        setDeleting(false);
      });
  };

  const getUserInfo = async () => {
    const result = await reqUserInfo();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setUserId(content.id);
      setUserInfo(content);
    }
  };

  const getUserInfoJson = async (userId) => {
    const result = await getUserById(userId);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setUserIdJson(content[0].id);
    }
  };

  const getQuestions = async () => {
    const result = await getQuestionsByRPS("RPS-PBO-001");
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      const question = content.find((q) => q.id === questionID);
      if (question) {
        setQuestions(content);
        setSelectedQuestionTitle(question.title);
      } else {
        setQuestions(content);
        setSelectedQuestionTitle("");
      }
    }
  };

  const getAnswers = async (questionID) => {
    const result = await getAnswers(questionID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setAnswers(content);
      setAnswerTitle(content[0].title);
      setAnswerTitle1(content[1].title);
      setAnswerTitle2(content[2].title);
      setAnswerTitle3(content[3].title);
    }
  };

  const getLinguisticValues = async () => {
    const result = await getLinguisticValues();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setLinguisticValues(content);
    }
  };

  const handleAddCriteriaValue = () => {
    setAddCriteriaValueModalVisible(true);
  };

  const handleCancel = () => {
    setAddCriteriaValueModalVisible(false);
  };

  const handleAddCriteriaValueOk = () => {
    const { form } = addCriteriaValueFormRef.props;
    form.validateFields((err, values) => {
      if (err) return;

      setAddCriteriaValueModalLoading(true);
      values.userId = userId;

      addCriteriaValue(values, selectedQuestionID)
        .then((response) => {
          form.resetFields();
          setAddCriteriaValueModalVisible(false);
          setAddCriteriaValueModalLoading(false);
          message.success("Berhasil menambahkan Criteria Value!");
          getCriteriaValues(selectedQuestionID);
        })
        .catch((e) => {
          console.error(e.response.data);
          message.error("Gagal menambahkan Criteria Value!");
        });
    });
  };

  useEffect(() => {
    const initializeData = async () => {
      const userInfoResponse = await reqUserInfo();
      const { id: userId } = userInfoResponse.data;

      await getUserInfoJson(userId);
      getQuestions();
      getLinguisticValues();
      getAnswers(questionID);
      getCriteriaValues(questionID);
    };

    initializeData();
  }, [questionID]);

  // Render logic remains mostly the same, just remove 'this'
  const criteriaNames = [
    "Nama",
    "Evaluation",
    "Synthesis",
    "Comprehension",
    "Analysis",
    "Difficulty",
    "Reliability",
    "Discrimination",
    "Application",
    "Knowledge",
  ];

  const columns = Array.from({ length: 9 }, (_, i) => (
    <Column
      title={`Nilai Kriteria ${criteriaNames[i + 1]}`}
      dataIndex={`value${i + 1}.name`}
      key={`value${i + 1}.name`}
      align="center"
    />
  ));

  const title = (
    <span>
      <Button type="primary" onClick={handleAddCriteriaValue}>
        Berikan Nilai ke soal
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat menilai pertanyaan di sistem, lalu memberinya nilai masing masing kriteria.`;

  return (
    <div>
      <TypingCard title={title} source={cardContent} />
      <Card title={""}>
        <h3>{`Soal: ${selectedQuestionTitle}`}</h3>
        <h3>
          List Jawaban:
          <div>{answerTitle}</div>
          <div>{answerTitle1}</div>
          <div>{answerTitle2}</div>
          <div>{answerTitle3}</div>
        </h3>

        <Table dataSource={criteriaValues} rowKey="id">
          {columns}
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteCriteriaValue(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>
      <h3>Nilai dalam bentuk numerik</h3>

      <AddCriteriaValueForm
        wrappedComponentRef={(formRef) => (addCriteriaValueFormRef = formRef)}
        visible={addCriteriaValueModalVisible}
        confirmLoading={addCriteriaValueModalLoading}
        onCancel={handleCancel}
        onOk={handleAddCriteriaValueOk}
        linguisticValues={linguisticValues}
        questionID={questionID}
        userID={userIdJson}
      />
    </div>
  );
};

export default CriteriaIndex;
