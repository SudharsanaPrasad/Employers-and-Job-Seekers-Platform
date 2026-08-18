import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { api, apiError } from '../api/client'
import type { Job, Page } from '../types'

const emptyForm = { title: '', description: '', location: '', salary: '', deadline: '' }

export default function EmployerDashboardPage() {
  const [jobs, setJobs] = useState<Job[]>([])
  const [error, setError] = useState('')
  const [form, setForm] = useState(emptyForm)
  const [msg, setMsg] = useState('')
  const [saving, setSaving] = useState(false)

  const load = () => {
    api
      .get<Page<Job>>('/api/jobs/mine')
      .then((r) => setJobs(r.data.content))
      .catch((e) => setError(apiError(e, 'Failed to load your jobs')))
  }

  useEffect(() => {
    load()
  }, [])

  const onPost = async (e: FormEvent) => {
    e.preventDefault()
    setSaving(true)
    setMsg('')
    try {
      await api.post('/api/jobs', form)
      setForm(emptyForm)
      setMsg('Job posted!')
      load()
    } catch (err) {
      setMsg(apiError(err, 'Could not post job'))
    } finally {
      setSaving(false)
    }
  }

  const onDelete = async (id: string) => {
    await api.delete(`/api/jobs/${id}`)
    load()
  }

  const field = 'w-full border border-gray-300 rounded px-3 py-2'

  return (
    <div className="max-w-4xl mx-auto px-4 py-6">
      <h1 className="text-2xl font-bold mb-4">Employer Dashboard</h1>

      <form onSubmit={onPost} className="bg-white border border-gray-200 rounded-lg p-6 mb-6 space-y-3">
        <h2 className="font-semibold">Post a job</h2>
        <input
          className={field}
          placeholder="Title"
          value={form.title}
          onChange={(e) => setForm({ ...form, title: e.target.value })}
          required
        />
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <input
            className={field}
            placeholder="Location"
            value={form.location}
            onChange={(e) => setForm({ ...form, location: e.target.value })}
            required
          />
          <input
            className={field}
            placeholder="Salary"
            value={form.salary}
            onChange={(e) => setForm({ ...form, salary: e.target.value })}
            required
          />
          <input
            className={field}
            type="date"
            value={form.deadline}
            onChange={(e) => setForm({ ...form, deadline: e.target.value })}
            required
          />
        </div>
        <textarea
          className={field}
          rows={3}
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
          required
        />
        <button
          disabled={saving}
          className="bg-indigo-600 text-white px-5 py-2 rounded disabled:opacity-60"
        >
          {saving ? 'Posting...' : 'Post job'}
        </button>
        {msg && <p className="text-sm text-gray-700">{msg}</p>}
      </form>

      <h2 className="font-semibold mb-2">My jobs</h2>
      {error && <p className="text-red-600">{error}</p>}
      <div className="space-y-3">
        {jobs.map((j) => (
          <div
            key={j.id}
            className="bg-white border border-gray-200 rounded-lg p-4 flex justify-between items-center"
          >
            <div>
              <p className="font-medium">{j.title}</p>
              <p className="text-sm text-gray-500">
                {j.location} &middot; {j.salary}
              </p>
            </div>
            <div className="flex gap-4 text-sm">
              <Link to={`/employer/jobs/${j.id}/applications`} className="text-indigo-600">
                Applications
              </Link>
              <button onClick={() => onDelete(j.id)} className="text-red-600">
                Delete
              </button>
            </div>
          </div>
        ))}
        {jobs.length === 0 && !error && <p className="text-gray-500">You have not posted any jobs yet.</p>}
      </div>
    </div>
  )
}
