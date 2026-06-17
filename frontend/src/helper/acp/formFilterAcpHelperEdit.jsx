/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Form, Select } from "antd";

export const useFormFilterEditAcp = (currentRowData, initialData) => {
  const [filterState, setFilterState] = useState({
    tahunAjaranList: [],
    semesterList: [],
    kelasList: [],
    mapelList: [],
    elemenList: [],
    acpList: [],
    availableSemesters: [],
    availableKelas: [],
    availableMapels: [],
    availableElemen: [],
    availableAcp: [],
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
        acpList,
      } = initialData;

      const availableSemesters = getAvailableSemesters(
        currentRowData?.tahunAjaran?.idTahun,
        semesterList,
        mapelList,
        elemenList,
        acpList
      );

      const availableKelas = getAvailableKelas(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        kelasList,
        mapelList,
        elemenList,
        acpList
      );

      const availableMapels = getAvailableMapels(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        mapelList,
        elemenList,
        acpList
      );

      const availableElemen = getAvailableElemen(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        elemenList,
        acpList
      );

      const availableAcp = getAvailableAcp(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        currentRowData?.elemen?.idElemen,
        acpList
      );

      setFilterState({
        tahunAjaranList,
        semesterList,
        kelasList,
        mapelList,
        elemenList,
        acpList,
        availableSemesters,
        availableKelas,
        availableMapels,
        availableElemen,
        availableAcp,
      });
    }
  }, [initialData, currentRowData]);

  const getAvailableSemesters = (tahunAjaranId, semesterList, acpList) => {
    if (!tahunAjaranId) return [];
    return semesterList.filter((semester) =>
      acpList.some(
        (acp) =>
          acp.tahunAjaran?.idTahun === tahunAjaranId &&
          acp.semester?.idSemester === semester.idSemester
      )
    );
  };

  const getAvailableKelas = (tahunAjaranId, semesterId, kelasList, acpList) => {
    if (!tahunAjaranId || !semesterId) return [];

    return kelasList.filter((kelas) =>
      acpList.some(
        (acp) =>
          acp.tahunAjaran?.idTahun === tahunAjaranId &&
          acp.semester?.idSemester === semesterId &&
          acp.kelas?.idKelas === kelas.idKelas
      )
    );
  };

  const getAvailableMapels = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelList,
    acpList
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
    elemenList,
    acpList
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

  const getAvailableAcp = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelId,
    elemenId,
    acpList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId || !mapelId || !elemenId)
      return [];

    const filtered = acpList.filter(
      (acp) =>
        acp.tahunAjaran?.idTahun === tahunAjaranId &&
        acp.semester?.idSemester === semesterId &&
        acp.kelas?.idKelas === kelasId &&
        acp.mapel?.idMapel === mapelId &&
        acp.elemen?.idElemen === elemenId
    );

    // Menggunakan reduce untuk menghapus duplikat berdasarkan nama acp
    return filtered.reduce((acc, current) => {
      // Perbaikan: Menggunakan namaAcp bukan nama, dan memeriksa namaAcp yang sama
      const x = acc.find((item) => item.namaAcp === current.namaAcp);
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
            idAcp: null,
          });
          const availableSemesters = getAvailableSemesters(
            idTahun,
            filterState.semesterList,
            filterState.acpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableSemesters,
            availableKelas: [],
            availableMapels: [],
            availableElemen: [],
            availableAcp: [],
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
              idAcp: null,
            });
            const availableKelas = getAvailableKelas(
              form.getFieldValue("idTahun"),
              idSemester,
              filterState.kelasList,
              filterState.acpList
            );
            setFilterState((prev) => ({
              ...prev,
              availableKelas,
              availableMapels: [],
              availableElemen: [],
              availableAcp: [],
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
            form.setFieldsValue({ idMapel: null, idElemen: null, idAcp: null });
            const availableMapels = getAvailableMapels(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              idKelas,
              filterState.mapelList,
              filterState.acpList
            );
            setFilterState((prev) => ({
              ...prev,
              availableMapels,
              availableElemen: [],
              availableAcp: [],
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
            form.setFieldsValue({ idElemen: null, idAcp: null });
            const availableElemen = getAvailableElemen(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              form.getFieldValue("idKelas"),
              idMapel,
              filterState.elemenList,
              filterState.acpList
            );
            setFilterState((prev) => ({
              ...prev,
              availableElemen,
              availableAcp: [],
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

  const renderElemenSelect = (form) => {
    const selectedTahun = form.getFieldValue("idTahun");
    const selectedSemester = form.getFieldValue("idSemester");
    const selectedKelas = form.getFieldValue("idKelas");
    const selectedMapel = form.getFieldValue("idMapel");
    return (
      <Form.Item
        label="Elemen"
        name="idElemen"
        rules={[{ required: true, message: "Silahkan pilih Elemen" }]}
      >
        <Select
          placeholder="Pilih Elemen"
          disabled={
            !selectedTahun ||
            !selectedSemester ||
            !selectedKelas ||
            !selectedMapel
          }
          onChange={(idElemen) => {
            form.setFieldsValue({ idAcp: null });
            const availableAcp = getAvailableAcp(
              form.getFieldValue("idTahun"),
              form.getFieldValue("idSemester"),
              form.getFieldValue("idKelas"),
              form.getFieldValue("idMapel"),
              idElemen,
              filterState.acpList
            );
            setFilterState((prev) => ({
              ...prev,
              availableAcp,
            }));
          }}
        >
          {filterState.availableElemen.map(({ idElemen, namaElemen }) => (
            <Select.Option key={idElemen} value={idElemen}>
              {namaElemen}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
    );
  };

  const renderAcpSelect = (form) => (
    <Form.Item
      label="Analisis Capaian Pembelajaran"
      name="idAcp"
      rules={[{ required: true, message: "Silahkan pilih ACP" }]}
    >
      <Select
        placeholder="Pilih Analisis Capaian Pembelajaran"
        disabled={filterState.availableAcp.length === 0}
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        style={{ width: "100%" }}
        optionLabelProp="label"
        dropdownStyle={{
          maxWidth: "1000px",
          maxHeight: "400px",
          overflowY: "auto",
        }}
      >
        {filterState.availableAcp.map(({ idAcp, namaAcp }, index) => (
          <Select.Option key={idAcp} value={idAcp} label={namaAcp}>
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "50px 1fr",
                alignItems: "start",
                gap: "6px",
                padding: "2px 0",
                whiteSpace: "normal",
                wordWrap: "break-word",
              }}
            >
              <div style={{ fontWeight: "bold", color: "#888" }}>
                {index + 1}.
              </div>
              <div>{namaAcp}</div>
            </div>
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
    renderAcpSelect,
  };
};
