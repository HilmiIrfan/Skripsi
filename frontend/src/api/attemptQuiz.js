import request from "@/utils/request";

export function addAttemptQuiz(data) {
  return request({
    url: "/quiz-attempt",
    method: "post",
    data,
  });
}

export function getAttemptQuizByUserID(id) {
  return request({
    url: `/quiz-attempt?userID=${id}`,
    method: "get",
  });
}

export function getAttemptQuizByQuizID(id) {
  return request({
    url: `/quiz-attempt?quizID=${id}`,
    method: "get",
  });
}
