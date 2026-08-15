import { useState } from 'react'
import type { FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAppDispatch, useAppSelector } from '../store/hooks'
import { login } from '../store/authSlice'

export default function LoginPage() {
  const dispatch = useAppDispatch()
  const navigate = useNavigate()
  const { loading, error } = useAppSelector((s) => s.auth)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault()
    const result = await dispatch(login({ email, password }))
    if (login.fulfilled.match(result)) navigate('/')
  }

  return (
    <div className="max-w-md mx-auto mt-12 bg-white p-6 rounded-lg border border-gray-200">
      <h1 className="text-2xl font-bold mb-4">Log in</h1>
      {error && <p className="mb-3 text-sm text-red-600">{error}</p>}
      <form onSubmit={onSubmit} className="space-y-3">
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          className="w-full border border-gray-300 rounded px-3 py-2"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button
          disabled={loading}
          className="w-full bg-indigo-600 text-white py-2 rounded disabled:opacity-60"
        >
          {loading ? 'Logging in...' : 'Log in'}
        </button>
      </form>
      <p className="text-sm text-gray-500 mt-4">
        No account?{' '}
        <Link to="/register" className="text-indigo-600">
          Register
        </Link>
      </p>
    </div>
  )
}
