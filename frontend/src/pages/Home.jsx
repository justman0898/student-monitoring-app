import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { GraduationCap, Users, BarChart3, BookOpen } from 'lucide-react'

const Home = () => {
  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (token) {
      navigate('/dashboard')
    }
  }, [navigate])

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-gray-900 mb-6">
            Student Monitoring System
          </h1>
          <p className="text-xl text-gray-600 mb-8 max-w-3xl mx-auto">
            Comprehensive platform for managing students, teachers, classes, and academic performance tracking
          </p>
          <div className="flex gap-4 justify-center">
            <button
              onClick={() => navigate('/login')}
              className="px-8 py-3 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors font-medium"
            >
              Sign In
            </button>
            <button
              onClick={() => navigate('/register')}
              className="px-8 py-3 border border-primary text-primary rounded-lg hover:bg-blue-50 transition-colors font-medium"
            >
              Register
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-16">
          <div className="bg-white rounded-xl shadow-sm p-6 text-center">
            <div className="bg-blue-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <Users className="w-8 h-8 text-blue-600" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Teacher Management</h3>
            <p className="text-gray-600">Register and manage teacher profiles, assignments, and specializations</p>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6 text-center">
            <div className="bg-green-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <GraduationCap className="w-8 h-8 text-green-600" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Class Organization</h3>
            <p className="text-gray-600">Create and organize classes, assign teachers, and manage student enrollment</p>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6 text-center">
            <div className="bg-purple-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <BookOpen className="w-8 h-8 text-purple-600" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Subject Management</h3>
            <p className="text-gray-600">Define subjects, create assessments, and track academic progress</p>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6 text-center">
            <div className="bg-orange-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
              <BarChart3 className="w-8 h-8 text-orange-600" />
            </div>
            <h3 className="text-xl font-semibold mb-2">Analytics & Reports</h3>
            <p className="text-gray-600">Generate insights on student performance and identify areas for improvement</p>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Ready to get started?</h2>
          <p className="text-gray-600 mb-6">
            Join our platform to streamline your educational management processes
          </p>
          <button
            onClick={() => navigate('/register')}
            className="px-8 py-3 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors font-medium"
          >
            Create Account
          </button>
        </div>
      </div>
    </div>
  )
}

export default Home