import request from "@/utils/request";

export function addKonsentrasiSekolah(data) {
  return request({
    url: "/konsentrasi-keahlian-sekolah",
    method: "post",
    data,
  });
}

export function getKonsentrasiSekolah() {
  return request({
    url: "/konsentrasi-keahlian-sekolah",
    method: "get",
  });
}

export function editKonsentrasiSekolah(data, id) {
  return request({
    url: `/konsentrasi-keahlian-sekolah/${id}`,
    method: "put",
    data,
  });
}

export function deleteKonsentrasiSekolah(data) {
  return request({
    url: `/konsentrasi-keahlian-sekolah/${data.idKonsentrasiSekolah}`,
    method: "delete",
    data,
  });
}
