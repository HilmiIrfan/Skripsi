import request from "@/utils/request";

export function addExam(data) {
  return request({
    url: "/exam",
    method: "post",
    data,
  });
}

export function getExam() {
  return request({
    url: "/exam",
    method: "get",
  });
}

export function getExamByID(id) {
  return request({
    url: `/exam/${id}`,
    method: "get",
  });
}

export function editExam(data, id) {
  return request({
    url: `/exam/${id}`,
    method: "put",
    data,
  });
}

export function deleteExam(data) {
  return request({
    url: `/exam/${data.id}`,
    method: "delete",
    data,
  });
}
