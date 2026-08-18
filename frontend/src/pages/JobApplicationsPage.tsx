import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api, apiError } from '../api/client'
import type { Application, ApplicationStatus } from '../types'

const actions: ApplicationStatus[] = ['REVIEWED', 'ACCEPTED', 'REJECTED']

export default function JobApplicationsPage() {
  const { jobId } = useParams()
  const [apps, setApps] = useState<Application[]>([])
  const [error, setError] = useState('')

  const load = () => {
    api
      .get<Application[]>(`/api/applications/job/${jobId}`)
      .then((r) => setApps(r.data))
      .catch((e) => setError(apiError(e, 'Failed to load applications')))
  }

  useEffect(() => {
    load()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [jobId])

  const setStatus = async (id: string, status: ApplicationStatus) => {
    await api.patch(`/api/applications/${id}/status`, { status })
    load()
  }

  return (
    <div className="max-w-3xl mx-auto px-4 py-6">
      <h1 className="text-2xl font-bold mb-4">Applications</h1>
      {error && <p className="text-red-600">{error}</p>}
      {apps.length === 0 && !error && <p className="text-gray-500">No applications yet.</p>}
      <div className="space-y-3">
        {apps.map((a) => (
          <div key={a.id} className="bg-white border border-gray-200 rounded-lg p-4">
            <div className="flex justify-between items-start">
              <div>
                <p className="font-medium">{a.seekerName}</p>
                <p className="text-sm text-gray-500">for {a.jobTitle}</p>
              </div>
              <span className="text-xs px-2 py-1 rounded bg-gray-100 text-gray-700">{a.status}</span>
            </div>
            {a.coverLetter && <p className="text-sm text-gray-600 mt-2">{a.coverLetter}</p>}
            <div className="flex gap-2 mt-3">
              {actions.map((s) => (
                <button
                  key={s}
                  onClick={() => setStatus(a.id, s)}
                  className="text-xs border border-gray-300 rounded px-2 py-1 hover:bg-gray-50"
                >
                  {s}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
