import request from "@/utils/request";

    export function getSchoolProfiles() {
        return request({
            url: "/school-profile",
            method: "get",
        });
    }

    export function addSchoolProfile(data) {
      const formData = new FormData();
        formData.append('npsn', data.npsn)
        formData.append('status', data.status)
        formData.append('bentukKependidikan', data.bentukKependidikan)
        formData.append('kepemilikan', data.kepemilikan)
        formData.append('SKPendirianSekolah', data.SKPendirianSekolah)
        formData.append('tglSKPendirian', data.tglSKPendirian)
        formData.append('SKIzinOperasional', data.SKIzinOperasional)
        formData.append('tglSKOperasional', data.tglSKOperasional)
        formData.append('school_id', data.school_id)
        formData.append('file', data.file.file); 
      return request({
        url: "/school-profile",
        method: "post",
        data: formData, 
      });
    }

    export function editSchoolProfile(data, id) {
      const formData = new FormData();
      formData.append('npsn', data.npsn)
      formData.append('status', data.status)
      formData.append('bentukKependidikan', data.bentukKependidikan)
      formData.append('kepemilikan', data.kepemilikan)
      formData.append('SKPendirianSekolah', data.SKPendirianSekolah)
      formData.append('tglSKPendirian', data.tglSKPendirian)
      formData.append('SKIzinOperasional', data.SKIzinOperasional)
      formData.append('tglSKOperasional', data.tglSKOperasional)
      formData.append('school_id', data.school_id)
      formData.append('file', data.file.file); 
    
      return request({
        url: `/school-profile/${id}`,
        method: "put",
        data: formData,
      });
    }
    
    export function deleteSchoolProfile(data) {
        return request({
            url: `/school-profile/${data.id}`,
            method: "delete",
            data,
        });
    }