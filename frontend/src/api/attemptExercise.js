import request from "@/utils/request";

export function addAttemptExercise(data) {
  return request({
    url: "/exercise-attempt",
    method: "post",
    data,
  });
}

export function getExerciseAttemptById(id) {
  return request({
    url: `/exercise-attempt/${id}`,
    method: "get",
  });
}

export function getAttemptExerciseByUserID(id) {
  return request({
    url: `/exercise-attempt?userID=${id}`,
    method: "get",
  });
}

export function getAttemptExerciseByExerciseID(id) {
  return request({
    url: `/exercise-attempt?exerciseID=${id}`,
    method: "get",
  });
}

export function getQuestionsFromStudentAnswers(exerciseAttemptId) {
  return request({
    url: `exercise-attempt/questions-from-exercise-attempt/${exerciseAttemptId}`,
    method: "get",
  });
}

// export function getExerciseAttemptById(id) {
//   return request({
//     url: `exercise-attempt/exercise-attempt-id?exerciseAttemptId=${id}`,
//     method: "get",
//   });
// }


