/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getRPSDetail,
  deleteRPSDetail,
  editRPSDetail,
  addRPSDetail,
} from "@/api/rpsDetail";
import { getFormLearnings } from "@/api/formLearning";
import { getLearningMethods } from "@/api/learningMethod";
import { getAssessmentCriterias } from "@/api/assessmentCriteria";
import { getAppraisalForms } from "@/api/appraisalForm";
import { Link, useParams } from "react-router-dom";
import TypingCard from "@/components/TypingCard";
import EditRPSDetailForm from "./forms/edit-rpsDetail-form";
import AddRPSDetailForm from "./forms/add-rpsDetail-form";
import { getRPSById } from "@/api/rps";

const { Column } = Table;

const RPSDetailDetail = () => {
  const [rpsDetail, setRpsDetail] = useState([]);
  const [formLearnings, setFormLearnings] = useState([]);
  const [learningMethods, setLearningMethods] = useState([]);
  const [devLecturers, setDevLecturers] = useState([]);
  const [assessmentCriterias, setAssessmentCriterias] = useState([]);
  const [rps, setRps] = useState([]);
  const [appraisalForms, setAppraisalForms] = useState([]);
  const [editRPSDetailModalVisible, setEditRPSDetailModalVisible] =
    useState(false);
  const [editRPSDetailModalLoading, setEditRPSDetailModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addRPSDetailModalVisible, setAddRPSDetailModalVisible] =
    useState(false);
  const [addRPSDetailModalLoading, setAddRPSDetailModalLoading] =
    useState(false);

  const { rpsID } = useParams();
  const editRPSDetailFormRef = useRef();
  const addRPSDetailFormRef = useRef();

  const getRPSDetailData = async (rpsID) => {
    const result = await getRPSDetail(rpsID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setRpsDetail(content);
    }
  };

  const getRPSByIdData = async (rpsID) => {
    const result = await getRPSById(rpsID);
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setRps(content);
      setDevLecturers(content.dev_lecturers);
    }
  };

  const getFormLearningsData = async () => {
    const result = await getFormLearnings();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setFormLearnings(content);
    }
  };

  const getLearningMethodsData = async () => {
    const result = await getLearningMethods();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setLearningMethods(content);
    }
  };

  const getAssessmentCriteriasData = async () => {
    const result = await getAssessmentCriterias();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setAssessmentCriterias(content);
    }
  };

  const getAppraisalFormsData = async () => {
    const result = await getAppraisalForms();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setAppraisalForms(content);
    }
  };

  const handleEditRPSDetail = (row) => {
    setCurrentRowData({ ...row });
    setEditRPSDetailModalVisible(true);
  };

  const handleDeleteRPSDetail = (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak bisa menghapus Admin!");
      return;
    }
    deleteRPSDetail({ id }).then(() => {
      message.success("Berhasil dihapus");
      getRPSDetailData(rpsID);
    });
  };

  const handleEditRPSDetailOk = () => {
    const { form } = editRPSDetailFormRef.current;
    form.validateFields((err, values) => {
      if (err) return;

      setEditRPSDetailModalLoading(true);
      editRPSDetail(values)
        .then(() => {
          form.resetFields();
          setEditRPSDetailModalVisible(false);
          setEditRPSDetailModalLoading(false);
          message.success("Berhasil!");
          getRPSDetailData(rpsID);
        })
        .catch(() => {
          message.error("Gagal!");
        });
    });
  };

  const handleCancel = () => {
    setEditRPSDetailModalVisible(false);
    setAddRPSDetailModalVisible(false);
  };

  const handleAddRPSDetail = () => {
    setAddRPSDetailModalVisible(true);
  };

  const handleAddRPSDetailOk = () => {
    const { form } = addRPSDetailFormRef.current;
    form.validateFields((err, values) => {
      if (err) return;

      setAddRPSDetailModalLoading(true);
      const mergedObj = { ...{ rps_id: rpsID }, ...values };

      addRPSDetail(mergedObj)
        .then(() => {
          form.resetFields();
          setAddRPSDetailModalVisible(false);
          setAddRPSDetailModalLoading(false);
          message.success("Berhasil!");
          getRPSDetailData(rpsID);
        })
        .catch(() => {
          message.error("Gagal menambahkan, silakan coba lagi!");
        });
    });
  };

  useEffect(() => {
    getRPSDetailData(rpsID);
    getRPSByIdData(rpsID);
    getFormLearningsData();
    getLearningMethodsData();
    getAssessmentCriteriasData();
    getAppraisalFormsData();
  }, [rpsID]);

  const title = (
    <span>
      <Button type="primary" onClick={handleAddRPSDetail}>
        Tambahkan Detail RPS
      </Button>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola RPS sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list RPS yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen RPS" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={rpsDetail} pagination={false}>
          <Column title="ID RPS" dataIndex="id" key="id" align="center" />
          <Column
            title="Minggu Ke"
            dataIndex="week"
            key="week"
            align="center"
            sorter={(a, b) => b.week - a.week}
          />
          <Column
            title="Bobot"
            dataIndex="weight"
            key="weight"
            align="center"
          />
          <Column
            title="Materi Pembelajaran"
            dataIndex="learning_materials"
            key="learning_materials"
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
                  title="Edit"
                  onClick={() => handleEditRPSDetail(row)}
                />
                <Divider type="vertical" />
                <Link to={`/rps/${rpsID}/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="Detail"
                  />
                </Link>
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="Hapus"
                  onClick={() => handleDeleteRPSDetail(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditRPSDetailForm
        currentRowData={currentRowData}
        wrappedComponentRef={editRPSDetailFormRef}
        visible={editRPSDetailModalVisible}
        confirmLoading={editRPSDetailModalLoading}
        onCancel={handleCancel}
        onOk={handleEditRPSDetailOk}
      />

      <AddRPSDetailForm
        wrappedComponentRef={addRPSDetailFormRef}
        visible={addRPSDetailModalVisible}
        confirmLoading={addRPSDetailModalLoading}
        onCancel={handleCancel}
        onOk={handleAddRPSDetailOk}
        formLearnings={formLearnings}
        learningMethods={learningMethods}
        assessmentCriterias={assessmentCriterias}
        appraisalForms={appraisalForms}
        rpsID={rpsID}
      />
    </div>
  );
};

export default RPSDetailDetail;
