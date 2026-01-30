import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Layout from './components/Layout'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import TeacherDashboard from './pages/TeacherDashboard'
import TeacherScoreSubmission from './pages/TeacherScoreSubmission'
import ParentDashboard from './pages/ParentDashboard'
import Teachers from './pages/Teachers'
import Classes from './pages/Classes'
import Subjects from './pages/Subjects'
import Parents from './pages/Parents'
import Assessments from './pages/Assessments'
import AssessmentTypes from './pages/AssessmentTypes'
import Analytics from './pages/Analytics'

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route path="/unauthorized" element={
            <div className="min-h-screen flex items-center justify-center">
              <div className="text-center">
                <h1 className="text-2xl font-bold text-gray-900 mb-4">Unauthorized</h1>
                <p className="text-gray-600">You don't have permission to access this page.</p>
              </div>
            </div>
          } />
          
          {/* Admin Routes */}
          <Route path="/admin/*" element={
            <ProtectedRoute requiredRole="admin">
              <Layout>
                <Routes>
                  <Route index element={<Navigate to="/admin/dashboard" replace />} />
                  <Route path="dashboard" element={<Dashboard />} />
                  <Route path="teachers" element={<Teachers />} />
                  <Route path="classes" element={<Classes />} />
                  <Route path="subjects" element={<Subjects />} />
                  <Route path="parents" element={<Parents />} />
                  <Route path="assessments" element={<Assessments />} />
                  <Route path="assessment-types" element={<AssessmentTypes />} />
                  <Route path="analytics" element={<Analytics />} />
                </Routes>
              </Layout>
            </ProtectedRoute>
          } />

          {/* Teacher Routes */}
          <Route path="/teacher-dashboard" element={
            <ProtectedRoute requiredRole="teacher">
              <TeacherDashboard />
            </ProtectedRoute>
          } />
          <Route path="/teacher/submit-score" element={
            <ProtectedRoute requiredRole="teacher">
              <TeacherScoreSubmission />
            </ProtectedRoute>
          } />

          {/* Parent Routes */}
          <Route path="/parent-dashboard" element={
            <ProtectedRoute requiredRole="parent">
              <ParentDashboard />
            </ProtectedRoute>
          } />

          {/* Catch all route */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  )
}

export default App
