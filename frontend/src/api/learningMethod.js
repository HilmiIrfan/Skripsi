import request from "@/utils/request";

export function addLearningMethod(data) {
  return request({
    url: "/learning-method",
    method: "post",
    data,
  });
}

export function getLearningMethods() {
  return request({
    url: "/learning-method",
    method: "get",
  });
}

export function editLearningMethod(data, id) {
  return request({
    url: `/learning-method/${id}`,
    method: "put",
    data,
  });
}

export function deleteLearningMethod(data) {
  return request({
    url: `/learning-method/${data.id}`,
    method: "delete",
    data,
  });
}
