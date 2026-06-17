import request from "@/utils/request";

export function addSchool(data) {
  return request({
    url: "/school",
    method: "post",
    data,
  });
}

export function getSchool() {
  return request({
    url: "/school",
    method: "get",
  });
}

export function editSchool(data, id) {
  return request({
    url: `/school/${id}`,
    method: "put",
    data,
  });
}

export function deleteSchool(data) {
  return request({
    url: `/school/${data.idSchool}`,
    method: "delete",
    data,
  });
}
