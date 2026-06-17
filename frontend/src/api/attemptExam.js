import request from "@/utils/request";

export function addAttemptExam(data) {
  return request({
    url: "/exam-attempt",
    method: "post",
    data,
  });
}

export function getAttemptExamByUserID(id) {
  return request({
    url: `/exam-attempt?userID=${id}`,
    method: "get",
  });
}

export function getAttemptExamByExamID(id) {
  return request({
    url: `/exam-attempt?examID=${id}`,
    method: "get",
  });
}
