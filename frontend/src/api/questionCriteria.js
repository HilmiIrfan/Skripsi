import request from "@/utils/request";

export function addQuestionCriteria(data) {
  return request({
    url: "/question-criteria",
    method: "post",
    data,
  });
}

export function getQuestionCriterias() {
  return request({
    url: "/question-criteria",
    method: "get",
  });
}

export function editQuestionCriteria(data, id) {
  return request({
    url: `/question-criteria/${id}`,
    method: "put",
    data,
  });
}

export function deleteQuestionCriteria(data) {
  return request({
    url: `/question-criteria/${data.id}`,
    method: "delete",
    data,
  });
}
