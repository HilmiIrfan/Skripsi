import request from "@/utils/request";

export function getExamTypes() {
    return request({
      url: "/exam-type",
      method: "get",
    });
  }

export function createExamType(data) {
  return request({
    url: "/exam-type",
    method: "post",
    data,
  });
}

export function getExamTypeById(id) {
  return request({
    url: `/exam-type/${id}`,
    method: "get",
  });
}

export function updateExamType(data, id) {
  return request({
    url: `/exam-type/${id}`,
    method: "put",
    data,
  });
}

export function deleteExamType(id) {
  return request({
    url: `/exam-type/${id}`,
    method: "delete",
  });
}