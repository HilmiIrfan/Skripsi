import request from "@/utils/request";
import requestForm from "@/utils/requestForm";

export function addLinguisticValue(data) {
    return requestForm({
        url: "/linguistic-value",
        method: "post",
        data,
    });
}

export function getLinguisticValues() {
    return request({
        url: "/linguistic-value",
        method: "get",
    });
}

export function editLinguisticValue(data, id) {
    return request({
        url: `/linguistic-value/${id}`,
        method: "put",
        data,
    });
}

export function deleteLinguisticValue(data) {
    return request({
        url: `/linguistic-value/${data.id}`,
        method: "delete",
        data,
    });
}
