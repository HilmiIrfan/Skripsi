import request from "@/utils/request";

export function addStudent(data) {
  return request({
    url: "/student",
    method: "post",
    data,
  });
}

export function getStudents() {
  return request({
    url: "/student",
    method: "get",
  });
}

export function getStudentByUser(id) {
  return request({
    url: `/student?userID=${id}`,
    method: "get",
  });
}

export function editStudent(data, id) {
  return request({
    url: `/student/${id}`,
    method: "put",
    data,
  });
}

export function deleteStudent(data) {
  return request({
    url: `/student/${data.id}`,
    method: "delete",
    data,
  });
}
