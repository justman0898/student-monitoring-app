import { useEffect, useState } from 'react'
import { Users, BookOpen, TrendingUp, LogOut, Eye } from 'lucide-react'
import { parentAPI } from '../services/api'
import { useAuth } from '../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

const ParentDashboard = () => {
  const [linkedStudents, setLinkedStudents] = useState([])
  const [selectedStudent, setSelectedStudent] = useState(null)
  const [studentResults, setStudentResults] = useState([])
  const [loading, setLoading] = useState(true)
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
              <p className="text-sm text-gray-600">Monitor your child's academic progress</p>
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
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">My Children</p>
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
                <p className="text-sm text-gray-600 mb-1">Total Subjects</p>
                <p className="text-3xl font-bold text-gray-900">
                  {selectedStudent ? studentResults.length : '-'}
                </p>
              </div>
              <div className="bg-green-500 p-3 rounded-lg">
                <BookOpen className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600 mb-1">Average Score</p>
                <p className="text-3xl font-bold text-gray-900">
                  {selectedStudent ? `${calculateAverage(studentResults)}%` : '-'}
                </p>
              </div>
              <div className="bg-purple-500 p-3 rounded-lg">
                <TrendingUp className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Students List */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">My Children</h2>
            <div className="space-y-3">
              {linkedStudents.length > 0 ? (
                linkedStudents.map((student) => (
                  <div
                    key={student.id}
                    className={`p-4 border-2 rounded-lg transition-colors cursor-pointer ${
                      selectedStudent?.id === student.id
                        ? 'border-blue-500 bg-blue-50'
                        : 'border-gray-200 hover:border-gray-300'
                    }`}
                    onClick={() => handleViewResults(student)}
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="font-medium text-gray-900">
                          {student.firstName} {student.lastName}
                        </h3>
                        <p className="text-sm text-gray-600">
                          Class: {student.className} - Grade {student.grade}
                        </p>
                        <p className="text-sm text-gray-500">ID: {student.id}</p>
                      </div>
                      <button className="text-blue-600 hover:text-blue-800">
                        <Eye className="w-5 h-5" />
                      </button>
                    </div>
                  </div>
                ))
              ) : (
                <div className="text-center py-8">
                  <Users className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                  <p className="text-gray-500">No children linked to your account</p>
                  <p className="text-sm text-gray-400 mt-2">
                    Contact the school administrator to link your children
                  </p>
                </div>
              )}
            </div>
          </div>

          {/* Student Results */}
          <div className="bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">
              Academic Results
              {selectedStudent && ` - ${selectedStudent.firstName} ${selectedStudent.lastName}`}
            </h2>
            <div className="space-y-3 max-h-96 overflow-y-auto">
              {selectedStudent ? (
                studentResults.length > 0 ? (
                  studentResults.map((result, index) => (
                    <div key={index} className="p-4 border border-gray-200 rounded-lg">
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-medium text-gray-900">{result.subjectName}</h3>
                          <p className="text-sm text-gray-600">
                            {result.assessmentType} - {result.term}
                          </p>
                          <p className="text-sm text-gray-500">{result.date}</p>
                        </div>
                        <div className="text-right">
                          <span
                            className={`px-3 py-1 rounded-full text-sm font-medium ${
                              result.score >= 80
                                ? 'bg-green-100 text-green-800'
                                : result.score >= 60
                                ? 'bg-yellow-100 text-yellow-800'
                                : 'bg-red-100 text-red-800'
                            }`}
                          >
                            {result.score}/{result.maxScore}
                          </span>
                          <p className="text-xs text-gray-500 mt-1">
                            {Math.round((result.score / result.maxScore) * 100)}%
                          </p>
                        </div>
                      </div>
                      {result.comment && (
                        <div className="mt-3 p-3 bg-gray-50 rounded-lg">
                          <p className="text-sm text-gray-700">{result.comment}</p>
                        </div>
                      )}
                    </div>
                  ))
                ) : (
                  <div className="text-center py-8">
                    <BookOpen className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                    <p className="text-gray-500">No results available</p>
                  </div>
                )
              ) : (
                <div className="text-center py-8">
                  <p className="text-gray-500">Select a child to view their results</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Performance Summary */}
        {selectedStudent && studentResults.length > 0 && (
          <div className="mt-6 bg-white rounded-xl shadow-sm p-6">
            <h2 className="text-xl font-semibold mb-4">Performance Summary</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="p-4 bg-green-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Highest Score</p>
                <p className="text-2xl font-bold text-green-700">
                  {Math.max(...studentResults.map(r => Math.round((r.score / r.maxScore) * 100)))}%
                </p>
              </div>
              <div className="p-4 bg-blue-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Average Score</p>
                <p className="text-2xl font-bold text-blue-700">
                  {calculateAverage(studentResults.map(r => Math.round((r.score / r.maxScore) * 100)))}%
                </p>
              </div>
              <div className="p-4 bg-purple-50 rounded-lg">
                <p className="text-sm text-gray-600 mb-1">Total Assessments</p>
                <p className="text-2xl font-bold text-purple-700">{studentResults.length}</p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default ParentDashboard