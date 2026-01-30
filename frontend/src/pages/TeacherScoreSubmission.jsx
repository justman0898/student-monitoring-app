import { useState, useEffect } from 'react'
import { Plus, Save, ArrowLeft } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { assessmentAPI, teacherAPI, subjectAPI, classAPI } from '../services/api'
import { useAuth } from '../contexts/AuthContext'

const TeacherScoreSubmission = () => {
  const [students, setStudents] = useState([])
  const [subjects, setSubjects] = useState([])
  const [assessmentTypes, setAssessmentTypes] = useState([])
  const [selectedClass, setSelectedClass] = useState('')
  const [selectedSubject, setSelectedSubject] = useState('')
  const [selectedAssessmentType, setSelectedAssessmentType] = useState('')
  const [scores, setScores] = useState([])
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const { user } = useAuth()

  useEffect(() => {
    fetchInitialData()
  }, [])

  const fetchInitialData = async () => {
    try {
      const [subjectsRes, assessmentTypesRes] = await Promise.all([
        subjectAPI.getSubjects(),
        classAPI.getAllAssessmentTypes()
      ])
      setSubjects(subjectsRes.data || [])
      setAssessmentTypes(assessmentTypesRes.data || [])
    } catch (error) {
      console.error('Error fetching initial data:', error)
    }
  }

  const fetchStudents = async (classId) => {
    if (!classId) return
    try {
      const response = await teacherAPI.getStudentsInClass(classId)
      const studentsData = response.data || []
      setStudents(studentsData)
      
      // Initialize scores array
      setScores(studentsData.map(student => ({
        studentId: student.id,
        studentName: `${student.firstName} ${student.lastName}`,
        score: '',
        maxScore: 100,
        comment: ''
      })))
    } catch (error) {
      console.error('Error fetching students:', error)
    }
  }

  const handleScoreChange = (studentId, field, value) => {
    setScores(prev => prev.map(score => 
      score.studentId === studentId 
        ? { ...score, [field]: value }
        : score
    ))
  }

  const handleSubmitAll = async () => {
    if (!selectedSubject || !selectedAssessmentType) {
      alert('Please select subject and assessment type')
      return
    }

    const validScores = scores.filter(score => score.score !== '' && score.score !== null)
    if (validScores.length === 0) {
      alert('Please enter at least one score')
      return
    }

    setLoading(true)
    try {
      const submissions = validScores.map(score => ({
        studentId: score.studentId,
        subjectId: selectedSubject,
        assessmentTypeId: selectedAssessmentType,
        score: parseFloat(score.score),
        maxScore: parseFloat(score.maxScore),
        comment: score.comment,
        term: '1', // You might want to make this dynamic
        academicYear: '2024/2025' // You might want to make this dynamic
      }))

      // Submit each score
      await Promise.all(submissions.map(submission => 
        assessmentAPI.submitScore(submission)
      ))

      alert(`Successfully submitted ${submissions.length} scores!`)
      navigate('/teacher-dashboard')
    } catch (error) {
      console.error('Error submitting scores:', error)
      alert('Failed to submit scores. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center h-16">
            <button
              onClick={() => navigate('/teacher-dashboard')}
              className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mr-4"
            >
              <ArrowLeft className="w-5 h-5" />
              Back to Dashboard
            </button>
            <h1 className="text-2xl font-bold text-gray-900">Submit Scores</h1>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Selection Form */}
        <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
          <h2 className="text-xl font-semibold mb-4">Assessment Details</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Class</label>
              <select
                value={selectedClass}
                onChange={(e) => {
                  setSelectedClass(e.target.value)
                  fetchStudents(e.target.value)
                }}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="">Select Class</option>
                {/* You'll need to get teacher's classes from profile */}
                <option value="class1">Class 1A</option>
                <option value="class2">Class 2B</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Subject</label>
              <select
                value={selectedSubject}
                onChange={(e) => setSelectedSubject(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="">Select Subject</option>
                {subjects.map(subject => (
                  <option key={subject.id} value={subject.id}>
                    {subject.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Assessment Type</label>
              <select
                value={selectedAssessmentType}
                onChange={(e) => setSelectedAssessmentType(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="">Select Assessment Type</option>
                {assessmentTypes.map(type => (
                  <option key={type.id} value={type.id}>
                    {type.name}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Scores Table */}
        {students.length > 0 && (
          <div className="bg-white rounded-xl shadow-sm overflow-hidden">
            <div className="px-6 py-4 border-b">
              <div className="flex justify-between items-center">
                <h2 className="text-xl font-semibold">Student Scores</h2>
                <button
                  onClick={handleSubmitAll}
                  disabled={loading}
                  className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
                >
                  <Save className="w-5 h-5" />
                  {loading ? 'Submitting...' : 'Submit All Scores'}
                </button>
              </div>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Student</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Score</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Max Score</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Percentage</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Comment</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {scores.map((scoreData, index) => {
                    const percentage = scoreData.score && scoreData.maxScore 
                      ? Math.round((scoreData.score / scoreData.maxScore) * 100)
                      : 0
                    
                    return (
                      <tr key={scoreData.studentId} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="font-medium text-gray-900">
                            {scoreData.studentName}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <input
                            type="number"
                            min="0"
                            step="0.1"
                            value={scoreData.score}
                            onChange={(e) => handleScoreChange(scoreData.studentId, 'score', e.target.value)}
                            className="w-20 px-2 py-1 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="0"
                          />
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <input
                            type="number"
                            min="1"
                            value={scoreData.maxScore}
                            onChange={(e) => handleScoreChange(scoreData.studentId, 'maxScore', e.target.value)}
                            className="w-20 px-2 py-1 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 rounded-full text-sm font-medium ${
                            percentage >= 80 ? 'bg-green-100 text-green-800' :
                            percentage >= 60 ? 'bg-yellow-100 text-yellow-800' :
                            percentage > 0 ? 'bg-red-100 text-red-800' :
                            'bg-gray-100 text-gray-800'
                          }`}>
                            {percentage}%
                          </span>
                        </td>
                        <td className="px-6 py-4">
                          <input
                            type="text"
                            value={scoreData.comment}
                            onChange={(e) => handleScoreChange(scoreData.studentId, 'comment', e.target.value)}
                            className="w-full px-2 py-1 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="Optional comment..."
                          />
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {students.length === 0 && selectedClass && (
          <div className="bg-white rounded-xl shadow-sm p-12 text-center">
            <p className="text-gray-500">No students found in the selected class.</p>
          </div>
        )}

        {!selectedClass && (
          <div className="bg-white rounded-xl shadow-sm p-12 text-center">
            <p className="text-gray-500">Please select a class to view students.</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default TeacherScoreSubmission