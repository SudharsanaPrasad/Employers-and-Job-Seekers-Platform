import { Link, useNavigate } from 'react-router-dom'
import { useAppDispatch, useAppSelector } from '../store/hooks'
import { logout } from '../store/authSlice'

export default function Navbar() {
  const { user } = useAppSelector((s) => s.auth)
  const dispatch = useAppDispatch()
  const navigate = useNavigate()

  const onLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b border-gray-200">
      <div className="max-w-5xl mx-auto px-4 h-14 flex items-center justify-between">
        <Link to="/" className="text-xl font-bold text-indigo-600">
          CareerNest
        </Link>
        <div className="flex items-center gap-4 text-sm">
          {user ? (
            <>
              <Link to="/" className="text-gray-700 hover:text-indigo-600">
                Jobs
              </Link>
              {user.role === 'JOB_SEEKER' && (
                <Link to="/my-applications" className="text-gray-700 hover:text-indigo-600">
                  My Applications
                </Link>
              )}
              {user.role === 'EMPLOYER' && (
                <Link to="/employer" className="text-gray-700 hover:text-indigo-600">
                  Employer
                </Link>
              )}
              <span className="text-gray-500">{user.name}</span>
              <button onClick={onLogout} className="text-gray-700 hover:text-indigo-600">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="text-gray-700 hover:text-indigo-600">
                Login
              </Link>
              <Link to="/register" className="bg-indigo-600 text-white px-3 py-1.5 rounded">
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}
