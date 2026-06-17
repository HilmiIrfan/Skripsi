import request from "@/utils/request";

export function addModul(data) {
  return request({
    url: "/modul",
    method: "post",
    data,
  });
}

export function getModul() {
  return request({
    url: "/modul",
    method: "get",
  });
}

export function editModul(data, id) {
  return request({
    url: `/modul/${id}`,
    method: "put",
    data,
  });
}

export function deleteModul(data) {
  return request({
    url: `/modul/${data.idModul}`,
    method: "delete",
    data,
  });
}
