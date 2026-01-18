import { useState } from 'react'
import { Plus, Search } from 'lucide-react'
import { classAPI, parentAPI } from '../services/api'

const Parents = () => {
  const [showModal, setShowModal] = useState(false)
  const [searchParentId, setSearchParentId] = useState('')
  const [linkedStudents, setLinkedStudents] = useState([])
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: ''
  })

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await classAPI.addParent(formData)
      setShowModal(false)
      setFormData({ firstName: '', lastName: '', email: '', phoneNumber: '', address: '' })
      alert('Parent added successfully!')
    } catch (error) {
      console.error('Error adding parent:', error)
      alert('Failed to add parent')
    }
  }

  const handleSearch = async () => {
    if (!searchParentId) return
    try {
      const response = await parentAPI.getLinkedStudents(searchParentId)
      setLinkedStudents(response.data || [])
    } catch (error) {
      console.error('Error fetching linked students:', error)
      alert('Failed to fetch linked students')
    }
  }

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Parents</h1>
        <button
          onClick={() => setShowModal(true)}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Add Parent
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Search Parent</h2>
        <div className="flex gap-3">
          <input
            type="text"
            placeholder="Enter Parent ID"
            value={searchParentId}
            onChange={(e) => setSearchParentId(e.target.value)}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
          />
          <button
            onClick={handleSearch}
            className="flex items-center gap-2 px-6 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors"
          >
            <Search className="w-5 h-5" />
            Search
          </button>
        </div>
      </div>

      {linkedStudents.length > 0 && (
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Linked Students</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {linkedStudents.map((student) => (
              <div key={student.id} className="p-4 border border-gray-200 rounded-lg">
                <h3 className="font-semibold text-gray-900">{student.name}</h3>
                <p className="text-sm text-gray-600">Class: {student.className}</p>
                <p className="text-sm text-gray-600">Grade: {student.grade}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h2 className="text-2xl font-bold mb-4">Add Parent</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <input
                  type="text"
                  required
                  value={formData.firstName}
                  onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <input
                  type="text"
                  required
                  value={formData.lastName}
                  onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                <input
                  type="tel"
                  required
                  value={formData.phoneNumber}
                  onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Address</label>
                <textarea
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  rows="2"
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
                  Add Parent
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default Parents
