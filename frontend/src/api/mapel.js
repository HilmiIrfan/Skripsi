import request from "@/utils/request";

export function addMapel(data) {
  return request({
    url: "/mapel",
    method: "post",
    data,
  });
}

export function getMapel() {
  return request({
    url: "/mapel",
    method: "get",
  });
}

export function editMapel(data, id) {
  return request({
    url: `/mapel/${id}`,
    method: "put",
    data,
  });
}

export function deleteMapel(data) {
  return request({
    url: `/mapel/${data.idMapel}`,
    method: "delete",
    data,
  });
}
