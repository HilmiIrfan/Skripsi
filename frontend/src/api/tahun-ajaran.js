import request from "@/utils/request";

export function addTahunAjaran(data) {
  return request({
    url: "/tahun",
    method: "post",
    data,
  });
}

export function getTahunAjaran() {
  return request({
    url: "/tahun",
    method: "get",
  });
}

export function editTahunAjaran(data, id) {
  return request({
    url: `/tahun/${id}`,
    method: "put",
    data,
  });
}

export function deleteTahunAjaran(data) {
  return request({
    url: `/tahun/${data.idTahun}`,
    method: "delete",
    data,
  });
}
