import { useEffect, useState } from 'react'
import { Users, BookOpen, TrendingUp, LogOut, Eye, Plus, Edit } from 'lucide-react'
import { parentAPI, classAPI } from '../services/api'
import { useAuth } from '../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

const ParentDashboard = () => {
  const [linkedStudents, setLinkedStudents] = useState([])
  const [selectedStudent, setSelectedStudent] = useState(null)
  const [studentResults, setStudentResults] = useState([])
  const [showUpdateModal, setShowUpdateModal] = useState(false)
  const [loading, setLoading] = useState(true)
  const [updateData, setUpdateData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: ''
  })
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  useEffect(() => {
    fetchLinkedStudents()
  }, [])

  const fetchLinkedStudents = async () => {
    try {
      const response = await parentAPI.getLinkedStudents(user.id)
      setLinkedStudents(response.data || [])
    } catch (error) {
      console.error('Error fetching linked students:', error)
    } finally {
      setLoading(false)
    }
  }

  const fetchStudentResults = async (studentId) => {
    try {
      const response = await parentAPI.viewResults(studentId)
      setStudentResults(response.data || [])
    } catch (error) {
      console.error('Error fetching student results:', error)
    }
  }

  const handleViewResults = (student) => {
    setSelectedStudent(student)
    fetchStudentResults(student.id)
  }

  const handleUpdateProfile = async (e) => {
    e.preventDefault()
    try {
      await classAPI.updateParent({ ...updateData, parentId: user.id })
      setShowUpdateModal(false)
      alert('Profile updated successfully!')
    } catch (error) {
      console.error('Error updating profile:', error)
      alert('Failed to update profile')
    }
  }

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const calculateAverage = (results) => {
    if (!results.length) return 0
    const total = results.reduce((sum, result) => sum + (result.score || 0), 0)
    return Math.round(total / results.length)
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
              <h1 className="text-2xl font-bold text-gray-900">Parent Dashboard</h1>
              <p className="text-sm text-gray-600">
                Welcome back, {user?.firstName} {user?.lastName}
              </p>
            </div>
            <div className="flex items-center gap-4">
              <button
                onClick={() => setShowUpdateModal(true)}
                className="flex items-center gap-2 px-4 py-2 text-blue-600 hover:text-blue-800 transition-colors"
              >
                <Edit className="w-5 h-5" />
                Update Profile
              </button>
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 px-4 py-2 text-gray-600 hover:text-gray-900 transition-colors"
              >
                <LogOut className="w-5 h-5" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Linked Students</p>
                <p className="text-3xl font-bold text-gray-900">{linkedStudents.length}</p>
              </div>
              <div className="bg-blue-500 p-3 rounded-lg">
                <Users className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Average Performance</p>
                <p className="text-3xl font-bold text-gray-900">
                  {calculateAverage(studentResults)}%
                </p>
              </div>
              <div className="bg-green-500 p-3 rounded-lg">
                <TrendingUp className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Total Assessments</p>
                <p className="text-3xl font-bold text-gray-900">{studentResults.length}</p>
              </div>
              <div className="bg-purple-500 p-3 rounded-lg">
                <BookOpen className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Linked Students */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">My Children</h2>
            <div className="space-y-3">
              {linkedStudents.length > 0 ? (
                linkedStudents.map((student) => (
                  <div
                    key={student.id}
                    className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium text-gray-900">
                          {student.firstName} {student.lastName}
                        </h3>
                        <p className="text-sm text-gray-600">Class: {student.className}</p>
                        <p className="text-sm text-gray-600">ID: {student.id}</p>
                      </div>
                      <button
                        onClick={() => handleViewResults(student)}
                        className="flex items-center gap-1 text-blue-600 hover:text-blue-800 text-sm font-medium"
                      >
                        <Eye className="w-4 h-4" />
                        View Results
                      </button>
                    </div>
                  </div>
                ))
              ) : (
                <p className="text-gray-500 text-center py-4">No linked students found.</p>
              )}
            </div>
          </div>

          {/* Student Results */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">
              {selectedStudent ? `${selectedStudent.firstName}'s Results` : 'Student Results'}
            </h2>
            <div className="space-y-3 max-h-96 overflow-y-auto">
              {selectedStudent ? (
                studentResults.length > 0 ? (
                  studentResults.map((result, index) => (
                    <div
                      key={index}
                      className="p-4 border border-gray-200 rounded-lg"
                    >
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-medium text-gray-900">{result.subject}</h3>
                          <p className="text-sm text-gray-600">{result.assessmentType}</p>
                          <p className="text-sm text-gray-500">{result.date}</p>
                        </div>
                        <div className="text-right">
                          <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                            result.percentage >= 80 ? 'bg-green-100 text-green-800' :
                            result.percentage >= 60 ? 'bg-yellow-100 text-yellow-800' :
                            'bg-red-100 text-red-800'
                          }`}>
                            {result.score}/{result.maxScore} ({result.percentage}%)
                          </span>
                        </div>
                      </div>
                      {result.comment && (
                        <p className="text-sm text-gray-600 mt-2 italic">"{result.comment}"</p>
                      )}
                    </div>
                  ))
                ) : (
                  <p className="text-gray-500 text-center py-4">No results found for this student.</p>
                )
              ) : (
                <p className="text-gray-500 text-center py-4">Select a student to view their results.</p>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Update Profile Modal */}
      {showUpdateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Update Profile</h2>
            <form onSubmit={handleUpdateProfile} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <input
                  type="text"
                  required
                  value={updateData.firstName}
                  onChange={(e) => setUpdateData({ ...updateData, firstName: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <input
                  type="text"
                  required
                  value={updateData.lastName}
                  onChange={(e) => setUpdateData({ ...updateData, lastName: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input
                  type="email"
                  required
                  value={updateData.email}
                  onChange={(e) => setUpdateData({ ...updateData, email: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                <input
                  type="tel"
                  required
                  value={updateData.phoneNumber}
                  onChange={(e) => setUpdateData({ ...updateData, phoneNumber: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Address</label>
                <textarea
                  required
                  value={updateData.address}
                  onChange={(e) => setUpdateData({ ...updateData, address: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  rows="3"
                />
              </div>
              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowUpdateModal(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600"
                >
                  Update Profile
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default ParentDashboard