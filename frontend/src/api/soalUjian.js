import request from "@/utils/request";

export function addSoalUjian(data) {
  return request({
    url: "/soalUjian",
    method: "post",
    data,
  });
}

export function getSoalUjian() {
  return request({
    url: "/soalUjian",
    method: "get",
  });
}

export function editSoalUjian(data, id) {
  return request({
    url: `/soalUjian/${id}`,
    method: "put",
    data,
  });
}
export function deleteSoalUjian(data) {
  return request({
    url: `/soalUjian/${data.idSoalUjian}`,
    method: "delete",
    data,
  });
}
