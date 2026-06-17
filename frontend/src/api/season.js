import request from "@/utils/request";

export function addSeason(data) {
  return request({
    url: "/season",
    method: "post",
    data,
  });
}

export function getSeason() {
  return request({
    url: "/season",
    method: "get",
  });
}

export function editSeason(data, id) {
  return request({
    url: `/season/${id}`,
    method: "put",
    data,
  });
}

export function deleteSeason(data) {
  return request({
    url: `/season/${data.id}`,
    method: "delete",
    data,
  });
}