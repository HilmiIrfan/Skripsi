import request from "@/utils/request";

export function addDepartment(data) {
  return request({
    url: "/department",
    method: "post",
    data,
  });
}

export function getDepartments() {
  return request({
    url: "/department",
    method: "get",
  });
}

export function editDepartment(data, id) {
  return request({
    url: `/department/${id}`,
    method: "put",
    data,
  });
}

export function deleteDepartment(data) {
  return request({
    url: `/department/${data.id}`,
    method: "delete",
    data,
  });
}
