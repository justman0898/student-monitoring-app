import { useEffect, useState } from 'react'
import { Plus, Trash2, UserPlus } from 'lucide-react'
import { classAPI, teacherAPI } from '../services/api'
import api from '../services/api'

const Classes = () => {
  const [classes, setClasses] = useState([])
  const [teachers, setTeachers] = useState([])
  const [loading, setLoading] = useState(true)
  const [showModal, setShowModal] = useState(false)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [selectedClass, setSelectedClass] = useState(null)
  const [formData, setFormData] = useState({
    name: '',
    grade: '',
    section: ''
  })

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      const [classesRes, teachersRes] = await Promise.all([
        classAPI.getAllClasses(),
        teacherAPI.getTeachers()
      ])
      setClasses(classesRes.data || [])
      setTeachers(teachersRes.data || [])
    } catch (error) {
      console.error('Error fetching data:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.createClass(formData)
      setShowModal(false)
      setFormData({ name: '', grade: '', section: '' })
      fetchData()
    } catch (error) {
      console.error('Error creating class:', error)
      alert('Failed to create class')
    }
  }

  const handleDelete = async (classId) => {
    if (window.confirm('Are you sure you want to delete this class?')) {
      try {
        await classAPI.deleteClass(classId)
        fetchData()
      } catch (error) {
        console.error('Error deleting class:', error)
        alert('Failed to delete class')
      }
    }
  }

  const handleAssignTeacher = async (teacherId) => {
    try {
      // Note: Backend expects PATCH /admin/classes/{classId}/assign/{teacherId} but we don't have this endpoint
      // Using the unassign endpoint structure as reference, we'll create a custom call
      await api.patch(`/admin/classes/${selectedClass.id}/assign/${teacherId}`)
      setShowAssignModal(false)
      setSelectedClass(null)
      fetchData()
    } catch (error) {
      console.error('Error assigning teacher:', error)
      alert('Failed to assign teacher')
    }
  }

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Classes</h1>
        <button
          onClick={() => setShowModal(true)}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Create Class
        </button>
      </div>

      {loading ? (
        <div className="text-center py-12">Loading...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {classes.length === 0 ? (
            <div className="col-span-full text-center py-12 text-gray-500">
              No classes found. Create your first class to get started.
            </div>
          ) : (
            classes.map((classItem) => (
              <div key={classItem.id} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition-shadow">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-xl font-semibold text-gray-900">{classItem.name}</h3>
                    <p className="text-sm text-gray-600">Grade {classItem.grade} - Section {classItem.section}</p>
                  </div>
                  <button
                    onClick={() => handleDelete(classItem.id)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
                <div className="space-y-2 mb-4">
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">Students:</span> {classItem.studentCount || 0}
                  </p>
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">Teacher:</span> {classItem.teacherName || 'Not assigned'}
                  </p>
                </div>
                <button
                  onClick={() => {
                    setSelectedClass(classItem)
                    setShowAssignModal(true)
                  }}
                  className="w-full flex items-center justify-center gap-2 px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
                >
                  <UserPlus className="w-4 h-4" />
                  Assign Teacher
                </button>
              </div>
            ))
          )}
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Create Class</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Class Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Grade</label>
                <input
                  type="text"
                  required
                  value={formData.grade}
                  onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Section</label>
                <input
                  type="text"
                  required
                  value={formData.section}
                  onChange={(e) => setFormData({ ...formData, section: e.target.value })}
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
                  Create
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showAssignModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Assign Teacher</h2>
            <p className="text-gray-600 mb-4">Select a teacher for {selectedClass?.name}</p>
            <div className="space-y-2 max-h-96 overflow-y-auto">
              {teachers.map((teacher) => (
                <button
                  key={teacher.id}
                  onClick={() => handleAssignTeacher(teacher.id)}
                  className="w-full text-left p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <p className="font-medium">{teacher.firstName} {teacher.lastName}</p>
                  <p className="text-sm text-gray-600">{teacher.specialization}</p>
                </button>
              ))}
            </div>
            <button
              onClick={() => {
                setShowAssignModal(false)
                setSelectedClass(null)
              }}
              className="w-full mt-4 px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default Classes
