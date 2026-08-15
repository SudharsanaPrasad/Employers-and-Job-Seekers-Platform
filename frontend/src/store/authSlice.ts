import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'
import { api, apiError } from '../api/client'
import type { AuthResponse, AuthUser, Role } from '../types'

interface AuthState {
  user: AuthUser | null
  token: string | null
  loading: boolean
  error: string | null
}

const storedUser = localStorage.getItem('user')
const initialState: AuthState = {
  user: storedUser ? (JSON.parse(storedUser) as AuthUser) : null,
  token: localStorage.getItem('token'),
  loading: false,
  error: null,
}

export const login = createAsyncThunk(
  'auth/login',
  async (body: { email: string; password: string }, { rejectWithValue }) => {
    try {
      const res = await api.post<AuthResponse>('/api/auth/login', body)
      return res.data
    } catch (e) {
      return rejectWithValue(apiError(e, 'Login failed'))
    }
  },
)

export const register = createAsyncThunk(
  'auth/register',
  async (
    body: { name: string; email: string; password: string; phone: string; role: Role },
    { rejectWithValue },
  ) => {
    try {
      const res = await api.post<AuthResponse>('/api/auth/register', body)
      return res.data
    } catch (e) {
      return rejectWithValue(apiError(e, 'Registration failed'))
    }
  },
)

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout(state) {
      state.user = null
      state.token = null
      state.error = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },
  },
  extraReducers: (builder) => {
    const success = (state: AuthState, action: PayloadAction<AuthResponse>) => {
      const { token, ...user } = action.payload
      state.user = user
      state.token = token
      state.loading = false
      state.error = null
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
    }
    builder
      .addCase(login.pending, (s) => {
        s.loading = true
        s.error = null
      })
      .addCase(login.fulfilled, success)
      .addCase(login.rejected, (s, a) => {
        s.loading = false
        s.error = a.payload as string
      })
      .addCase(register.pending, (s) => {
        s.loading = true
        s.error = null
      })
      .addCase(register.fulfilled, success)
      .addCase(register.rejected, (s, a) => {
        s.loading = false
        s.error = a.payload as string
      })
  },
})

export const { logout } = authSlice.actions
export default authSlice.reducer
