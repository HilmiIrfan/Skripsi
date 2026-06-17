// src/utils/service.js
import axios from "axios";
import store from "@/store";
import { getToken } from "@/utils/auth";

// Membuat instance axios
const service = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL_BACKEND, // Sesuaikan dengan environment Anda
  timeout: 0, // 10 detik
});

// Request interceptor
service.interceptors.request.use(
  (config) => {
    // Menambahkan Authorization header jika token tersedia
    const token = store.getState().user.token;
    if (token) {
      config.headers.Authorization = `Bearer ${getToken()}`;
    }

    // Cek apakah data adalah FormData
    if (config.data && config.data instanceof FormData) {
      // Biarkan Axios mengatur Content-Type dengan boundary secara otomatis
      delete config.headers["Content-Type"];
    } else {
      // Menetapkan Content-Type ke application/json jika belum ditetapkan
      if (!config.headers["Content-Type"]) {
        config.headers["Content-Type"] = "application/json";
      }
    }

    return config;
  },
  (error) => {
    console.error("Error in request interceptor:", error);
    return Promise.reject(error);
  }
);

// Response interceptor
service.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("Error in response interceptor:", error);
    return Promise.reject(error);
  }
);

export default service;
