import request from "@/utils/request";

export function addLecture(data) {
  return request({
    url: "/lecture",
    method: "post",
    data,
  });
}

export function getLectures() {
  return request({
    url: "/lecture",
    method: "get",
  });
}

export function editLecture(data, id) {
  return request({
    url: `/lecture/${id}`,
    method: "put",
    data,
  });
}

export function deleteLecture(data) {
  return request({
    url: `/lecture/${data.id}`,
    method: "delete",
    data,
  });
}
