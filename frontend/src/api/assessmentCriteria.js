import request from "@/utils/request";

export function addAssessmentCriteria(data) {
  return request({
    url: "/assessment-criteria",
    method: "post",
    data,
  });
}

export function getAssessmentCriterias() {
  return request({
    url: "/assessment-criteria",
    method: "get",
  });
}

export function editAssessmentCriteria(data, id) {
  return request({
    url: `/assessment-criteria/${id}`,
    method: "put",
    data,
  });
}

export function deleteAssessmentCriteria(data) {
  return request({
    url: `/assessment-criteria/${data.id}`,
    method: "delete",
    data,
  });
}
