import request from "@/utils/request";

export function addSubject(data) {
  return request({
    url: "/subject",
    method: "post",
    data,
  });
}

export function getSubjects() {
  return request({
    url: "/subject",
    method: "get",
  });
}

export function editSubject(data, id) {
  return request({
    url: `/subject/${id}`,
    method: "put",
    data,
  });
}

export function deleteSubject(data) {
  return request({
    url: `/subject/${data.id}`,
    method: "delete",
    data,
  });
}
