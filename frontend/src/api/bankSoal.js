import request from "@/utils/request";

export function addBankSoal(data) {
  return request({
    url: "/bankSoal",
    method: "post",
    data,
  });
}

export function getBankSoal() {
  return request({
    url: "/bankSoal",
    method: "get",
  });
}

export function editBankSoal(data, id) {
  return request({
    url: `/bankSoal/${id}`,
    method: "put",
    data,
  });
}

export function deleteBankSoal(data) {
  return request({
    url: `/bankSoal/${data.idBankSoal}`,
    method: "delete",
    data,
  });
}

export function getSoalUjian() {
  return request({
    url: "/soalUjian",
    method: "get",
  });
}
