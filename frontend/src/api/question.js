import request from "@/utils/request";
import requestForm from "@/utils/requestForm";

export function getImage(data) {
  return request({
    url: "/question/image",
    method: "get",
    data,
  });
}

// Your existing code goes here...
export function addQuestion(data) {
  return requestForm({
    url: "/question",
    method: "post",
    data,
  });
}

export function getQuestions(rpsDetailID) {
  return request({
    url: `/question?rpsDetailID=${rpsDetailID}`,
    method: "get",
  });
}

export function getQuestionsByRPS(rpsID) {
  return request({
    url: `/question?rpsID=${rpsID}`,
    method: "get",
  });
}

export function getQuestionByIdPaged(questionId) {
  return request({
    url: `question/paged/${questionId}`,
    method: "get",
  });
}

export function editQuestion(data, id) {
  return request({
    url: `/question/${id}`,
    method: "put",
    data,
  });
}

export function deleteQuestion(data) {
  return request({
    url: `/question/${data.id}`,
    method: "delete",
    data,
  });
}
