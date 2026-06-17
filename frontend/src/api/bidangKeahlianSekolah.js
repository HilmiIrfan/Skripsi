import request from "@/utils/request";

export function addBidangSekolah(data) {
  return request({
    url: "/bidang-keahlian-sekolah",
    method: "post",
    data,
  });
}

export function getBidangSekolah() {
  return request({
    url: "/bidang-keahlian-sekolah",
    method: "get",
  });
}

export function editBidangSekolah(data, id) {
  return request({
    url: `/bidang-keahlian-sekolah/${id}`,
    method: "put",
    data,
  });
}

export function deleteBidangSekolah(data) {
  return request({
    url: `/bidang-keahlian-sekolah/${data.idBidangSekolah}`,
    method: "delete",
    data,
  });
}
