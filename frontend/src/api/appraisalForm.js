import request from "@/utils/request";

export function addAppraisalForm(data) {
  return request({
    url: "/appraisal-form",
    method: "post",
    data,
  });
}

export function getAppraisalForms() {
  return request({
    url: "/appraisal-form",
    method: "get",
  });
}

export function editAppraisalForm(data, id) {
  return request({
    url: `/appraisal-form/${id}`,
    method: "put",
    data,
  });
}

export function deleteAppraisalForm(data) {
  return request({
    url: `/appraisal-form/${data.id}`,
    method: "delete",
    data,
  });
}
