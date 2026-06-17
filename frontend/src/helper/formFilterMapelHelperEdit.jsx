/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Form, Select } from "antd";

export const useFormFilterEdit = (currentRowData, initialData) => {
  const [filterState, setFilterState] = useState({
    tahunAjaranList: [],
    semesterList: [],
    kelasList: [],
    mapelList: [],
    availableSemesters: [],
    availableKelas: [],
    availableMapels: [],
  });

  // Inisialisasi data
  useEffect(() => {
    if (initialData && currentRowData) {
      const { tahunAjaranList, semesterList, kelasList, mapelList } =
        initialData;

      const availableSemesters = getAvailableSemesters(
        currentRowData?.tahunAjaran?.idTahun,
        semesterList,
        mapelList
      );

      const availableKelas = getAvailableKelas(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        kelasList,
        mapelList
      );

      const availableMapels = getAvailableMapels(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        mapelList
      );

      setFilterState({
        tahunAjaranList,
        semesterList,
        kelasList,
        mapelList,
        availableSemesters,
        availableKelas,
        availableMapels,
      });
    }
  }, [initialData, currentRowData]);

  const getAvailableSemesters = (tahunAjaranId, semesterList, mapelList) => {
    if (!tahunAjaranId) return [];
    return semesterList.filter((semester) =>
      mapelList.some(
        (mapel) =>
          mapel.tahunAjaran?.idTahun === tahunAjaranId &&
          mapel.semester?.idSemester === semester.idSemester
      )
    );
  };

  const getAvailableKelas = (
    tahunAjaranId,
    semesterId,
    kelasList,
    mapelList
  ) => {
    if (!tahunAjaranId || !semesterId) return [];

    return kelasList.filter((kelas) =>
      mapelList.some(
        (mapel) =>
          mapel.tahunAjaran?.idTahun === tahunAjaranId &&
          mapel.semester?.idSemester === semesterId &&
          mapel.kelas?.idKelas === kelas.idKelas
      )
    );
  };

  const getAvailableMapels = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId) return [];

    const filtered = mapelList.filter(
      (mapel) =>
        mapel.tahunAjaran?.idTahun === tahunAjaranId &&
        mapel.semester?.idSemester === semesterId &&
        mapel.kelas?.idKelas === kelasId
    );

    return filtered.reduce((acc, current) => {
      const x = acc.find((item) => item.name === current.name);
      if (!x) return acc.concat([current]);
      return acc;
    }, []);
  };

  const renderTahunAjaranSelect = (form) => (
    <Form.Item
      label="Tahun Ajaran"
      name="idTahun"
      rules={[{ required: true, message: "Silahkan pilih Tahun Ajaran" }]}
    >
      <Select
        placeholder="Pilih Tahun Ajaran"
        onChange={(idTahun) => {
          form.setFieldsValue({
            idSemester: null,
            idKelas: null,
            idMapel: null,
          });
          const availableSemesters = getAvailableSemesters(
            idTahun,
            filterState.semesterList,
            filterState.mapelList
          );
          setFilterState((prev) => ({
            ...prev,
            availableSemesters,
            availableKelas: [],
            availableMapels: [],
          }));
        }}
      >
        {filterState.tahunAjaranList.map(({ idTahun, tahunAjaran }) => (
          <Select.Option key={idTahun} value={idTahun}>
            {tahunAjaran}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderSemesterSelect = (form) => {
    const selectedTahun = form.getFieldValue("idTahun");
    return (
      <Form.Item
        label="Semester"
        name="idSemester"
        rules={[{ required: true, message: "Silahkan pilih Semester" }]}
      >
        <Select
          placeholder="Pilih Semester"
          disabled={!selectedTahun}
          onChange={(idSemester) => {
            form.setFieldsValue({
              idKelas: null,
              idMapel: null,
            });
            const availableKelas = getAvailableKelas(
              form.getFieldValue("idTahun"),
              idSemester,
              filterState.kelasList,
              filterState.mapelList
            );
            setFilterState((prev) => ({
              ...prev,
              availableKelas,
              availableMapels: [],
            }));
          }}
        >
          {filterState.availableSemesters.map(
            ({ idSemester, namaSemester }) => (
              <Select.Option key={idSemester} value={idSemester}>
                {namaSemester}
              </Select.Option>
            )
          )}
        </Select>
      </Form.Item>
    );
  };

  const renderKelasSelect = (form) => {
    const selectedTahun = form.getFieldValue("idTahun");
    const selectedSemester = form.getFieldValue("idSemester");
    return (
      <Form.Item
        label="Kelas"
        name="idKelas"
        rules={[{ required: true, message: "Silahkan pilih Kelas" }]}
      >
        <Select
          placeholder="Pilih Kelas"
          disabled={!selectedTahun || !selectedSemester}
          onChange={(idKelas) => {
            form.setFieldsValue({ idMapel: null });
            const availableMapels = getAvailableMapels(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              idKelas,
              filterState.mapelList
            );
            setFilterState((prev) => ({
              ...prev,
              availableMapels,
            }));
          }}
        >
          {filterState.availableKelas.map(({ idKelas, namaKelas }) => (
            <Select.Option key={idKelas} value={idKelas}>
              {namaKelas}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
    );
  };

  const renderMapelSelect = (form) => (
    <Form.Item
      label="Mapel"
      name="idMapel"
      rules={[{ required: true, message: "Silahkan pilih Mapel" }]}
    >
      <Select
        placeholder="Pilih Mapel"
        disabled={filterState.availableMapels.length === 0}
      >
        {filterState.availableMapels.map(({ idMapel, name }) => (
          <Select.Option key={idMapel} value={idMapel}>
            {name}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  return {
    filterState,
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
  };
};
