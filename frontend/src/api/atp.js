import request from "@/utils/request";

export function addATP(data) {
  return request({
    url: "/atp",
    method: "post",
    data,
  });
}

export function getATP() {
  return request({
    url: "/atp",
    method: "get",
  });
}

export function editATP(data, id) {
  return request({
    url: `/atp/${id}`,
    method: "put",
    data,
  });
}

export function deleteATP(data) {
  return request({
    url: `/atp/${data.idAtp}`,
    method: "delete",
    data,
  });
}
