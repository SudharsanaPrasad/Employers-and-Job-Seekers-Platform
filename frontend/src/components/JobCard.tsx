import { Link } from 'react-router-dom'
import type { Job } from '../types'

export default function JobCard({ job }: { job: Job }) {
  return (
    <Link
      to={`/jobs/${job.id}`}
      className="block bg-white border border-gray-200 rounded-lg p-4 hover:shadow-md transition"
    >
      <div className="flex justify-between items-start gap-3">
        <h3 className="text-lg font-semibold text-gray-900">{job.title}</h3>
        <span className="text-sm text-indigo-600 font-medium whitespace-nowrap">{job.salary}</span>
      </div>
      <p className="text-sm text-gray-500 mt-1">
        {job.employerName} &middot; {job.location}
      </p>
      <p className="text-sm text-gray-600 mt-2 line-clamp-2">{job.description}</p>
      <p className="text-xs text-gray-400 mt-2">Apply by {job.deadline}</p>
    </Link>
  )
}
