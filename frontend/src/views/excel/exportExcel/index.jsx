/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import {
  Table,
  Tag,
  Form,
  Button,
  Input,
  Radio,
  Select,
  message,
  Collapse,
} from "antd";
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

const Excel = () => {
  const [list, setList] = useState([]);
  const [filename, setFilename] = useState("excel-file");
  const [autoWidth, setAutoWidth] = useState(true);
  const [bookType, setBookType] = useState("xlsx");
  const [downloadLoading, setDownloadLoading] = useState(false);
  const [selectedRows, setSelectedRows] = useState([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const fetchData = async () => {
    try {
      const response = await excelList();
      const items = response.data.data.items;
      setList(items);
    } catch (error) {
      message.error("Gagal mengambil data");
    }
  };

  useEffect(() => {
    let mounted = true;
    const getData = async () => {
      try {
        const response = await excelList();
        if (mounted) {
          setList(response.data.data.items);
        }
      } catch (error) {
        if (mounted) {
          message.error("Gagal mengambil data");
        }
      }
    };
    getData();
    return () => {
      mounted = false;
    };
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
      const excel = await import("@/lib/Export2Excel");
      const tHeader = ["Id", "Title", "Author", "Readings", "Date"];
      const filterVal = ["id", "title", "author", "readings", "date"];
      const data = type === "all" ? list : selectedRows;
      const formattedData = formatJson(filterVal, data);

      excel.export_json_to_excel({
        header: tHeader,
        data: formattedData,
        filename,
        autoWidth,
        bookType,
      });

      setSelectedRowKeys([]);
      message.success("Berhasil mengekspor data");
    } catch (error) {
      message.error("Gagal mengekspor data");
    } finally {
      setDownloadLoading(false);
    }
  };

  const formatJson = (filterVal, jsonData) => {
    return jsonData.map((v) => filterVal.map((j) => v[j]));
  };

  const handleFilenameChange = (e) => {
    setFilename(e.target.value);
  };

  const handleAutoWidthChange = (e) => {
    setAutoWidth(e.target.value);
  };

  const handleBookTypeChange = (value) => {
    setBookType(value);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  return (
    <div className="app-container">
      <Collapse defaultActiveKey={["1"]}>
        <Panel header="Export Options" key="1">
          <Form layout="inline">
            <Form.Item label="Nama File:">
              <Input
                style={{ width: "250px" }}
                prefix={<FileOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
                placeholder="Masukkan nama file (default: excel-file)"
                onChange={handleFilenameChange}
              />
            </Form.Item>
            <Form.Item label="Auto Width:">
              <Radio.Group onChange={handleAutoWidthChange} value={autoWidth}>
                <Radio value={true}>Ya</Radio>
                <Radio value={false}>Tidak</Radio>
              </Radio.Group>
            </Form.Item>
            <Form.Item label="Tipe File:">
              <Select
                defaultValue="xlsx"
                style={{ width: 120 }}
                onChange={handleBookTypeChange}
              >
                <Select.Option value="xlsx">xlsx</Select.Option>
                <Select.Option value="csv">csv</Select.Option>
                <Select.Option value="txt">txt</Select.Option>
              </Select>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                icon="file-excel"
                onClick={() => handleDownload("all")}
              >
                Export Semua
              </Button>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                icon="file-excel"
                onClick={() => handleDownload("selected")}
              >
                Export yang Dipilih
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

export default Excel;
