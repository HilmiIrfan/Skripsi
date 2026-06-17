import request from "@/utils/request";

export function addKonsentrasiKeahlian(data) {
  return request({
    url: "/konsentrasi-keahlian",
    method: "post",
    data,
  });
}

export function getKonsentrasiKeahlian() {
  return request({
    url: "/konsentrasi-keahlian",
    method: "get",
  });
}

export function getKonsentrasiByProgram(programId) {
  return request({
    url: "/konsentrasi-keahlian",
    method: "get",
    params: {
      programId: programId
    },
  });
}

export function editKonsentrasiKeahlian(data, id) {
  return request({
    url: `/konsentrasi-keahlian/${id}`,
    method: "put",
    data,
  });
}

export function deleteKonsentrasiKeahlian(data) {
  return request({
    url: `/konsentrasi-keahlian/${data.id}`,
    method: "delete",
    data,
  });
}