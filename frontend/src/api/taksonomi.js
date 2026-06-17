import request from "@/utils/request";

export function addTaksonomi(data) {
  return request({
    url: "/taksonomi",
    method: "post",
    data,
  });
}

export function getTaksonomi() {
  return request({
    url: "/taksonomi",
    method: "get",
  });
}

export function editTaksonomi(data, id) {
  return request({
    url: `/taksonomi/${id}`,
    method: "put",
    data,
  });
}

export function deleteTaksonomi(data) {
  return request({
    url: `/taksonomi/${data.idTaksonomi}`,
    method: "delete",
    data,
  });
}
