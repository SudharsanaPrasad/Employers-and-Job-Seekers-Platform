import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAppDispatch, useAppSelector } from '../store/hooks'
import { register } from '../store/authSlice'
import type { Role } from '../types'

export default function RegisterPage() {
  const dispatch = useAppDispatch()
  const navigate = useNavigate()
  const { loading, error } = useAppSelector((s) => s.auth)
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    phone: '',
    role: 'JOB_SEEKER' as Role,
  })

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    const result = await dispatch(register(form))
    if (register.fulfilled.match(result)) navigate('/')
  }

  return (
    <div className="max-w-md mx-auto mt-12 bg-white p-6 rounded-lg border border-gray-200">
      <h1 className="text-2xl font-bold mb-4">Create your account</h1>
      {error && <p className="mb-3 text-sm text-red-600">{error}</p>}
      <form onSubmit={onSubmit} className="space-y-3">
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          placeholder="Full name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          required
        />
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          type="email"
          placeholder="Email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          type="password"
          placeholder="Password (min 6 characters)"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          required
        />
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          placeholder="Phone (e.g. +9198XXXXXXXX)"
          value={form.phone}
          onChange={(e) => setForm({ ...form, phone: e.target.value })}
          required
        />
        <select
          className="w-full border border-gray-300 rounded px-3 py-2"
          value={form.role}
          onChange={(e) => setForm({ ...form, role: e.target.value as Role })}
        >
          <option value="JOB_SEEKER">Job Seeker</option>
          <option value="EMPLOYER">Employer</option>
        </select>
        <button
          disabled={loading}
          className="w-full bg-indigo-600 text-white py-2 rounded disabled:opacity-60"
        >
          {loading ? 'Creating...' : 'Register'}
        </button>
      </form>
      <p className="text-sm text-gray-500 mt-4">
        Already have an account?{' '}
        <Link to="/login" className="text-indigo-600">
          Log in
        </Link>
      </p>
    </div>
  )
}
