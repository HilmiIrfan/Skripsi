/* eslint-disable no-unused-vars */
import {
  Card,
  Select,
  Steps,
  Button,
  Space,
  Alert,
  Tag,
  Row,
  Col,
  message,
} from "antd";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { getMapel } from "@/api/mapel";
import { getKelas } from "@/api/kelas";
import { getSemester } from "@/api/semester";
import { getTahunAjaran } from "@/api/tahun-ajaran";
import React, { useState, useEffect, useRef, useCallback } from "react";

const { Step } = Steps;

// Fungsi untuk memuat data awal
export const fetchInitialData = async () => {
  try {
    const [tahunAjaranRes, semesterRes, kelasRes, mapelRes] = await Promise.all(
      [getTahunAjaran(), getSemester(), getKelas(), getMapel()]
    );

    return {
      tahunAjaranList: tahunAjaranRes.data.content || [],
      semesterList: semesterRes.data.content || [],
      kelasList: kelasRes.data.content || [],
      allMapelList: mapelRes.data.content || [],
    };
  } catch (error) {
    message.error("Gagal memuat data awal");
    return {
      tahunAjaranList: [],
      semesterList: [],
      kelasList: [],
      allMapelList: [],
    };
  }
};

// Fungsi untuk memfilter semester yang tersedia
export const getAvailableSemesters = (
  selectedTahunAjaran,
  semesterList,
  allMapelList
) => {
  if (!selectedTahunAjaran) return [];

  return semesterList.filter((semester) =>
    allMapelList.some(
      (mapel) =>
        mapel.tahunAjaran?.idTahun === selectedTahunAjaran &&
        mapel.semester?.idSemester === semester.idSemester
    )
  );
};

// Fungsi untuk memfilter kelas yang tersedia
export const getAvailableKelas = (
  selectedTahunAjaran,
  selectedSemester,
  kelasList,
  allMapelList
) => {
  if (!selectedTahunAjaran || !selectedSemester) return [];

  return kelasList.filter((kelas) =>
    allMapelList.some(
      (mapel) =>
        mapel.tahunAjaran?.idTahun === selectedTahunAjaran &&
        mapel.semester?.idSemester === selectedSemester &&
        mapel.kelas?.idKelas === kelas.idKelas
    )
  );
};

// Fungsi untuk memfilter mapel yang tersedia
export const getAvailableMapels = (
  selectedTahunAjaran,
  selectedSemester,
  selectedKelas,
  allMapelList
) => {
  if (!selectedTahunAjaran || !selectedSemester || !selectedKelas) return [];

  const filteredMapels = allMapelList.filter(
    (mapel) =>
      mapel.tahunAjaran?.idTahun === selectedTahunAjaran &&
      mapel.semester?.idSemester === selectedSemester &&
      mapel.kelas?.idKelas === selectedKelas
  );

  // Buat unique berdasarkan nama mapel
  return filteredMapels.reduce((acc, current) => {
    const x = acc.find((item) => item.name === current.name);
    if (!x) return acc.concat([current]);
    return acc;
  }, []);
};

// Fungsi untuk memfilter data berdasarkan seleksi
export const filterData = (
  elemen,
  selectedTahunAjaran,
  selectedSemester,
  selectedKelas,
  selectedMapel
) => {
  return elemen.filter(
    (item) =>
      item?.tahunAjaran?.idTahun === selectedTahunAjaran &&
      item?.semester?.idSemester === selectedSemester &&
      item?.kelas?.idKelas === selectedKelas &&
      (!selectedMapel || item?.mapel?.idMapel === selectedMapel)
  );
};

