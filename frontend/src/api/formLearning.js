import request from "@/utils/request";

export function addFormLearning(data) {
  return request({
    url: "/form-learning",
    method: "post",
    data,
  });
}

export function getFormLearnings() {
  return request({
    url: "/form-learning",
    method: "get",
  });
}

export function editFormLearning(data, id) {
  return request({
    url: `/form-learning/${id}`,
    method: "put",
    data,
  });
}

export function deleteFormLearning(data) {
  return request({
    url: `/form-learning/${data.id}`,
    method: "delete",
    data,
  });
}
