import request from "@/utils/request";
import requestForm from "@/utils/requestForm";

export function getRPSs() {
    return request({
      url: `/criteria-value`,
      method: "get",
    });
}

export function getAllQuestionsByRPS(rpsID) {
  return request({
    url: `/criteria-value/questions?rpsID=${rpsID}`,
    method: "get",
  });
}

export function getQuestionsWithCriteriaValuesFromQuizAnnouncement(quizAnnouncementId) {
  return request({
    url: `/criteria-value/quizAnnouncement/${quizAnnouncementId}`,
    method: "get",
  });
}

export function getAllCriteriaValueByQuestion(questionID) {
    return request({
      url: `/criteria-value/question/${questionID}`,
      method: "get",
    });
      
}
  
export function addCriteriaValue(data, questionId) {
  return request({
      url: `/criteria-value/${questionId}`,
      method: "post",
      data,
  });
}

export function getCriteriaValueById(criteriaValueId) {
    return request({
      url: `/criteria-value/${criteriaValueId}`,
      method: "get",
    });
  }

  
export function editCriteriaValue(criteriaValueId, criteriaValueRequest) {
    return request({
      url: `/criteria-value/${criteriaValueId}`,
      method: "put",
      data: criteriaValueRequest,
    });
  }
  
export function deleteCriteriavalue(data) {
    return request({
        url: `/criteria-value/${data.id}`,
        method: "delete",
        data
    });
}