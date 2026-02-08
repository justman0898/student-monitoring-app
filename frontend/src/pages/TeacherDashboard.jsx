import { useEffect, useState } from 'react'
import { Users, BookOpen, ClipboardList, TrendingUp, LogOut } from 'lucide-react'
import { teacherAPI, assessmentAPI } from '../services/api'
import { useAuth } from '../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

const TeacherDashboard = () => {
  const [profile, setProfile] = useState(null)
  const [students, setStudents] = useState([])
  const [selectedClass, setSelectedClass] = useState('')
  const [loading, setLoading] = useState(true)
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    fetchProfile()
  }, [])

  const fetchProfile = async () => {
    try {
      const response = await teacherAPI.getProfile(user.id)
      setProfile(response.data)
    } catch (error) {
      console.error('Error fetching profile:', error)
    } finally {
      setLoading(false)
    }
  }

  const fetchStudents = async (classId) => {
    if (!classId) return
    try {
      const response = await teacherAPI.getStudentsInClass(classId)
      setStudents(response.data || [])
    } catch (error) {
      console.error('Error fetching students:', error)
    }
  }

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Teacher Dashboard</h1>
              <p className="text-sm text-gray-600">
                Welcome back, {profile?.firstName} {profile?.lastName}
              </p>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 text-gray-600 hover:text-gray-900 transition-colors"
            >
              <LogOut className="w-5 h-5" />
              Logout
            </button>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">My Classes</p>
                <p className="text-3xl font-bold text-gray-900">
                  {profile?.assignedClasses?.length || 0}
                </p>
              </div>
              <div className="bg-blue-500 p-3 rounded-lg">
                <Users className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Students</p>
                <p className="text-3xl font-bold text-gray-900">{students.length}</p>
              </div>
              <div className="bg-green-500 p-3 rounded-lg">
                <BookOpen className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Subjects</p>
                <p className="text-3xl font-bold text-gray-900">
                  {profile?.subjects?.length || 0}
                </p>
              </div>
              <div className="bg-purple-500 p-3 rounded-lg">
                <ClipboardList className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Avg Performance</p>
                <p className="text-3xl font-bold text-gray-900">85%</p>
              </div>
              <div className="bg-orange-500 p-3 rounded-lg">
                <TrendingUp className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Class Selection */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">My Classes</h2>
            <div className="space-y-3">
              {profile?.assignedClasses?.map((classItem) => (
                <button
                  key={classItem.id}
                  onClick={() => {
                    setSelectedClass(classItem.id)
                    fetchStudents(classItem.id)
                  }}
                  className={`w-full text-left p-4 rounded-lg border-2 transition-colors ${
                    selectedClass === classItem.id
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-200 hover:border-gray-300'
                  }`}
                >
                  <h3 className="font-medium text-gray-900">{classItem.name}</h3>
                  <p className="text-sm text-gray-600">
                    Grade {classItem.grade} - Section {classItem.section}
                  </p>
                  <p className="text-sm text-gray-500 mt-1">
                    {classItem.studentCount || 0} students
                  </p>
                </button>
              )) || (
                <p className="text-gray-500 text-center py-4">No classes assigned</p>
              )}
            </div>
          </div>

          {/* Students List */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">
              Students {selectedClass && '- Selected Class'}
            </h2>
            <div className="space-y-3 max-h-96 overflow-y-auto">
              {students.length > 0 ? (
                students.map((student) => (
                  <div
                    key={student.id}
                    className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium text-gray-900">
                          {student.firstName} {student.lastName}
                        </h3>
                        <p className="text-sm text-gray-600">ID: {student.id}</p>
                      </div>
                      <button
                        onClick={() => navigate(`/teacher/student/${student.id}`)}
                        className="text-blue-600 hover:text-blue-800 text-sm font-medium"
                      >
                        View Details
                      </button>
                    </div>
                  </div>
                ))
              ) : (
                <p className="text-gray-500 text-center py-4">
                  {selectedClass ? 'No students in selected class' : 'Select a class to view students'}
                </p>
              )}
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="mt-6 bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <button
              onClick={() => navigate('/teacher/submit-score')}
              className="p-4 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
            >
              <ClipboardList className="w-6 h-6 mx-auto mb-2" />
              Submit Scores
            </button>
            <button
              onClick={() => navigate('/teacher/assessments')}
              className="p-4 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
            >
              <BookOpen className="w-6 h-6 mx-auto mb-2" />
              Manage Assessments
            </button>
            <button
              onClick={() => navigate('/teacher/analytics')}
              className="p-4 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors"
            >
              <TrendingUp className="w-6 h-6 mx-auto mb-2" />
              View Analytics
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TeacherDashboard