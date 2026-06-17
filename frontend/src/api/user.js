import request from "@/utils/request";

export function reqUserInfo(data) {
  return request({
    url: "/user/me",
    method: "get",
    data,
  });
}

export function getUsers() {
  return request({
    url: "/users",
    method: "get",
  });
}

export function getUsersNotUsedInLectures() {
  return request({
    url: "/users/not-used-account",
    method: "get",
  });
}

export function deleteUser(data) {
  return request({
    url: `/users/delete/${data.id}`,
    method: "delete",
    data,
  });
}

export function editUser(data, id) {
  return request({
    url: `/users/edit/${id}`,
    method: "put",
    data,
  });
}

export function reqValidatUserID(data) {
  return request({
    url: "/user/validatUserID",
    method: "post",
    data,
  });
}

export function addUser(data) {
  return request({
    url: "/users/add",
    method: "post",
    data,
  });
}

export function getUserById(userId) {
  return request({
    url: `/users/${userId}`,
    method: "get",
  });
}
