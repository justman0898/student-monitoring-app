import { useEffect, useState } from 'react'
import { Plus, Trash2, Settings } from 'lucide-react'
import { classAPI } from '../services/api'

const AssessmentTypes = () => {
  const [assessmentTypes, setAssessmentTypes] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [showConfigModal, setShowConfigModal] = useState(false)
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    weight: ''
  })
  const [configData, setConfigData] = useState({
    assessmentTypeId: '',
    maxScore: '',
    passingScore: '',
    term: '',
    academicYear: ''
  })

  useEffect(() => {
    fetchAssessmentTypes()
  }, [])

  const fetchAssessmentTypes = async () => {
    try {
      const response = await classAPI.getAllAssessmentTypes()
      setAssessmentTypes(response.data || [])
    } catch (error) {
      console.error('Error fetching assessment types:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.createAssessmentType(formData)
      setShowModal(false)
      setFormData({ name: '', description: '', weight: '' })
      fetchAssessmentTypes()
    } catch (error) {
      console.error('Error creating assessment type:', error)
      alert('Failed to create assessment type')
    }
  }

  const handleConfigSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.createAssessmentConfig(configData)
      setShowConfigModal(false)
      setConfigData({ assessmentTypeId: '', maxScore: '', passingScore: '', term: '', academicYear: '' })
      alert('Assessment configuration created successfully!')
    } catch (error) {
      console.error('Error creating assessment config:', error)
      alert('Failed to create assessment configuration')
    }
  }

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Assessment Types</h1>
        <div className="flex gap-3">
          <button
            onClick={() => setShowConfigModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
          >
            <Settings className="w-5 h-5" />
            Configure Assessment
          </button>
          <button
            onClick={() => setShowModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            <Plus className="w-5 h-5" />
            Add Assessment Type
          </button>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {assessmentTypes.length === 0 ? (
            <div className="col-span-full text-center py-12 text-gray-500">
              No assessment types found. Create your first assessment type to get started.
            </div>
          ) : (
            assessmentTypes.map((type) => (
              <div key={type.id} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div className="flex-1">
                    <h3 className="text-xl font-semibold text-gray-900">{type.name}</h3>
                    <p className="text-sm text-gray-600 mt-1">Weight: {type.weight}%</p>
                  </div>
                  <button className="text-red-600 hover:text-red-800">
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
                <p className="text-sm text-gray-600 mb-4">
                  {type.description || 'No description available'}
                </p>
                <div className="flex justify-between items-center text-sm text-gray-500">
                  <span>Created: {new Date(type.createdAt).toLocaleDateString()}</span>
                  <button
                    onClick={() => {
                      setConfigData({ ...configData, assessmentTypeId: type.id })
                      setShowConfigModal(true)
                    }}
                    className="text-blue-600 hover:text-blue-800 font-medium"
                  >
                    Configure
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      )}

      {/* Add Assessment Type Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Add Assessment Type</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="e.g., Mid-term Exam, Quiz, Assignment"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Weight (%)</label>
                <input
                  type="number"
                  required
                  min="0"
                  max="100"
                  value={formData.weight}
                  onChange={(e) => setFormData({ ...formData, weight: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="e.g., 30"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="Describe this assessment type..."
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
                  Create
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Assessment Configuration Modal */}
      {showConfigModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Configure Assessment</h2>
            <form onSubmit={handleConfigSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Assessment Type</label>
                <select
                  required
                  value={configData.assessmentTypeId}
                  onChange={(e) => setConfigData({ ...configData, assessmentTypeId: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                >
                  <option value="">Select Assessment Type</option>
                  {assessmentTypes.map((type) => (
                    <option key={type.id} value={type.id}>
                      {type.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Max Score</label>
                  <input
                    type="number"
                    required
                    min="1"
                    value={configData.maxScore}
                    onChange={(e) => setConfigData({ ...configData, maxScore: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="100"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Passing Score</label>
                  <input
                    type="number"
                    required
                    min="1"
                    value={configData.passingScore}
                    onChange={(e) => setConfigData({ ...configData, passingScore: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="60"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Term</label>
                <select
                  required
                  value={configData.term}
                  onChange={(e) => setConfigData({ ...configData, term: e.target.value })}
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
                  value={configData.academicYear}
                  onChange={(e) => setConfigData({ ...configData, academicYear: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="2024/2025"
                />
              </div>
              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowConfigModal(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                >
                  Configure
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default AssessmentTypes