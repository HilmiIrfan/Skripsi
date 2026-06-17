import request from "@/utils/request";

export function addReligion(data) {
  return request({
    url: "/religion",
    method: "post",
    data,
  });
}

export function getReligions() {
  return request({
    url: "/religion",
    method: "get",
  });
}

export function editReligion(data, id) {
  return request({
    url: `/religion/${id}`,
    method: "put",
    data,
  });
}

export function deleteReligion(data) {
  return request({
    url: `/religion/${data.id}`,
    method: "delete",
    data,
  });
}
