import { useEffect, useState } from 'react'
import { Users, GraduationCap, BookOpen, TrendingUp } from 'lucide-react'
import { teacherAPI, classAPI, subjectAPI } from '../services/api'

const Dashboard = () => {
  const [stats, setStats] = useState({
    teachers: 0,
    classes: 0,
    subjects: 0
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchStats()
  }, [])

  const fetchStats = async () => {
    try {
      const [teachers, classes, subjects] = await Promise.all([
        teacherAPI.getTeachers(),
        classAPI.getAllClasses(),
        subjectAPI.getSubjects()
      ])
      setStats({
        teachers: teachers.data?.length || 0,
        classes: classes.data?.length || 0,
        subjects: subjects.data?.length || 0
      })
    } catch (error) {
      console.error('Error fetching stats:', error)
    } finally {
      setLoading(false)
    }
  }

  const statCards = [
    { title: 'Total Teachers', value: stats.teachers, icon: Users, color: 'bg-blue-500' },
    { title: 'Total Classes', value: stats.classes, icon: GraduationCap, color: 'bg-green-500' },
    { title: 'Total Subjects', value: stats.subjects, icon: BookOpen, color: 'bg-purple-500' },
    { title: 'Performance', value: '85%', icon: TrendingUp, color: 'bg-orange-500' }
  ]

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Dashboard</h1>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {statCards.map((stat, index) => {
          const Icon = stat.icon
          return (
            <div key={index} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition-shadow">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">{stat.title}</p>
                  <p className="text-3xl font-bold text-gray-900">
                    {loading ? '...' : stat.value}
                  </p>
                </div>
                <div className={`${stat.color} p-3 rounded-lg`}>
                  <Icon className="w-6 h-6 text-white" />
                </div>
              </div>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Recent Activity</h2>
          <div className="space-y-4">
            <div className="flex items-center gap-4 p-3 bg-gray-50 rounded-lg">
              <div className="w-2 h-2 bg-green-500 rounded-full"></div>
              <p className="text-sm text-gray-700">New teacher registered</p>
            </div>
            <div className="flex items-center gap-4 p-3 bg-gray-50 rounded-lg">
              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
              <p className="text-sm text-gray-700">Class assignment updated</p>
            </div>
            <div className="flex items-center gap-4 p-3 bg-gray-50 rounded-lg">
              <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
              <p className="text-sm text-gray-700">New subject created</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-4">
            <button className="p-4 bg-primary text-white rounded-lg hover:bg-blue-600 transition-colors">
              Add Teacher
            </button>
            <button className="p-4 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors">
              Create Class
            </button>
            <button className="p-4 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition-colors">
              Add Subject
            </button>
            <button className="p-4 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition-colors">
              View Reports
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
