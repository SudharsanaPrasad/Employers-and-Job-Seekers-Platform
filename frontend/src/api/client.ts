import axios from 'axios'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export const api = axios.create({ baseURL })

// add the token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// get the error message to show
export function apiError(e: unknown, fallback: string): string {
  if (axios.isAxiosError(e)) {
    return (e.response?.data as { message?: string })?.message ?? e.message ?? fallback
  }
  return fallback
}
