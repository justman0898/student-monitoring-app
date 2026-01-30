import { useState } from 'react'
import { Search, TrendingDown, TrendingUp } from 'lucide-react'
import { analysisAPI } from '../services/api'

const Analytics = () => {
  const [searchStudentId, setSearchStudentId] = useState('')
  const [performanceData, setPerformanceData] = useState(null)
  const [weakSubjects, setWeakSubjects] = useState([])
  const [loading, setLoading] = useState(false)

  const handleSearch = async () => {
    if (!searchStudentId) return
    setLoading(true)
    try {
      const [performance, weak] = await Promise.all([
        analysisAPI.getPerformanceHistory(searchStudentId),
        analysisAPI.getWeakSubjects(searchStudentId)
      ])
      setPerformanceData(performance.data)
      setWeakSubjects(weak.data || [])
    } catch (error) {
      console.error('Error fetching analytics:', error)
      alert('Failed to fetch analytics data')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Analytics</h1>

      <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4">Student Performance Analysis</h2>
        <div className="flex gap-3">
          <input
            type="text"
            placeholder="Enter Student ID"
            value={searchStudentId}
            onChange={(e) => setSearchStudentId(e.target.value)}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
          />
          <button
            onClick={handleSearch}
            disabled={loading}
            className="flex items-center gap-2 px-6 py-2 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors disabled:opacity-50"
          >
            <Search className="w-5 h-5" />
            {loading ? 'Loading...' : 'Analyze'}
          </button>
        </div>
        <div className="mt-4 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
          <p className="text-sm text-yellow-800">
            <strong>Note:</strong> Analytics endpoints are currently being implemented. 
            Some features may not return data until the backend analysis service is fully configured.
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm p-6">
          <div className="flex items-center gap-2 mb-4">
            <TrendingUp className="w-6 h-6 text-green-600" />
            <h2 className="text-xl font-semibold">Performance Trends</h2>
          </div>
          {performanceData ? (
            <div className="space-y-4">
              <div className="p-4 bg-green-50 rounded-lg">
                <p className="text-sm text-gray-600">Overall Average</p>
                <p className="text-2xl font-bold text-green-700">
                  {performanceData.average || 'N/A'}%
                </p>
              </div>
              <div className="space-y-2">
                <p className="font-medium text-gray-700">Recent Performance:</p>
                {performanceData.history?.map((item, index) => (
                  <div key={index} className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="text-sm text-gray-700">{item.subject}</span>
                    <span className="font-medium text-gray-900">{item.score}%</span>
                  </div>
                )) || <p className="text-sm text-gray-500">No performance data available</p>}
              </div>
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              Search for a student to view performance trends
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6">
          <div className="flex items-center gap-2 mb-4">
            <TrendingDown className="w-6 h-6 text-red-600" />
            <h2 className="text-xl font-semibold">Areas for Improvement</h2>
          </div>
          {weakSubjects.length > 0 ? (
            <div className="space-y-3">
              {weakSubjects.map((subject, index) => (
                <div key={index} className="p-4 bg-red-50 rounded-lg">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-medium text-red-900">{subject.name}</p>
                      <p className="text-sm text-red-700 mt-1">{subject.reason}</p>
                    </div>
                    <span className="px-3 py-1 bg-red-200 text-red-800 rounded-full text-sm font-medium">
                      {subject.score}%
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              {performanceData 
                ? 'No weak subjects identified' 
                : 'Search for a student to identify weak subjects'}
            </div>
          )}
        </div>
      </div>

      <div className="mt-6 bg-white rounded-xl shadow-sm p-6">
        <h2 className="text-xl font-semibold mb-4">Class Performance Overview</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="p-4 bg-blue-50 rounded-lg">
            <p className="text-sm text-gray-600 mb-1">Top Performers</p>
            <p className="text-2xl font-bold text-blue-700">15</p>
            <p className="text-xs text-gray-500 mt-1">Students above 85%</p>
          </div>
          <div className="p-4 bg-yellow-50 rounded-lg">
            <p className="text-sm text-gray-600 mb-1">Average Performers</p>
            <p className="text-2xl font-bold text-yellow-700">42</p>
            <p className="text-xs text-gray-500 mt-1">Students 60-85%</p>
          </div>
          <div className="p-4 bg-red-50 rounded-lg">
            <p className="text-sm text-gray-600 mb-1">Need Support</p>
            <p className="text-2xl font-bold text-red-700">8</p>
            <p className="text-xs text-gray-500 mt-1">Students below 60%</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Analytics
