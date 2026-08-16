import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api, apiError } from '../api/client'
import { useAppSelector } from '../store/hooks'
import type { Job } from '../types'

export default function JobDetailPage() {
  const { id } = useParams()
  const { user } = useAppSelector((s) => s.auth)
  const [job, setJob] = useState<Job | null>(null)
  const [error, setError] = useState('')
  const [coverLetter, setCoverLetter] = useState('')
  const [applyMsg, setApplyMsg] = useState('')
  const [applying, setApplying] = useState(false)

  useEffect(() => {
    api
      .get<Job>(`/api/jobs/${id}`)
      .then((r) => setJob(r.data))
      .catch((e) => setError(apiError(e, 'Job not found')))
  }, [id])

  const onApply = async () => {
    setApplying(true)
    setApplyMsg('')
    try {
      await api.post('/api/applications', { jobId: id, coverLetter: coverLetter || undefined })
      setApplyMsg('Application submitted!')
    } catch (e) {
      setApplyMsg(apiError(e, 'Could not apply'))
    } finally {
      setApplying(false)
    }
  }

  if (error) return <div className="max-w-3xl mx-auto px-4 py-6 text-red-600">{error}</div>
  if (!job) return <div className="max-w-3xl mx-auto px-4 py-6 text-gray-500">Loading...</div>

  return (
    <div className="max-w-3xl mx-auto px-4 py-6">
      <div className="bg-white border border-gray-200 rounded-lg p-6">
        <h1 className="text-2xl font-bold">{job.title}</h1>
        <p className="text-gray-500 mt-1">
          {job.employerName} &middot; {job.location}
        </p>
        <p className="text-indigo-600 font-medium mt-1">{job.salary}</p>
        <p className="text-sm text-gray-400">Apply by {job.deadline}</p>
        <p className="mt-4 whitespace-pre-wrap text-gray-700">{job.description}</p>
      </div>

      {user?.role === 'JOB_SEEKER' && (
        <div className="bg-white border border-gray-200 rounded-lg p-6 mt-4">
          <h2 className="font-semibold mb-2">Apply for this job</h2>
          <textarea
            className="w-full border border-gray-300 rounded px-3 py-2"
            rows={3}
            placeholder="Cover letter (optional)"
            value={coverLetter}
            onChange={(e) => setCoverLetter(e.target.value)}
          />
          <button
            disabled={applying}
            onClick={onApply}
            className="mt-2 bg-indigo-600 text-white px-4 py-2 rounded disabled:opacity-60"
          >
            {applying ? 'Submitting...' : 'Apply'}
          </button>
          {applyMsg && <p className="mt-2 text-sm text-gray-700">{applyMsg}</p>}
        </div>
      )}
    </div>
  )
}
