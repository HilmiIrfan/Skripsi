import request from "@/utils/request";

export function addACP(data) {
  return request({
    url: "/acp",
    method: "post",
    data,
  });
}

export function getACP() {
  return request({
    url: "/acp",
    method: "get",
  });
}

export function editACP(data, id) {
  return request({
    url: `/acp/${id}`,
    method: "put",
    data,
  });
}

export function deleteACP(data) {
  return request({
    url: `/acp/${data.idAcp}`,
    method: "delete",
    data,
  });
}
