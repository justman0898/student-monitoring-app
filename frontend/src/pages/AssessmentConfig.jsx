import { useEffect, useState } from 'react'
import { Plus, Settings, Eye } from 'lucide-react'
import { classAPI } from '../services/api'

const AssessmentConfig = () => {
  const [assessmentTypes, setAssessmentTypes] = useState([])
  const [loading, setLoading] = useState(true)
  const [showTypeModal, setShowTypeModal] = useState(false)
  const [showConfigModal, setShowConfigModal] = useState(false)
  const [typeFormData, setTypeFormData] = useState({
    name: '',
    description: ''
  })
  const [configFormData, setConfigFormData] = useState({
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

  const handleTypeSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.createAssessmentType(typeFormData)
      setShowTypeModal(false)
      setTypeFormData({ name: '', description: '' })
      fetchAssessmentTypes()
      alert('Assessment type created successfully!')
    } catch (error) {
      console.error('Error creating assessment type:', error)
      alert('Failed to create assessment type')
    }
  }

  const handleConfigSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.createAssessmentConfig(configFormData)
      setShowConfigModal(false)
      setConfigFormData({ assessmentTypeId: '', maxScore: '', passingScore: '', term: '', academicYear: '' })
      alert('Assessment configuration created successfully!')
    } catch (error) {
      console.error('Error creating assessment config:', error)
      alert('Failed to create assessment configuration')
    }
  }

  const viewAssessmentType = async (assessmentTypeId) => {
    try {
      const response = await classAPI.getAssessmentType(assessmentTypeId)
      alert(`Assessment Type Details: ${JSON.stringify(response.data, null, 2)}`)
    } catch (error) {
      console.error('Error fetching assessment type:', error)
      alert('Failed to fetch assessment type details')
    }
  }

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Assessment Configuration</h1>
        <div className="flex gap-3">
          <button
            onClick={() => setShowTypeModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            <Plus className="w-5 h-5" />
            Add Assessment Type
          </button>
          <button
            onClick={() => setShowConfigModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
          >
            <Settings className="w-5 h-5" />
            Configure Assessment
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Assessment Types</h2>
          {loading ? (
            <div className="text-center py-8">Loading...</div>
          ) : (
            <div className="space-y-3">
              {assessmentTypes.length === 0 ? (
                <div className="text-center py-8 text-gray-500">
                  No assessment types found. Create your first assessment type to get started.
                </div>
              ) : (
                assessmentTypes.map((type) => (
                  <div key={type.id} className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="font-semibold text-gray-900">{type.name}</h3>
                        <p className="text-sm text-gray-600 mt-1">{type.description}</p>
                      </div>
                      <button
                        onClick={() => viewAssessmentType(type.id)}
                        className="text-blue-600 hover:text-blue-800"
                      >
                        <Eye className="w-5 h-5" />
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Quick Actions</h2>
          <div className="space-y-3">
            <button
              onClick={() => setShowTypeModal(true)}
              className="w-full p-4 text-left bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors"
            >
              <p className="font-medium text-blue-900">Create Assessment Type</p>
              <p className="text-sm text-blue-700">Define new types of assessments (Quiz, Exam, etc.)</p>
            </button>
            <button
              onClick={() => setShowConfigModal(true)}
              className="w-full p-4 text-left bg-green-50 hover:bg-green-100 rounded-lg transition-colors"
            >
              <p className="font-medium text-green-900">Configure Assessment</p>
              <p className="text-sm text-green-700">Set scoring rules and parameters</p>
            </button>
            <button
              onClick={fetchAssessmentTypes}
              className="w-full p-4 text-left bg-purple-50 hover:bg-purple-100 rounded-lg transition-colors"
            >
              <p className="font-medium text-purple-900">Refresh Types</p>
              <p className="text-sm text-purple-700">Reload assessment types list</p>
            </button>
          </div>
        </div>
      </div>

      {/* Assessment Type Modal */}
      {showTypeModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Create Assessment Type</h2>
            <form onSubmit={handleTypeSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Type Name</label>
                <input
                  type="text"
                  required
                  value={typeFormData.name}
                  onChange={(e) => setTypeFormData({ ...typeFormData, name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="e.g., Quiz, Midterm Exam, Final Exam"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea
                  value={typeFormData.description}
                  onChange={(e) => setTypeFormData({ ...typeFormData, description: e.target.value })}
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                  placeholder="Describe this assessment type..."
                />
              </div>
              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowTypeModal(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600"
                >
                  Create Type
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Assessment Config Modal */}
      {showConfigModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Configure Assessment</h2>
            <form onSubmit={handleConfigSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Assessment Type</label>
                <select
                  required
                  value={configFormData.assessmentTypeId}
                  onChange={(e) => setConfigFormData({ ...configFormData, assessmentTypeId: e.target.value })}
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
                    value={configFormData.maxScore}
                    onChange={(e) => setConfigFormData({ ...configFormData, maxScore: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="100"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Passing Score</label>
                  <input
                    type="number"
                    required
                    value={configFormData.passingScore}
                    onChange={(e) => setConfigFormData({ ...configFormData, passingScore: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                    placeholder="60"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Term</label>
                <select
                  required
                  value={configFormData.term}
                  onChange={(e) => setConfigFormData({ ...configFormData, term: e.target.value })}
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
                  value={configFormData.academicYear}
                  onChange={(e) => setConfigFormData({ ...configFormData, academicYear: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
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
                  className="flex-1 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
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

export default AssessmentConfig