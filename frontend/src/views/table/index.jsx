/* eslint-disable no-unused-vars */
import React, { useState, useEffect, useRef } from "react";
import {
  Table,
  Tag,
  Form,
  Button,
  Input,
  Collapse,
  Pagination,
  Divider,
  message,
  Select,
} from "antd";
import { tableList, deleteItem, editItem } from "@/api/table";
import EditForm from "./forms/editForm";

const { Column } = Table;
const { Panel } = Collapse;

const TableComponent = () => {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [listQuery, setListQuery] = useState({
    pageNumber: 1,
    pageSize: 10,
    title: "",
    star: "",
    status: "",
  });
  const [modalState, setModalState] = useState({
    editVisible: false,
    editLoading: false,
  });
  const [currentRowData, setCurrentRowData] = useState({
    id: 0,
    author: "",
    date: "",
    readings: 0,
    star: "★",
    status: "published",
    title: "",
  });

  const formRef = useRef();

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await tableList(listQuery);
      const { items, total } = response.data.data;
      setList(items);
      setTotal(total);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, [listQuery]);

  const handleFiltersChange = (type, value) => {
    setListQuery((prev) => ({
      ...prev,
      [type]: typeof value === "object" ? value.target.value : value,
    }));
  };

  const handleChangePage = (pageNumber, pageSize) => {
    setListQuery((prev) => ({
      ...prev,
      pageNumber,
      pageSize: pageSize || prev.pageSize,
    }));
  };

  const handleDelete = async (row) => {
    try {
      await deleteItem({ id: row.id });
      message.success("Berhasil dihapus");
      fetchData();
    } catch (error) {
      message.error("Gagal menghapus");
    }
  };

  const handleEdit = (row) => {
    setCurrentRowData({ ...row });
    setModalState((prev) => ({ ...prev, editVisible: true }));
  };

  const handleOk = () => {
    const form = formRef.current?.props.form;
    form?.validateFields(async (err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        star: "".padStart(fieldsValue["star"], "★"),
        date: fieldsValue["date"].format("YYYY-MM-DD HH:mm:ss"),
      };

      setModalState((prev) => ({ ...prev, editLoading: true }));
      try {
        await editItem(values);
        form.resetFields();
        setModalState({ editVisible: false, editLoading: false });
        message.success("Berhasil diperbarui!");
        fetchData();
      } catch (error) {
        message.error("Gagal memperbarui");
        setModalState((prev) => ({ ...prev, editLoading: false }));
      }
    });
  };

  const handleCancel = () => {
    setModalState((prev) => ({ ...prev, editVisible: false }));
  };

  return (
    <div className="app-container">
      <Collapse defaultActiveKey={["1"]}>
        <Panel header="Filter" key="1">
          <Form layout="inline">
            <Form.Item label="Judul:">
              <Input onChange={(e) => handleFiltersChange("title", e)} />
            </Form.Item>
            <Form.Item label="Tipe:">
              <Select
                style={{ width: 120 }}
                onChange={(value) => handleFiltersChange("status", value)}
              >
                <Select.Option value="published">published</Select.Option>
                <Select.Option value="draft">draft</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item label="Rating:">
              <Select
                style={{ width: 120 }}
                onChange={(value) => handleFiltersChange("star", value)}
              >
                <Select.Option value={1}>★</Select.Option>
                <Select.Option value={2}>★★</Select.Option>
                <Select.Option value={3}>★★★</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item>
              <Button type="primary" icon="search" onClick={fetchData}>
                Cari
              </Button>
            </Form.Item>
          </Form>
        </Panel>
      </Collapse>
      <br />
      <Table
        variant
        rowKey={(record) => record.id}
        dataSource={list}
        loading={loading}
        pagination={false}
      >
        <Column
          title="No"
          dataIndex="id"
          align="center"
          sorter={(a, b) => a.id - b.id}
        />
        <Column title="Judul" dataIndex="title" align="center" />
        <Column title="Penulis" dataIndex="author" align="center" />
        <Column title="Dibaca" dataIndex="readings" align="center" />
        <Column title="Rating" dataIndex="star" align="center" />
        <Column
          title="Status"
          dataIndex="status"
          align="center"
          render={(status) => {
            const color =
              status === "published"
                ? "green"
                : status === "deleted"
                ? "red"
                : "";
            return <Tag color={color}>{status}</Tag>;
          }}
        />
        <Column title="Waktu" dataIndex="date" align="center" />
        <Column
          title="Operasi"
          key="action"
          align="center"
          render={(_, row) => (
            <span>
              <Button
                type="primary"
                shape="circle"
                icon="edit"
                title="edit"
                onClick={() => handleEdit(row)}
              />
              <Divider type="vertical" />
              <Button
                type="primary"
                shape="circle"
                icon="delete"
                title="hapus"
                onClick={() => handleDelete(row)}
              />
            </span>
          )}
        />
      </Table>
      <br />
      <Pagination
        total={total}
        pageSizeOptions={["10", "20", "40"]}
        showTotal={(total) => `Total ${total} data`}
        onChange={handleChangePage}
        current={listQuery.pageNumber}
        onShowSizeChange={handleChangePage}
        showSizeChanger
        showQuickJumper
        hideOnSinglePage={true}
      />
      <EditForm
        wrappedComponentRef={formRef}
        currentRowData={currentRowData}
        visible={modalState.editVisible}
        confirmLoading={modalState.editLoading}
        onCancel={handleCancel}
        onOk={handleOk}
      />
    </div>
  );
};

export default TableComponent;
