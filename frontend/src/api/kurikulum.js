import request from "@/utils/request";

export function addKurikulum(data) {
  return request({
    url: "/kurikulum",
    method: "post",
    data,
  });
}

export function getKurikulum() {
  return request({
    url: "/kurikulum",
    method: "get",
  });
}

export function editKurikulum(data, id) {
  return request({
    url: `/kurikulum/${id}`,
    method: "put",
    data,
  });
}

export function deleteKurikulum(data) {
  return request({
    url: `/kurikulum/${data.id}`,
    method: "delete",
    data,
  });
}