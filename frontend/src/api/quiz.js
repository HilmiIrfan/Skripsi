import request from "@/utils/request";

export function addQuiz(data) {
  return request({
    url: "/quiz",
    method: "post",
    data,
  });
}

export function getQuiz() {
  return request({
    url: "/quiz",
    method: "get",
  });
}

export function getQuestionsByRPSQuiz1(rpsID) {
  return request({
    url: `/quiz/questionsByRPSQuiz1?rpsID=${rpsID}`,
    method: "get",
  });
}
export function getQuestionsByRPSQuiz2(rpsID) {
  return request({
    url: `/quiz/questionsByRPSQuiz2?rpsID=${rpsID}`,
    method: "get",
  });
}

export function getQuizByID(id) {
  return request({
    url: `/quiz/${id}`,
    method: "get",
  });
}

export function editQuiz(data, id) {
  return request({
    url: `/quiz/${id}`,
    method: "put",
    data,
  });
}

export function deleteQuiz(data) {
  return request({
    url: `/quiz/${data.id}`,
    method: "delete",
    data,
  });
}
