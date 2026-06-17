export const global = {
  env: import.meta.env.VITE_APP_MODE,
}

export const frontendUrl =
  global.env === 'local'
    ? import.meta.env.VITE_APP_FE_LOCAL_URL
    : import.meta.env.VITE_APP_FE_DEV_URL
