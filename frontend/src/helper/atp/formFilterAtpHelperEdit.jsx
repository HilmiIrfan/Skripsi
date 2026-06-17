/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { Form, Select } from "antd";

export const useFormFilterEditAtp = (currentRowData, initialData) => {
  const [filterState, setFilterState] = useState({
    tahunAjaranList: [],
    semesterList: [],
    kelasList: [],
    mapelList: [],
    elemenList: [],
    acpList: [],
    atpList: [],
    availableSemesters: [],
    availableKelas: [],
    availableMapels: [],
    availableElemen: [],
    availableAcp: [],
    availableAtp: [],
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
        atpList,
      } = initialData;

      const availableSemesters = getAvailableSemesters(
        currentRowData?.tahunAjaran?.idTahun,
        semesterList,
        mapelList,
        elemenList,
        acpList,
        atpList
      );

      const availableKelas = getAvailableKelas(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        kelasList,
        mapelList,
        elemenList,
        acpList,
        atpList
      );

      const availableMapels = getAvailableMapels(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        mapelList,
        elemenList,
        acpList,
        atpList
      );

      const availableElemen = getAvailableElemen(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        elemenList,
        acpList,
        atpList
      );

      const availableAcp = getAvailableAcp(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        currentRowData?.elemen?.idElemen,
        acpList,
        atpList
      );

      const availableAtp = getAvailableAtp(
        currentRowData?.tahunAjaran?.idTahun,
        currentRowData?.semester?.idSemester,
        currentRowData?.kelas?.idKelas,
        currentRowData?.mapel?.idMapel,
        currentRowData?.elemen?.idElemen,
        currentRowData?.acp?.idAcp,
        atpList
      );

      setFilterState({
        tahunAjaranList,
        semesterList,
        kelasList,
        mapelList,
        elemenList,
        acpList,
        atpList,
        availableSemesters,
        availableKelas,
        availableMapels,
        availableElemen,
        availableAcp,
        availableAtp,
      });
    }
  }, [initialData, currentRowData]);

  const getAvailableSemesters = (tahunAjaranId, semesterList, atpList) => {
    if (!tahunAjaranId) return [];
    return semesterList.filter((semester) =>
      atpList.some(
        (atp) =>
          atp.tahunAjaran?.idTahun === tahunAjaranId &&
          atp.semester?.idSemester === semester.idSemester
      )
    );
  };

  const getAvailableKelas = (tahunAjaranId, semesterId, kelasList, atpList) => {
    if (!tahunAjaranId || !semesterId) return [];
    return kelasList.filter((kelas) =>
      atpList.some(
        (atp) =>
          atp.tahunAjaran?.idTahun === tahunAjaranId &&
          atp.semester?.idSemester === semesterId &&
          atp.kelas?.idKelas === kelas.idKelas
      )
    );
  };

  const getAvailableMapels = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelList,
    atpList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId) return [];
    return mapelList.filter((mapel) =>
      atpList.some(
        (atp) =>
          atp.tahunAjaran?.idTahun === tahunAjaranId &&
          atp.semester?.idSemester === semesterId &&
          atp.kelas?.idKelas === kelasId &&
          atp.mapel?.idMapel === mapel.idMapel
      )
    );
  };

  const getAvailableElemen = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelId,
    elemenList,
    atpList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId || !mapelId) return [];
    return elemenList.filter((elemen) =>
      atpList.some(
        (atp) =>
          atp.tahunAjaran?.idTahun === tahunAjaranId &&
          atp.semester?.idSemester === semesterId &&
          atp.kelas?.idKelas === kelasId &&
          atp.mapel?.idMapel === mapelId &&
          atp.elemen?.idElemen === elemen.idElemen
      )
    );
  };

  const getAvailableAcp = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelId,
    elemenId,
    acpList,
    atpList
  ) => {
    if (!tahunAjaranId || !semesterId || !kelasId || !mapelId || !elemenId)
      return [];
    return acpList.filter((acp) =>
      atpList.some(
        (atp) =>
          atp.tahunAjaran?.idTahun === tahunAjaranId &&
          atp.semester?.idSemester === semesterId &&
          atp.kelas?.idKelas === kelasId &&
          atp.mapel?.idMapel === mapelId &&
          atp.elemen?.idElemen === elemenId &&
          atp.acp?.idAcp === acp.idAcp
      )
    );
  };

  const getAvailableAtp = (
    tahunAjaranId,
    semesterId,
    kelasId,
    mapelId,
    elemenId,
    acpId,
    atpList
  ) => {
    if (
      !tahunAjaranId ||
      !semesterId ||
      !kelasId ||
      !mapelId ||
      !elemenId ||
      !acpId
    )
      return [];
    return atpList.filter(
      (atp) =>
        atp.tahunAjaran?.idTahun === tahunAjaranId &&
        atp.semester?.idSemester === semesterId &&
        atp.kelas?.idKelas === kelasId &&
        atp.mapel?.idMapel === mapelId &&
        atp.elemen?.idElemen === elemenId &&
        atp.acp?.idAcp === acpId
    );
  };

  const { Option } = Select;

  const renderTahunAjaranSelect = (form) => (
    <Form.Item
      label="Tahun Ajaran:"
      name="idTahun"
      rules={[{ required: true, message: "Silahkan pilih Tahun Ajaran" }]}
    >
      <Select
        placeholder="Pilih Tahun Ajaran"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idTahun) => {
          form.setFieldsValue({
            idSemester: null,
            idKelas: null,
            idMapel: null,
            idElemen: null,
            idAcp: null,
            idAtp: null,
          });
          const availableSemesters = getAvailableSemesters(
            idTahun,
            filterState.semesterList,
            filterState.mapelList,
            filterState.elemenList,
            filterState.acpList,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableSemesters,
            availableKelas: [],
            availableMapels: [],
            availableElemen: [],
            availableAcp: [],
            availableAtp: [],
          }));
        }}
      >
        {filterState.tahunAjaranList.map(({ idTahun, tahunAjaran }) => (
          <Option key={idTahun} value={idTahun}>
            {tahunAjaran}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderSemesterSelect = (form) => (
    <Form.Item
      label="Semester:"
      name="idSemester"
      rules={[{ required: true, message: "Silahkan pilih Semester" }]}
    >
      <Select
        placeholder="Pilih Semester"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idSemester) => {
          form.setFieldsValue({
            idKelas: null,
            idMapel: null,
            idElemen: null,
            idAcp: null,
            idAtp: null,
          });
          const availableKelas = getAvailableKelas(
            form.getFieldValue("idTahun"),
            idSemester,
            filterState.kelasList,
            filterState.mapelList,
            filterState.elemenList,
            filterState.acpList,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableKelas,
            availableMapels: [],
            availableElemen: [],
            availableAcp: [],
            availableAtp: [],
          }));
        }}
      >
        {filterState.availableSemesters.map(({ idSemester, namaSemester }) => (
          <Option key={idSemester} value={idSemester}>
            {namaSemester}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderKelasSelect = (form) => (
    <Form.Item
      label="Kelas:"
      name="idKelas"
      rules={[{ required: true, message: "Silahkan pilih Kelas" }]}
    >
      <Select
        placeholder="Pilih Kelas"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idKelas) => {
          form.setFieldsValue({
            idMapel: null,
            idElemen: null,
            idAcp: null,
            idAtp: null,
          });
          const availableMapels = getAvailableMapels(
            form.getFieldValue("idTahun"),
            form.getFieldValue("idSemester"),
            idKelas,
            filterState.mapelList,
            filterState.elemenList,
            filterState.acpList,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableMapels,
            availableElemen: [],
            availableAcp: [],
            availableAtp: [],
          }));
        }}
      >
        {filterState.availableKelas.map(({ idKelas, namaKelas }) => (
          <Option key={idKelas} value={idKelas}>
            {namaKelas}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderMapelSelect = (form) => (
    <Form.Item
      label="Mata Pelajaran:"
      name="idMapel"
      rules={[{ required: true, message: "Silahkan pilih Mata Pelajaran" }]}
    >
      <Select
        placeholder="Pilih Mata Pelajaran"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idMapel) => {
          form.setFieldsValue({
            idElemen: null,
            idAcp: null,
            idAtp: null,
          });
          const availableElemen = getAvailableElemen(
            form.getFieldValue("idTahun"),
            form.getFieldValue("idSemester"),
            form.getFieldValue("idKelas"),
            idMapel,
            filterState.elemenList,
            filterState.acpList,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableElemen,
            availableAcp: [],
            availableAtp: [],
          }));
        }}
      >
        {filterState.availableMapels.map(({ idMapel, name }) => (
          <Option key={idMapel} value={idMapel}>
            {name}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderElemenSelect = (form) => (
    <Form.Item
      label="Elemen:"
      name="idElemen"
      rules={[{ required: true, message: "Silahkan pilih Elemen" }]}
    >
      <Select
        placeholder="Pilih Elemen"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idElemen) => {
          form.setFieldsValue({
            idAcp: null,
            idAtp: null,
          });
          const availableAcp = getAvailableAcp(
            form.getFieldValue("idTahun"),
            form.getFieldValue("idSemester"),
            form.getFieldValue("idKelas"),
            form.getFieldValue("idMapel"),
            idElemen,
            filterState.acpList,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableAcp,
            availableAtp: [],
          }));
        }}
      >
        {filterState.availableElemen.map(({ idElemen, namaElemen }) => (
          <Option key={idElemen} value={idElemen}>
            {namaElemen}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderAcpSelect = (form) => (
    <Form.Item
      label="Capaian Pembelajaran:"
      name="idAcp"
      rules={[
        { required: true, message: "Silahkan pilih Capaian Pembelajaran" },
      ]}
    >
      <Select
        placeholder="Pilih Capaian Pembelajaran"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
        onChange={(idAcp) => {
          form.setFieldsValue({
            idAtp: null,
          });
          const availableAtp = getAvailableAtp(
            form.getFieldValue("idTahun"),
            form.getFieldValue("idSemester"),
            form.getFieldValue("idKelas"),
            form.getFieldValue("idMapel"),
            form.getFieldValue("idElemen"),
            idAcp,
            filterState.atpList
          );
          setFilterState((prev) => ({
            ...prev,
            availableAtp,
          }));
        }}
      >
        {filterState.availableAcp.map(({ idAcp, namaAcp }) => (
          <Option key={idAcp} value={idAcp}>
            {namaAcp.length > 100 ? `${namaAcp.substring(0, 100)}...` : namaAcp}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  const renderAtpSelect = (form) => (
    <Form.Item
      label="Tujuan Pembelajaran:"
      name="idAtp"
      rules={[
        { required: true, message: "Silahkan pilih Tujuan Pembelajaran" },
      ]}
    >
      <Select
        placeholder="Pilih Tujuan Pembelajaran"
        showSearch
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().includes(input.toLowerCase())
        }
      >
        {filterState.availableAtp.map(({ idAtp, namaAtp }) => (
          <Option key={idAtp} value={idAtp}>
            {namaAtp.length > 100 ? `${namaAtp.substring(0, 100)}...` : namaAtp}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );

  return {
    renderTahunAjaranSelect,
    renderSemesterSelect,
    renderKelasSelect,
    renderMapelSelect,
    renderElemenSelect,
    renderAcpSelect,
    renderAtpSelect,
  };
};
