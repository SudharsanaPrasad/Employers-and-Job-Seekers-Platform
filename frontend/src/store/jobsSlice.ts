import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { api, apiError } from '../api/client'
import type { Job, Page } from '../types'

interface JobsState {
  items: Job[]
  total: number
  loading: boolean
  error: string | null
}

const initialState: JobsState = { items: [], total: 0, loading: false, error: null }

export const fetchJobs = createAsyncThunk(
  'jobs/fetch',
  async (params: { location?: string; keyword?: string }, { rejectWithValue }) => {
    try {
      const res = await api.get<Page<Job>>('/api/jobs', {
        params: { ...params, page: 0, size: 50 },
      })
      return res.data
    } catch (e) {
      return rejectWithValue(apiError(e, 'Failed to load jobs'))
    }
  },
)

const jobsSlice = createSlice({
  name: 'jobs',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchJobs.pending, (s) => {
        s.loading = true
        s.error = null
      })
      .addCase(fetchJobs.fulfilled, (s, a) => {
        s.loading = false
        s.items = a.payload.content
        s.total = a.payload.totalElements
      })
      .addCase(fetchJobs.rejected, (s, a) => {
        s.loading = false
        s.error = a.payload as string
      })
  },
})

export default jobsSlice.reducer
