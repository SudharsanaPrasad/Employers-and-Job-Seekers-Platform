import { useEffect, useState } from 'react'
import { api, apiError } from '../api/client'
import type { Application, ApplicationStatus } from '../types'

const statusColor: Record<ApplicationStatus, string> = {
  APPLIED: 'bg-gray-100 text-gray-700',
  REVIEWED: 'bg-blue-100 text-blue-700',
  ACCEPTED: 'bg-green-100 text-green-700',
  REJECTED: 'bg-red-100 text-red-700',
}

export default function MyApplicationsPage() {
  const [apps, setApps] = useState<Application[]>([])
  const [error, setError] = useState('')

  useEffect(() => {
    api
      .get<Application[]>('/api/applications/mine')
      .then((r) => setApps(r.data))
      .catch((e) => setError(apiError(e, 'Failed to load applications')))
  }, [])

  return (
    <div className="max-w-3xl mx-auto px-4 py-6">
      <h1 className="text-2xl font-bold mb-4">My Applications</h1>
      {error && <p className="text-red-600">{error}</p>}
      {apps.length === 0 && !error && (
        <p className="text-gray-500">You have not applied to any jobs yet.</p>
      )}
      <div className="space-y-3">
        {apps.map((a) => (
          <div
            key={a.id}
            className="bg-white border border-gray-200 rounded-lg p-4 flex justify-between items-center"
          >
            <div>
              <p className="font-medium">{a.jobTitle}</p>
              <p className="text-sm text-gray-500">
                Applied {new Date(a.appliedAt).toLocaleDateString()}
              </p>
            </div>
            <span className={`text-xs px-2 py-1 rounded ${statusColor[a.status]}`}>{a.status}</span>
          </div>
        ))}
      </div>
    </div>
  )
}
