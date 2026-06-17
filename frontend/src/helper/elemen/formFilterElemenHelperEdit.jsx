/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Form, Select } from "antd";

export const useFormFilterEditElemen = (currentRowData, initialData) => {
  const [filterState, setFilterState] = useState({
    tahunAjaranList: [],
    semesterList: [],
    kelasList: [],
    mapelList: [],
    elemenList: [],
    availableSemesters: [],
    availableKelas: [],
    availableMapels: [],
    availableElemen: [],
  });

  // Inisialisasi data
  useEffect(() => {
    if (initialData && currentRowData) {
      const {
        tahunAjaranList,
        semesterList,
        kelasList,
        mapelList,
        elemenList,
      } = initialData;

      const availableSemesters = getAvailableSemesters(
        currentRowData?.tahunAjaran?.idTahun,
        semesterList,
        mapelList,
        elemenList
      );

      const availableKelas = getAvailableKelas(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        kelasList,
        mapelList,
        elemenList
      );

      const availableMapels = getAvailableMapels(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        mapelList,
        elemenList
      );

      const availableElemen = getAvailableElemen(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        elemenList
      );

      setFilterState({
        tahunAjaranList,
        semesterList,
        kelasList,
        mapelList,
        elemenList,
        availableSemesters,
        availableKelas,
        availableMapels,
        availableElemen,
      });
    }
  }, [initialData, currentRowData]);

  const getAvailableSemesters = (tahunAjaranId, semesterList, elemenList) => {
    if (!tahunAjaranId) return [];
    return semesterList.filter((semester) =>
      elemenList.some(
        (elemen) =>
          elemen.tahunAjaran?.idTahun === tahunAjaranId &&
          elemen.semester?.idSemester === semester.idSemester
      )
    );
  };

  const getAvailableKelas = (
    tahunAjaranId,
    semesterId,
    kelasList,
    elemenList
  ) => {
    if (!tahunAjaranId || !semesterId) return [];

    return kelasList.filter((kelas) =>
      elemenList.some(
        (elemen) =>
          elemen.tahunAjaran?.idTahun === tahunAjaranId &&
          elemen.semester?.idSemester === semesterId &&
          elemen.kelas?.idKelas === kelas.idKelas
      )
    );
  };

  const getAvailableMapels = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelList,
    elemenList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId) return [];

    // Menghapus duplikat berdasarkan nama mapel
    const filtered = mapelList.filter(
      (mapel) =>
        mapel.tahunAjaran?.idTahun === tahunAjaranId &&
        mapel.semester?.idSemester === semesterId &&
        mapel.kelas?.idKelas === kelasId
    );

    // Menghapus duplikat berdasarkan nama mapel
    return filtered.reduce((acc, current) => {
      const x = acc.find((item) => item.name === current.name);
      if (!x) return acc.concat([current]);
      return acc;
    }, []);
  };

  const getAvailableElemen = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelId,
    elemenList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId || !mapelId) return [];

    const filtered = elemenList.filter(
      (elemen) =>
        elemen.tahunAjaran?.idTahun === tahunAjaranId &&
        elemen.semester?.idSemester === semesterId &&
        elemen.kelas?.idKelas === kelasId &&
        elemen.mapel?.idMapel === mapelId
    );

    // Menghapus duplikat berdasarkan nama elemen
    return filtered.reduce((acc, current) => {
      const x = acc.find((item) => item.namaElemen === current.namaElemen);
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
            idElemen: null,
          });
          const availableSemesters = getAvailableSemesters(
            idTahun,
            filterState.semesterList,
            filterState.elemenList
          );
          setFilterState((prev) => ({
            ...prev,
            availableSemesters,
            availableKelas: [],
            availableMapels: [],
            availableElemen: [],
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
              idElemen: null,
            });
            const availableKelas = getAvailableKelas(
              form.getFieldValue("idTahun"),
              idSemester,
              filterState.kelasList,
              filterState.elemenList
            );
            setFilterState((prev) => ({
              ...prev,
              availableKelas,
              availableMapels: [],
              availableElemen: [],
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
            form.setFieldsValue({ idMapel: null, idElemen: null });
            const availableMapels = getAvailableMapels(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              idKelas,
              filterState.mapelList,
              filterState.elemenList
            );
            setFilterState((prev) => ({
              ...prev,
              availableMapels,
              availableElemen: [],
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

  const renderMapelSelect = (form) => {
    const selectedTahun = form.getFieldValue("idTahun");
    const selectedSemester = form.getFieldValue("idSemester");
    const selectedKelas = form.getFieldValue("idKelas");
    return (
      <Form.Item
        label="Mapel"
        name="idMapel"
        rules={[{ required: true, message: "Silahkan pilih Mapel" }]}
      >
        <Select
          placeholder="Pilih Mapel"
          disabled={!selectedTahun || !selectedSemester || !selectedKelas}
          onChange={(idMapel) => {
            form.setFieldsValue({ idElemen: null });
            const availableElemen = getAvailableElemen(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              form.getFieldValue("idKelas"),
              idMapel,
              filterState.elemenList
            );
            setFilterState((prev) => ({
              ...prev,
              availableElemen,
            }));
          }}
        >
          {filterState.availableMapels.map(({ idMapel, name }) => (
            <Select.Option key={idMapel} value={idMapel}>
              {name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
    );
  };

  const renderElemenSelect = (form) => (
    <Form.Item
      label="Elemen"
      name="idElemen"
      rules={[{ required: true, message: "Silahkan pilih Elemen" }]}
    >
      <Select
        placeholder="Pilih Elemen"
        disabled={filterState.availableElemen.length === 0}
      >
        {filterState.availableElemen.map(({ idElemen, namaElemen }) => (
          <Select.Option key={idElemen} value={idElemen}>
            {namaElemen}
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
    renderElemenSelect,
  };
};
