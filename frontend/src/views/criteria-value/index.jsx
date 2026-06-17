import { useState, useEffect } from "react";
import { Card, Button, Table, message, Divider } from "antd";
import { getRPS } from "@/api/rps";
import { getSubjects } from "@/api/subject";
import { getStudyPrograms } from "@/api/studyProgram";
import { getLectures } from "@/api/lecture";
import { Link } from "react-router-dom";
import {
  getLearningMediasSoftware,
  getLearningMediasHardware,
} from "@/api/learningMedia";
import TypingCard from "@/components/TypingCard";

const { Column } = Table;

const CriteriaValue = () => {
  const [rps, setRps] = useState([]);
  const [learningMediaSoftwares, setLearningMediaSoftwares] = useState([]);
  const [learningMediaHardwares, setLearningMediaHardwares] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [studyPrograms, setStudyPrograms] = useState([]);
  const [lectures, setLectures] = useState([]);

  const fetchRPS = async () => {
    const result = await getRPS();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setRps(content);
    }
  };

  const fetchLearningMediasSoftware = async () => {
    const result = await getLearningMediasSoftware();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setLearningMediaSoftwares(content);
    }
  };

  const fetchLearningMediasHardware = async () => {
    const result = await getLearningMediasHardware();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setLearningMediaHardwares(content);
    }
  };

  const fetchSubjects = async () => {
    const result = await getSubjects();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setSubjects(content);
    }
  };

  const fetchStudyProgram = async () => {
    const result = await getStudyPrograms();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setStudyPrograms(content);
    }
  };

  const fetchLectures = async () => {
    const result = await getLectures();
    const { content, statusCode } = result.data;
    if (statusCode === 200) {
      setLectures(content);
    }
  };

  useEffect(() => {
    fetchRPS();
    fetchSubjects();
    fetchLearningMediasHardware();
    fetchLearningMediasSoftware();
    fetchStudyProgram();
    fetchLectures();
  }, []);

  const title = (
    <span>
      <div>Pilih RPS yang akan dinilai soal soalnya</div>
    </span>
  );

  const cardContent = `Di sini, Anda dapat mengelola RPS sesuai dengan mata kuliah yang diampu. Di bawah ini dapat menampilkan list RPS yang ada.`;

  return (
    <div className="app-container">
      <TypingCard title="Manajemen RPS" source={cardContent} />
      <br />
      <Card title={title}>
        <Table variant rowKey="id" dataSource={rps} pagination={false}>
          <Column title="ID RPS" dataIndex="id" key="id" align="center" />
          <Column title="Nama" dataIndex="name" key="name" align="center" />
          <Column
            title="Semester"
            dataIndex="semester"
            key="semester"
            align="center"
          />
          <Column title="SKS" dataIndex="sks" key="sks" align="center" />
          <Column
            title="Mata Kuliah"
            dataIndex="subject.name"
            key="subject.name"
            align="center"
          />
          <Column
            title="Operasi"
            key="action"
            width={195}
            align="center"
            render={(text, row) => (
              <span>
                <Divider type="vertical" />
                <Link to={`/index/question/${row.id}`}>
                  <Button
                    type="primary"
                    shape="circle"
                    icon="diff"
                    title="menghapus"
                  />
                </Link>
                <Divider type="vertical" />
              </span>
            )}
          />
        </Table>
      </Card>
    </div>
  );
};

export default CriteriaValue;
