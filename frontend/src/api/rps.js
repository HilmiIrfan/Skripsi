import request from "@/utils/request";

export function addRPS(data) {
  return request({
    url: "/rps",
    method: "post",
    data,
  });
}

export function importRPS(file) {
  const formData = new FormData();
  formData.append('file', file);

  return request({
    url: "/rps/import",
    method: "post",
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

export function getRPS() {
  return request({
    url: "/rps",
    method: "get",
  });
}

export function editRPS(data, id) {
  return request({
    url: `/rps/${id}`,
    method: "put",
    data,
  });
}

export function getRPSById(rpsId) {
  return request({
    url: `/rps/${rpsId}`,
    method: "get",
  });
}

export function deleteRPS(data) {
  return request({
    url: `/rps/${data.id}`,
    method: "delete",
    data,
  });
  
}
