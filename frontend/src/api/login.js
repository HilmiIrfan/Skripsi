import request from "@/utils/request";

export function reqLogin(data) {
  return request({
    url: "/auth/signin",
    method: "post",
    data,
  });
}

export function reqLogout(data) {
  return request({
    url: "/logout",
    method: "post",
    data,
  });
}
