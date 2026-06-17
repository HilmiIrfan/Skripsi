/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import {
  getLinguisticValues,
  deleteLinguisticValue,
  editLinguisticValue,
  addLinguisticValue,
} from "@/api/linguisticValue";
import process from "process";
import TypingCard from "@/components/TypingCard";
import EditLinguisticValueForm from "./forms/edit-linguistic-value-form";
import AddLinguisticValueForm from "./forms/add-linguistic-value-form";

const { Column } = Table;
const BASE_URL = process.env.REACT_APP_BASE_URL;

const LinguisticValue = () => {
  const [linguisticValues, setLinguisticValues] = useState([]);
  const [editLinguisticValueModalVisible, setEditLinguisticValueModalVisible] =
    useState(false);
  const [editLinguisticValueModalLoading, setEditLinguisticValueModalLoading] =
    useState(false);
  const [currentRowData, setCurrentRowData] = useState({});
  const [addLinguisticValueModalVisible, setAddLinguisticValueModalVisible] =
    useState(false);
  const [addLinguisticValueModalLoading, setAddLinguisticValueModalLoading] =
    useState(false);

  const editLinguisticValueFormRef = useRef();
  const addLinguisticValueFormRef = useRef();

  const fetchLinguisticValues = async () => {
    try {
      const result = await getLinguisticValues();
      const { content, statusCode } = result.data;
      if (statusCode === 200) {
        setLinguisticValues(content);
      } else {
        message.error("Gagal mengambil data");
      }
    } catch (error) {
      message.error("Terjadi kesalahan: " + error.message);
    }
  };

  const handleEditLinguisticValue = (row) => {
    setCurrentRowData({ ...row });
    setEditLinguisticValueModalVisible(true);
  };

  const handleDeleteLinguisticValue = async (row) => {
    const { id } = row;
    if (id === "admin") {
      message.error("Tidak dapat menghapus Admin!");
      return;
    }

    try {
      await deleteLinguisticValue({ id });
      message.success("Berhasil dihapus");
      fetchLinguisticValues();
    } catch (error) {
      message.error("Gagal menghapus: " + error.message);
    }
  };

  const handleCancel = () => {
    setEditLinguisticValueModalVisible(false);
    setAddLinguisticValueModalVisible(false);
  };

  const handleAddLinguisticValue = () => {
    setAddLinguisticValueModalVisible(true);
  };

  const handleAddLinguisticValueOk = () => {
    const form = addLinguisticValueFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setAddLinguisticValueModalLoading(true);
      const { file, ...otherValues } = values;

      const formData = new FormData();
      if (file && file.fileList.length > 0) {
        formData.append("file", file.fileList[0].originFileObj);
      }
      formData.append("name", otherValues.name);
      formData.append("value1", otherValues.value1);
      formData.append("value2", otherValues.value2);
      formData.append("value3", otherValues.value3);
      formData.append("value4", otherValues.value4);

      addLinguisticValue(formData)
        .then(() => {
          form.resetFields();
          setAddLinguisticValueModalVisible(false);
          setAddLinguisticValueModalLoading(false);
          message.success("Berhasil ditambahkan!");
          fetchLinguisticValues();
        })
        .catch((error) => {
          setAddLinguisticValueModalLoading(false);
          message.error("Gagal menambahkan: " + error.message);
        });
    });
  };

  const handleEditLinguisticValueOk = () => {
    const form = editLinguisticValueFormRef.current?.props.form;
    form.validateFields((err, values) => {
      if (err) {
        message.error("Harap isi semua field yang diperlukan");
        return;
      }

      setEditLinguisticValueModalLoading(true);
      editLinguisticValue(values, currentRowData.id)
        .then(() => {
          form.resetFields();
          setEditLinguisticValueModalVisible(false);
          setEditLinguisticValueModalLoading(false);
          message.success("Berhasil diubah!");
          fetchLinguisticValues();
        })
        .catch((error) => {
          setEditLinguisticValueModalLoading(false);
          message.error("Gagal mengubah: " + error.message);
        });
    });
  };

  useEffect(() => {
    fetchLinguisticValues();
  }, []);

  const sortedLinguisticValues = [...linguisticValues].sort(
    (a, b) => a.avg - b.avg
  );

  const title = (
    <span>
      <Button type="primary" onClick={handleAddLinguisticValue}>
        Tambah Nilai Dari Tabel Linguistic Untuk IVIHV
      </Button>
    </span>
  );

  return (
    <div className="app-container">
      <TypingCard source="Disini anda dapat menambahkan linguistic value beserta gambar yang anda iginkan" />
      <Card title={title}>
        <Table dataSource={sortedLinguisticValues} rowKey="id">
          <Column
            title="ID"
            key="id"
            align="center"
            render={(value, record, index) => index + 1}
          />
          <Column
            title="Name"
            dataIndex="name"
            key="name"
            render={(text, record) => {
              if (record.file_path) {
                return (
                  <>
                    {text}
                    <img
                      src={`${BASE_URL}${record.file_path}`}
                      alt={text}
                      style={{
                        width: "200px",
                        height: "200px",
                        marginLeft: "10px",
                      }}
                    />
                  </>
                );
              }
              return text;
            }}
          />
          <Column title="Value 1" dataIndex="value1" key="value1" />
          <Column title="Value 2" dataIndex="value2" key="value2" />
          <Column title="Value 3" dataIndex="value3" key="value3" />
          <Column title="Value 4" dataIndex="value4" key="value4" />
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
                  onClick={() => handleEditLinguisticValue(row)}
                />
                <Divider type="vertical" />
                <Button
                  type="primary"
                  shape="circle"
                  icon="delete"
                  title="menghapus"
                  onClick={() => handleDeleteLinguisticValue(row)}
                />
              </span>
            )}
          />
        </Table>
      </Card>

      <EditLinguisticValueForm
        wrappedComponentRef={editLinguisticValueFormRef}
        visible={editLinguisticValueModalVisible}
        confirmLoading={editLinguisticValueModalLoading}
        onCancel={handleCancel}
        onOk={handleEditLinguisticValueOk}
        currentRowData={currentRowData}
        file_path={currentRowData.file_path}
      />

      <AddLinguisticValueForm
        wrappedComponentRef={addLinguisticValueFormRef}
        visible={addLinguisticValueModalVisible}
        confirmLoading={addLinguisticValueModalLoading}
        onCancel={handleCancel}
        onOk={handleAddLinguisticValueOk}
      />
    </div>
  );
};

export default LinguisticValue;
