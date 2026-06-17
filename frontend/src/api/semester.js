import request from "@/utils/request";

export function addSemester(data) {
  return request({
    url: "/semester",
    method: "post",
    data,
  });
}

export function getSemester() {
  return request({
    url: "/semester",
    method: "get",
  });
}

export function editSemester(data, id) {
  return request({
    url: `/semester/${id}`,
    method: "put",
    data,
  });
}

export function deleteSemester(data) {
  return request({
    url: `/semester/${data.idSemester}`,
    method: "delete",
    data,
  });
}
