/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Form, Select } from "antd";

export const useFormFilterElemen = (initialData) => {
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
    selectedTahunAjaran: null,
    selectedSemester: null,
    selectedKelas: null,
    selectedMapel: null,
  });

  // Inisialisasi data
  useEffect(() => {
    if (initialData) {
      setFilterState((prev) => ({
        ...prev,
        tahunAjaranList: initialData.tahunAjaranList || [],
        semesterList: initialData.semesterList || [],
        kelasList: initialData.kelasList || [],
        mapelList: initialData.mapelList || [],
        elemenList: initialData.elemenList || [],
      }));
    }
  }, [initialData]);

  // Handler perubahan select
  const handleTahunAjaranChange = (value, form) => {
    const newState = {
      selectedTahunAjaran: value,
      selectedSemester: null,
      selectedKelas: null,
      selectedMapel: null,
      availableSemesters: getAvailableSemesters(
        value,
        filterState.semesterList,
        filterState.mapelList
      ),
      availableKelas: [],
      availableMapels: [],
      availableElemen: [],
    };

    setFilterState((prev) => ({ ...prev, ...newState }));
    form?.setFieldsValue({
      idSemester: undefined,
      idKelas: undefined,
      idMapel: undefined,
      idElemen: undefined,
    });
  };

  const handleSemesterChange = (value, form) => {
    const newState = {
      selectedSemester: value,
      selectedKelas: null,
      selectedMapel: null,
      availableKelas: getAvailableKelas(
        filterState.selectedTahunAjaran,
        value,
        filterState.kelasList,
        filterState.mapelList
      ),
      availableMapels: [],
      availableElemen: [],
    };

    setFilterState((prev) => ({ ...prev, ...newState }));
    form?.setFieldsValue({
      idKelas: undefined,
      idMapel: undefined,
      idElemen: undefined,
    });
  };

  const handleKelasChange = (value, form) => {
    const newState = {
      selectedKelas: value,
      selectedMapel: null,
      availableMapels: getAvailableMapels(
        filterState.selectedTahunAjaran,
        filterState.selectedSemester,
        value,
        filterState.mapelList
      ),
      availableElemen: [],
    };

    setFilterState((prev) => ({ ...prev, ...newState }));
    form?.setFieldsValue({ idMapel: undefined, idElemen: undefined });
  };

  const handleMapelChange = (value, form) => {
    const newState = {
      selectedMapel: value,
      availableElemen: getAvailableElemens(
        filterState.selectedTahunAjaran,
        filterState.selectedSemester,
        filterState.selectedKelas,
        value,
        filterState.elemenList
      ),
    };

    setFilterState((prev) => ({ ...prev, ...newState }));
    form?.setFieldsValue({ idElemen: undefined });
  };

  // Fungsi filter - memperbaiki logika filter
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

    // Menghapus duplikat berdasarkan nama mapel
    return filtered.reduce((acc, current) => {
      const x = acc.find((item) => item.name === current.name);
      if (!x) return acc.concat([current]);
      return acc;
    }, []);
  };

  const getAvailableElemens = (
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

  // Komponen Select yang sudah dikonfigurasi
  const renderTahunAjaranSelect = (form) => (
    <Form.Item
      label="Tahun Ajaran"
      name="idTahun"
      rules={[{ required: true, message: "Silahkan pilih Tahun Ajaran" }]}
    >
      <Select
        placeholder="Pilih Tahun Ajaran"
        onChange={(value) => handleTahunAjaranChange(value, form)}
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
      >
        {filterState.tahunAjaranList.map(({ idTahun, tahunAjaran }) => (
          <Select.Option key={idTahun} value={idTahun}>
            {tahunAjaran}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderSemesterSelect = (form) => (
    <Form.Item
      label="Semester"
      name="idSemester"
      rules={[{ required: true, message: "Silahkan pilih Semester" }]}
    >
      <Select
        placeholder="Pilih Semester"
        onChange={(value) => handleSemesterChange(value, form)}
        value={filterState.selectedSemester}
        disabled={
          !filterState.selectedTahunAjaran ||
          filterState.availableSemesters.length === 0
        }
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
      >
        {filterState.availableSemesters.map(({ idSemester, namaSemester }) => (
          <Select.Option key={idSemester} value={idSemester}>
            {namaSemester}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderKelasSelect = (form) => (
    <Form.Item
      label="Kelas"
      name="idKelas"
      rules={[{ required: true, message: "Silahkan pilih Kelas" }]}
    >
      <Select
        placeholder="Pilih Kelas"
        onChange={(value) => handleKelasChange(value, form)}
        value={filterState.selectedKelas}
        disabled={
          !filterState.selectedSemester ||
          filterState.availableKelas.length === 0
        }
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
      >
        {filterState.availableKelas.map(({ idKelas, namaKelas }) => (
          <Select.Option key={idKelas} value={idKelas}>
            {namaKelas}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderMapelSelect = (form) => (
    <Form.Item
      label="Mapel"
      name="idMapel"
      rules={[{ required: true, message: "Silahkan pilih Mapel" }]}
    >
      <Select
        placeholder="Pilih Mapel"
        onChange={(value) => handleMapelChange(value, form)}
        value={filterState.selectedMapel}
        disabled={
          !filterState.selectedKelas || filterState.availableMapels.length === 0
        }
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
      >
        {filterState.availableMapels.map(({ idMapel, name }) => (
          <Select.Option key={idMapel} value={idMapel}>
            {name}
          </Select.Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderElemenSelect = (form) => (
    <Form.Item
      label="Elemen"
      name="idElemen"
      rules={[{ required: true, message: "Silahkan pilih Elemen" }]}
    >
      <Select
        placeholder="Pilih Elemen"
        disabled={
          !filterState.selectedMapel || filterState.availableElemen.length === 0
        }
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
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
    getAvailableSemesters,
    getAvailableKelas,
    getAvailableMapels,
    getAvailableElemens,
  };
};
