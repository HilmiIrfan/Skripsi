import request from "@/utils/request";

export function addProgramSekolah(data) {
  return request({
    url: "/program-keahlian-sekolah",
    method: "post",
    data,
  });
}

export function getProgramSekolah() {
  return request({
    url: "/program-keahlian-sekolah",
    method: "get",
  });
}

export function editProgramSekolah(data, id) {
  return request({
    url: `/program-keahlian-sekolah/${id}`,
    method: "put",
    data,
  });
}

export function deleteProgramSekolah(data) {
  return request({
    url: `/program-keahlian-sekolah/${data.idProgramSekolah}`,
    method: "delete",
    data,
  });
}
