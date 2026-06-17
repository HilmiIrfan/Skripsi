import request from "@/utils/request";

export function addTeamTeaching(data) {
    return request({
        url: "/team-teaching",
        method: "post",
        data,
    });
}

export function getTeamTeachings() {
    return request({
        url: "/team-teaching",
        method: "get",
    });
}

export function editTeamTeaching(data, id) {
    return request({
        url: `/team-teaching/${id}`,
        method: "put",
        data,
    });
}

export function deleteTeamTeaching(data) {
    return request({
        url: `/team-teaching/${data.id}`,
        method: "delete",
        data,
    });
}
