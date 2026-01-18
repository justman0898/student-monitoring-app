import { useState } from 'react'
import { Plus, Search } from 'lucide-react'
import { assessmentAPI } from '../services/api'

const Assessments = () => {
  const [showModal, setShowModal] = useState(false)
  const [searchStudentId, setSearchStudentId] = useState('')
  const [academicHistory, setAcademicHistory] = useState(null)
  const [formData, setFormData] = useState({
    studentId: '',
    subjectId: '',
    score: '',
    maxScore: '',
    term: '',
    academicYear: ''
  })

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await assessmentAPI.submitScore(formData)
      setShowModal(false)
      setFormData({ studentId: '', subjectId: '', score: '', maxScore: '', term: '', academicYear: '' })
      alert('Score submitted successfully!')
    } catch (error) {
      console.error('Error submitting score:', error)
      alert('Failed to submit score')
    }
  }

  const handleSearchHistory = async () => {
    if (!searchStudentId) return
    try {
      const response = await assessmentAPI.getAcademicHistory(searchStudentId)
      setAcademicHistory(response.data)
    } catch (error) {
      console.error('Error fetching academic history:', error)
      alert('Failed to fetch academic history')
    }
  }

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Assessments</h1>
        <button
          onClick={() => setShowModal(true)}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Submit Score
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Student Academic History</h2>
        <div className="flex gap-3">
          <input
            type="text"
            placeholder="Enter Student ID"
            value={searchStudentId}
            onChange={(e) => setSearchStudentId(e.target.value)}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
          />
          <button
            onClick={handleSearchHistory}
            className="flex items-center gap-2 px-6 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            <Search className="w-5 h-5" />
            Search
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Recent Submissions</h2>
          <div className="space-y-3">
            <div className="p-4 bg-gray-50 rounded-lg">
              <div className="flex justify-between items-start">
                <div>
                  <p className="font-medium text-gray-900">Mathematics</p>
                  <p className="text-sm text-gray-600">Student: John Doe</p>
                </div>
                <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                  85/100
                </span>
              </div>
            </div>
            <div className="p-4 bg-gray-50 rounded-lg">
              <div className="flex justify-between items-start">
                <div>
                  <p className="font-medium text-gray-900">English</p>
                  <p className="text-sm text-gray-600">Student: Jane Smith</p>
                </div>
                <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                  92/100
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Quick Actions</h2>
          <div className="space-y-3">
            <button className="w-full p-4 text-left bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors">
              <p className="font-medium text-blue-900">Add Subject Comment</p>
              <p className="text-sm text-blue-700">Provide feedback on subject performance</p>
            </button>
            <button className="w-full p-4 text-left bg-purple-50 hover:bg-purple-100 rounded-lg transition-colors">
              <p className="font-medium text-purple-900">Add Student Comment</p>
              <p className="text-sm text-purple-700">Add general comments for student</p>
            </button>
            <button className="w-full p-4 text-left bg-orange-50 hover:bg-orange-100 rounded-lg transition-colors">
              <p className="font-medium text-orange-900">Update Score</p>
              <p className="text-sm text-orange-700">Modify existing assessment scores</p>
            </button>
          </div>
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Submit Score</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Student ID</label>
                <input
                  type="text"
                  required
                  value={formData.studentId}
                  onChange={(e) => setFormData({ ...formData, studentId: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Subject ID</label>
                <input
                  type="text"
                  required
                  value={formData.subjectId}
                  onChange={(e) => setFormData({ ...formData, subjectId: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Score</label>
                  <input
                    type="number"
                    required
                    value={formData.score}
                    onChange={(e) => setFormData({ ...formData, score: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Max Score</label>
                  <input
                    type="number"
                    required
                    value={formData.maxScore}
                    onChange={(e) => setFormData({ ...formData, maxScore: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Term</label>
                <select
                  required
                  value={formData.term}
                  onChange={(e) => setFormData({ ...formData, term: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                >
                  <option value="">Select Term</option>
                  <option value="1">First Term</option>
                  <option value="2">Second Term</option>
                  <option value="3">Third Term</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Academic Year</label>
                <input
                  type="text"
                  required
                  placeholder="e.g., 2024/2025"
                  value={formData.academicYear}
                  onChange={(e) => setFormData({ ...formData, academicYear: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600"
                >
                  Submit
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default Assessments