// Fungsi untuk render selection steps
export const renderSelectionSteps = ({
  currentStep,
  tahunAjaranList,
  semesterList,
  kelasList,
  availableSemesters,
  availableKelas,
  availableMapels,
  selectedTahunAjaran,
  selectedSemester,
  selectedKelas,
  onTahunAjaranChange,
  onSemesterChange,
  onKelasChange,
  onMapelChange,
  onStepBack,
}) => {
  return (
    <Card style={{ maxWidth: 600, margin: "20px auto" }}>
      <Steps current={currentStep - 1} style={{ marginBottom: 24 }}>
        <Step title="Tahun Ajaran" />
        <Step title="Semester" />
        <Step title="Kelas" />
        <Step title="Mata Pelajaran" />
      </Steps>

      <div style={{ minHeight: 150 }}>
        {/* Step 1: Pilih Tahun Ajaran */}
        {currentStep === 1 && (
          <Select
            style={{ width: "100%" }}
            placeholder="Pilih Tahun Ajaran"
            onChange={onTahunAjaranChange}
            options={tahunAjaranList.map((tahunAjaran) => ({
              value: tahunAjaran.idTahun,
              label: tahunAjaran.tahunAjaran,
            }))}
          />
        )}

        {currentStep === 2 && (
          <Space direction="vertical" style={{ width: "100%" }}>
            <Button
              type="text"
              icon={<ArrowLeftOutlined />}
              onClick={() => onStepBack(1)}
              style={{ marginBottom: 16 }}
            >
              Kembali ke Pilih Tahun Ajaran
            </Button>
            <Select
              style={{ width: "100%" }}
              placeholder="Pilih Semester"
              onChange={onSemesterChange}
              disabled={!selectedTahunAjaran || availableSemesters.length === 0}
              options={availableSemesters.map((semester) => ({
                value: semester.idSemester,
                label: semester.namaSemester,
              }))}
            />
            {availableSemesters.length === 0 && selectedTahunAjaran && (
              <Alert
                message="Tidak ada semester tersedia untuk tahun ajaran ini"
                type="info"
              />
            )}
          </Space>
        )}

        {currentStep === 3 && (
          <Space direction="vertical" style={{ width: "100%" }}>
            <Button
              type="text"
              icon={<ArrowLeftOutlined />}
              onClick={() => onStepBack(2)}
              style={{ marginBottom: 16 }}
            >
              Kembali ke Pilih Semester
            </Button>
            <Select
              style={{ width: "100%" }}
              placeholder="Pilih Kelas"
              onChange={onKelasChange}
              disabled={!selectedSemester || availableKelas.length === 0}
              options={availableKelas.map((kelas) => ({
                value: kelas.idKelas,
                label: kelas.namaKelas,
              }))}
            />
            {availableKelas.length === 0 && selectedSemester && (
              <Alert
                message="Tidak ada kelas tersedia untuk semester ini"
                type="info"
              />
            )}
          </Space>
        )}

        {currentStep === 4 && (
          <Space direction="vertical" style={{ width: "100%" }}>
            <Button
              type="text"
              icon={<ArrowLeftOutlined />}
              onClick={() => onStepBack(3)}
              style={{ marginBottom: 16 }}
            >
              Kembali ke Pilih Kelas
            </Button>
            <Select
              style={{ width: "100%" }}
              placeholder="Pilih Mata Pelajaran"
              onChange={onMapelChange}
              disabled={!selectedKelas || availableMapels.length === 0}
              options={availableMapels.map((mapel) => ({
                value: mapel.idMapel,
                label: mapel.name,
              }))}
            />
            {availableMapels.length === 0 && selectedKelas && (
              <Alert
                message="Tidak ada mata pelajaran tersedia untuk kelas ini"
                type="info"
              />
            )}
          </Space>
        )}
      </div>
    </Card>
  );
};

// Fungsi untuk render info filter aktif
export const renderActiveFilters = ({
  tahunAjaranList,
  semesterList,
  kelasList,
  filteredMapelList,
  selectedTahunAjaran,
  selectedSemester,
  selectedKelas,
  selectedMapel,
  onBackClick,
}) => {
  return (
    <Card style={{ marginBottom: 16 }}>
      <Row justify="space-between" align="middle">
        <Col>
          <Button
            type="text"
            icon={<ArrowLeftOutlined />}
            onClick={onBackClick}
          >
            Kembali
          </Button>
        </Col>
        <Col>
          <Space>
            <Tag color="purple">
              Tahun Ajaran:{" "}
              {tahunAjaranList.find((k) => k.idTahun === selectedTahunAjaran)
                ?.tahunAjaran || "-"}
            </Tag>
            <Tag color="geekblue">
              Semester:{" "}
              {semesterList.find((s) => s.idSemester === selectedSemester)
                ?.namaSemester || "-"}
            </Tag>
            <Tag color="blue">
              Kelas:{" "}
              {kelasList.find((k) => k.idKelas === selectedKelas)?.namaKelas ||
                "-"}
            </Tag>
            {/* <Tag color="red">
              Mapel:{" "}
              {filteredMapelList.find((m) => m.idMapel === selectedMapel)
                ?.name || "-"}
            </Tag> */}
          </Space>
        </Col>
      </Row>
    </Card>
  );
};
