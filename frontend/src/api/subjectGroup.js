import request from "@/utils/request";

export function addSubjectGroup(data) {
  return request({
    url: "/subject-group",
    method: "post",
    data,
  });
}

export function getSubjectGroups() {
  return request({
    url: "/subject-group",
    method: "get",
  });
}

export function editSubjectGroup(data, id) {
  return request({
    url: `/subject-group/${id}`,
    method: "put",
    data,
  });
}

export function deleteSubjectGroup(data) {
  return request({
    url: `/subject-group/${data.id}`,
    method: "delete",
    data,
  });
}
