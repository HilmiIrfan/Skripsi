import request from "@/utils/request";

export function addStudyProgram(data) {
  return request({
    url: "/study-program",
    method: "post",
    data,
  });
}

export function getStudyPrograms() {
  return request({
    url: "/study-program",
    method: "get",
  });
}

export function editStudyProgram(data, id) {
  return request({
    url: `/study-program/${id}`,
    method: "put",
    data,
  });
}

export function deleteStudyProgram(data) {
  return request({
    url: `/study-program/${data.id}`,
    method: "delete",
    data,
  });
}
