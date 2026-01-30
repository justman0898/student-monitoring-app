import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { User, Lock, UserCheck, Users, ArrowLeft } from 'lucide-react'
import { authAPI } from '../services/api'

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  })
  const [userType, setUserType] = useState('admin')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      let response
      switch (userType) {
        case 'admin':
          response = await authAPI.adminLogin(formData)
          break
        case 'teacher':
          response = await authAPI.teacherLogin(formData)
          break
        case 'parent':
          response = await authAPI.parentLogin(formData)
          break
        default:
          throw new Error('Invalid user type')
      }

      // Store auth token and user role
      if (response.data?.token) {
        localStorage.setItem('authToken', response.data.token)
        localStorage.setItem('userRole', userType)
        localStorage.setItem('userId', response.data.userId)
        
        // Redirect based on user type
        switch (userType) {
          case 'admin':
            navigate('/admin/dashboard')
            break
          case 'teacher':
            navigate('/teacher-dashboard')
            break
          case 'parent':
            navigate('/parent-dashboard')
            break
        }
      } else {
        setError('Login successful but no token received')
      }
    } catch (error) {
      console.error('Login error:', error)
      setError(error.response?.data?.message || 'Login failed. Please check your credentials.')
    } finally {
      setLoading(false)
    }
  }

  const userTypes = [
    { value: 'admin', label: 'Administrator', icon: UserCheck, color: 'bg-blue-500' },
    { value: 'teacher', label: 'Teacher', icon: User, color: 'bg-green-500' },
    { value: 'parent', label: 'Parent', icon: Users, color: 'bg-purple-500' }
  ]

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <Link to="/" className="flex items-center gap-2 text-gray-600 hover:text-gray-900">
              <ArrowLeft className="w-5 h-5" />
              Back to Home
            </Link>
            <h1 className="text-2xl font-bold text-blue-600">Student Monitor</h1>
            <Link
              to="/register"
              className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              Create Account
            </Link>
          </div>
        </div>
      </nav>

      <div className="flex items-center justify-center p-4 py-12">
        <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Student Monitor</h1>
          <p className="text-gray-600">Sign in to your account</p>
        </div>

        {/* User Type Selection */}
        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-3">Login as</label>
          <div className="grid grid-cols-3 gap-2">
            {userTypes.map((type) => {
              const Icon = type.icon
              return (
                <button
                  key={type.value}
                  type="button"
                  onClick={() => setUserType(type.value)}
                  className={`p-3 rounded-lg border-2 transition-all ${
                    userType === type.value
                      ? `${type.color} text-white border-transparent`
                      : 'bg-gray-50 text-gray-700 border-gray-200 hover:border-gray-300'
                  }`}
                >
                  <Icon className="w-5 h-5 mx-auto mb-1" />
                  <span className="text-xs font-medium">{type.label}</span>
                </button>
              )
            })}
          </div>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-sm text-red-600">{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="email"
                required
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Enter your email"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="password"
                required
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Enter your password"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3 px-4 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-sm text-gray-600">
            Don't have an account?{' '}
            <Link to="/register" className="text-blue-600 hover:text-blue-500 font-medium">
              Create one here
            </Link>
          </p>
          
          {/* Demo Credentials */}
          <div className="mt-4 p-4 bg-yellow-50 border border-yellow-200 rounded-lg text-left">
            <p className="text-sm font-medium text-yellow-800 mb-2">Demo Credentials (for testing):</p>
            <div className="text-xs text-yellow-700 space-y-1">
              <p><strong>Admin:</strong> admin@school.com / admin123</p>
              <p><strong>Teacher:</strong> teacher@school.com / teacher123</p>
              <p><strong>Parent:</strong> parent@school.com / parent123</p>
            </div>
            <p className="text-xs text-yellow-600 mt-2">
              Note: These are demo accounts. In production, use the registration system.
            </p>
          </div>
          
          <p className="text-sm text-gray-600 mt-4">
            Need help? Contact your system administrator
          </p>
        </div>
      </div>
    </div>
    </div>
  )
}

export default Login