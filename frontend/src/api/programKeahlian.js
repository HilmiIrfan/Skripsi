import request from "@/utils/request";

export function addProgramKeahlian(data) {
  return request({
    url: "/program-keahlian",
    method: "post",
    data,
  });
}

export function getProgramKeahlian() {
  return request({
    url: "/program-keahlian",
    method: "get",
  });
}

export function getProgramByBidang(bidangId) {
  return request({
    url: "/program-keahlian",
    method: "get",
    params: {
      bidangId: bidangId
    },
  });
}

export function editProgramKeahlian(data, id) {
  return request({
    url: `/program-keahlian/${id}`,
    method: "put",
    data,
  });
}

export function deleteProgramKeahlian(data) {
  return request({
    url: `/program-keahlian/${data.id}`,
    method: "delete",
    data,
  });
}