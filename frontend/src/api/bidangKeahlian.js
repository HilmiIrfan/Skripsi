import request from "@/utils/request";

export function addBidangKeahlian(data) {
  return request({
    url: "/bidang-keahlian",
    method: "post",
    data,
  });
}

export function getBidangKeahlian() {
  return request({
    url: "/bidang-keahlian",
    method: "get",
  });
}

export function editBidangKeahlian(data, id) {
  return request({
    url: `/bidang-keahlian/${id}`,
    method: "put",
    data,
  });
}

export function deleteBidangKeahlian(data) {
  return request({
    url: `/bidang-keahlian/${data.id}`,
    method: "delete",
    data,
  });
}

export function getBidangKeahlianById(id) {
  return request({
    url: `/bidang-keahlian/${id}`,
    method: "get",
  });
}

export function importBidangKeahlian(data) {
  return request({
    url: "/bidang-keahlian/",
    method: "post",
    data,
  });
}
