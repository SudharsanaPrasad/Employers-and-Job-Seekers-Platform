import { Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useAppSelector } from '../store/hooks'
import type { Role } from '../types'

// gates a route: requires a logged-in user, and optionally a specific role
export default function ProtectedRoute({ children, role }: { children: ReactNode; role?: Role }) {
  const { user } = useAppSelector((s) => s.auth)
  if (!user) return <Navigate to="/login" replace />
  if (role && user.role !== role) return <Navigate to="/" replace />
  return <>{children}</>
}
