import request from "@/utils/request";

export function addExercise(data) {
  return request({
    url: "/exercise",
    method: "post",
    data,
  });
}

export function getExercise() {
  return request({
    url: "/exercise",
    method: "get",
  });
}

export function getQuestionsByRPS(rpsID, type_exercise) {
  return request({
    url: `/exercise/questions?rpsID=${rpsID}&type_exercise=${type_exercise}`,
    method: "get",
  });
}

export function getExerciseByID(id) {
  return request({
    url: `/exercise/${id}`,
    method: "get",
  });
}

export function editExercise(data, id) {
  return request({
    url: `/exercise/${id}`,
    method: "put",
    data,
  });
}

export function deleteExercise(data) {
  return request({
    url: `/exercise/${data.id}`,
    method: "delete",
    data,
  });
}
