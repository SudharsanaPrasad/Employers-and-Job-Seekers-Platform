import { Route, Routes } from 'react-router-dom'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import JobsPage from './pages/JobsPage'
import JobDetailPage from './pages/JobDetailPage'
import MyApplicationsPage from './pages/MyApplicationsPage'
import EmployerDashboardPage from './pages/EmployerDashboardPage'
import JobApplicationsPage from './pages/JobApplicationsPage'

export default function App() {
  return (
    <div className="min-h-screen">
      <Navbar />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <JobsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/jobs/:id"
          element={
            <ProtectedRoute>
              <JobDetailPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/my-applications"
          element={
            <ProtectedRoute role="JOB_SEEKER">
              <MyApplicationsPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/employer"
          element={
            <ProtectedRoute role="EMPLOYER">
              <EmployerDashboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/employer/jobs/:jobId/applications"
          element={
            <ProtectedRoute role="EMPLOYER">
              <JobApplicationsPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  )
}
