import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { useAppDispatch, useAppSelector } from '../store/hooks'
import { fetchJobs } from '../store/jobsSlice'
import JobCard from '../components/JobCard'

export default function JobsPage() {
  const dispatch = useAppDispatch()
  const { items, loading, error } = useAppSelector((s) => s.jobs)
  const [location, setLocation] = useState('')
  const [keyword, setKeyword] = useState('')

  useEffect(() => {
    dispatch(fetchJobs({}))
  }, [dispatch])

  const onSearch = (e: FormEvent) => {
    e.preventDefault()
    dispatch(fetchJobs({ location: location || undefined, keyword: keyword || undefined }))
  }

  return (
    <div className="max-w-5xl mx-auto px-4 py-6">
      <h1 className="text-2xl font-bold mb-4">Find a job</h1>
      <form onSubmit={onSearch} className="flex flex-wrap gap-2 mb-6">
        <input
          className="flex-1 min-w-[160px] border border-gray-300 rounded px-3 py-2"
          placeholder="Keyword (title or description)"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <input
          className="flex-1 min-w-[160px] border border-gray-300 rounded px-3 py-2"
          placeholder="Location"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
        <button className="bg-indigo-600 text-white px-5 py-2 rounded">Search</button>
      </form>

      {loading && <p className="text-gray-500">Loading...</p>}
      {error && <p className="text-red-600">{error}</p>}
      <div className="grid gap-4 sm:grid-cols-2">
        {items.map((job) => (
          <JobCard key={job.id} job={job} />
        ))}
      </div>
      {!loading && items.length === 0 && <p className="text-gray-500">No jobs found.</p>}
    </div>
  )
}
