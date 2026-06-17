/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { Table, Tag } from "antd";
import { transactionList } from "@/api/remoteSearch";

const columns = [
  {
    title: "Order_No",
    dataIndex: "order_no",
    key: "order_no",
    width: 200,
  },
  {
    title: "Price",
    dataIndex: "price",
    key: "price",
    width: 195,
    render: (text) => `$${text}`,
  },
  {
    title: "Status",
    key: "tag",
    dataIndex: "tag",
    width: 100,
    render: (tag) => (
      <Tag color={tag === "pending" ? "magenta" : "green"} key={tag}>
        {tag}
      </Tag>
    ),
  },
];

const TransactionTable = () => {
  const [list, setList] = useState([]);

  useEffect(() => {
    let mounted = true;

    const fetchData = async () => {
      try {
        const response = await transactionList();
        if (mounted) {
          const data = response.data.data.items.slice(0, 13);
          setList(data);
        }
      } catch (error) {
        console.error("Error fetching transaction list:", error);
      }
    };

    fetchData();

    return () => {
      mounted = false;
    };
  }, []);

  return <Table columns={columns} dataSource={list} pagination={false} />;
};

export default TransactionTable;
