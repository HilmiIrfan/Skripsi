import request from "@/utils/request";

export function addQuizAnnouncement(data) {
  return request({
    url: "/quizAnnouncements",
    method: "post",
    data,
  });
}

export function getQuizAnnouncements(page = 1, size = 10) {
    return request({
      url: `/quizAnnouncements?page=${page}&size=${size}`,
      method: "get",
    });
  }

export function getQuizAnnouncementByID(id) {
  return request({
    url: `/quizAnnouncements/${id}`,
    method: "get",
  });
}

export function editQuizAnnouncement(data, id) {
  return request({
    url: `/quizAnnouncements/${id}`,
    method: "put",
    data,
  });
}

export function deleteQuizAnnouncement(id) {
  return request({
    url: `/quizAnnouncements/${id}`,
    method: "delete",
  });
}