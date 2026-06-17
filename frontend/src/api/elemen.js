import request from "@/utils/request";

export function addElemen(data) {
  return request({
    url: "/elemen",
    method: "post",
    data,
  });
}

export function getElemen() {
  return request({
    url: "/elemen",
    method: "get",
  });
}

export function editElemen(data, id) {
  return request({
    url: `/elemen/${id}`,
    method: "put",
    data,
  });
}

export function deleteElemen(data) {
  return request({
    url: `/elemen/${data.idElemen}`,
    method: "delete",
    data,
  });
}
