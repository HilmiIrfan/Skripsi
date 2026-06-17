import request from "@/utils/request";

export function addJadwalPelajaran(data) {
  return request({
    url: "/jadwal-pelajaran",
    method: "post",
    data,
  });
}

export function getJadwalPelajaran() {
  return request({
    url: "/jadwal-pelajaran",
    method: "get",
  });
}

export function editJadwalPelajaran(data, id) {
  return request({
    url: `/jadwal-pelajaran/${id}`,
    method: "put",
    data,
  });
}

export function deleteJadwalPelajaran(data) {
  return request({
    url: `/jadwal-pelajaran/${data.id}`,
    method: "delete",
    data,
  });
}