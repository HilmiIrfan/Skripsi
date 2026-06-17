/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Table, Tag, Form, Button, Input, message, Collapse } from "antd";
import { FileOutlined } from "@ant-design/icons";
import { excelList } from "@/api/excel";

const { Panel } = Collapse;

const columns = [
  {
    title: "Id",
    dataIndex: "id",
    key: "id",
    width: 200,
    align: "center",
  },
  {
    title: "Title",
    dataIndex: "title",
    key: "title",
    width: 200,
    align: "center",
  },
  {
    title: "Author",
    key: "author",
    dataIndex: "author",
    width: 100,
    align: "center",
    render: (author) => <Tag key={author}>{author}</Tag>,
  },
  {
    title: "Readings",
    dataIndex: "readings",
    key: "readings",
    width: 195,
    align: "center",
  },
  {
    title: "Date",
    dataIndex: "date",
    key: "date",
    width: 195,
    align: "center",
  },
];

const Zip = () => {
  const [list, setList] = useState([]);
  const [filename, setFilename] = useState("file");
  const [downloadLoading, setDownloadLoading] = useState(false);
  const [selectedRows, setSelectedRows] = useState([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const fetchData = async () => {
    try {
      const response = await excelList();
      setList(response.data.data.items);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const onSelectChange = (selectedRowKeys, selectedRows) => {
    setSelectedRows(selectedRows);
    setSelectedRowKeys(selectedRowKeys);
  };

  const handleDownload = async (type) => {
    if (type === "selected" && selectedRowKeys.length === 0) {
      message.error("Pilih minimal satu item untuk diekspor");
      return;
    }

    setDownloadLoading(true);

    try {
      const zip = await import("@/lib/Export2Zip");
      const tHeader = ["Id", "Title", "Author", "Readings", "Date"];
      const filterVal = ["id", "title", "author", "readings", "date"];
      const data = formatJson(filterVal, type === "all" ? list : selectedRows);

      zip.export_txt_to_zip(tHeader, data, filename, filename);
      setSelectedRowKeys([]);
    } catch (error) {
      console.error("Error downloading:", error);
      message.error("Gagal mengekspor file");
    }

    setDownloadLoading(false);
  };

  const formatJson = (filterVal, jsonData) => {
    return jsonData.map((v) => filterVal.map((j) => v[j]));
  };

  const handleFilenameChange = (e) => {
    setFilename(e.target.value);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  return (
    <div className="app-container">
      <Collapse defaultActiveKey={["1"]}>
        <Panel header="Opsi Ekspor" key="1">
          <Form layout="inline">
            <Form.Item label="Nama File:">
              <Input
                style={{ width: "250px" }}
                prefix={<FileOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
                placeholder="Masukkan nama file (default: file)"
                onChange={handleFilenameChange}
              />
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                icon="file-zip"
                onClick={() => handleDownload("all")}
              >
                Ekspor Semua
              </Button>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                icon="file-zip"
                onClick={() => handleDownload("selected")}
              >
                Ekspor Item Terpilih
              </Button>
            </Form.Item>
          </Form>
        </Panel>
      </Collapse>
      <br />
      <Table
        variant
        columns={columns}
        rowKey={(record) => record.id}
        dataSource={list}
        pagination={false}
        rowSelection={rowSelection}
        loading={downloadLoading}
      />
    </div>
  );
};

export default Zip;
