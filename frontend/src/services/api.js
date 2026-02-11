import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/v1',
  headers: {
    'Content-Type': 'application/json'
  }
})

// Helper function to trim all string values in an object
const trimObjectValues = (obj) => {
  if (obj === null || obj === undefined) return obj
  if (typeof obj === 'string') return obj.trim()
  if (Array.isArray(obj)) return obj.map(trimObjectValues)
  if (typeof obj === 'object') {
    const trimmed = {}
    for (const [key, value] of Object.entries(obj)) {
      trimmed[key] = trimObjectValues(value)
    }
    return trimmed
  }
  return obj
}

// Add request interceptor to include auth token and trim inputs
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // Trim all string values in request data
    if (config.data) {
      config.data = trimObjectValues(config.data)
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Add response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Admin Class APIs
export const classAPI = {
  createClass: (data) => api.post('/admin/classes/create', data),
  getAllClasses: () => api.get('/admin/classes'),
  updateClass: (classId, patch) => api.patch(`/admin/classes/${classId}`, patch),
  deleteClass: (classId) => api.delete(`/admin/classes/de-activate/${classId}`),
  assignTeacher: (classId, teacherId) => api.patch(`/admin/classes/${classId}/assign/${teacherId}`),
  unassignTeacher: (classId, teacherId) => api.patch(`/admin/classes/${classId}/un-assign/${teacherId}`),
  addParent: (data) => api.post('/admin/classes/create-parent', data),
  updateParent: (data) => api.patch('/admin/classes/update-parent', data),
  registerStudent: (data) => api.post('/admin/classes/students', data),
  createAssessmentType: (data) => api.post('/admin/classes/assessment-type', data),
  createAssessmentConfig: (data) => api.post('/admin/classes/assessment-config', data),
  getAssessmentType: (assessmentTypeId) => api.get(`/admin/classes/${assessmentTypeId}`),
  getAllAssessmentTypes: () => api.get('/admin/classes/assessment-types')
}

// Admin Teacher APIs
export const teacherAPI = {
  registerTeacher: (data) => api.post('/teachers', data),
  getTeachers: () => api.get('/teachers'),
  getTeacher: (teacherId) => api.get(`/teachers/${teacherId}`),
  removeTeacher: (teacherId) => api.delete(`/teachers/${teacherId}`),
  getProfile: (teacherId) => api.get(`/teachers/me`),
  getStudentsInClass: (classId) => api.get(`/teachers/classes/${classId}/students`)
}

// Subject APIs
export const subjectAPI = {
  createSubject: (data) => api.post('/subjects', data),
  getSubjects: () => api.get('/subjects'),
  updateSubject: (subjectId, patch) => api.patch(`/subjects/${subjectId}`, patch),
  deleteSubject: (subjectId) => api.delete(`/subjects/${subjectId}`)
}

// Parent APIs
export const parentAPI = {
  getLinkedStudents: (parentId) => api.get(`/parent/students/${parentId}`),
  viewResults: (studentId) => api.get(`/parent/${studentId}`)
}

// Teacher Assessment APIs
export const assessmentAPI = {
  submitScore: (data) => api.post('/teachers/submit', data),
  updateScore: (patch) => api.patch('/teachers/update-score', patch),
  deleteScore: (scoreId) => api.delete(`/teachers/delete/${scoreId}`),
  addComment: (data) => api.post('/teachers/comment', data),
  getAcademicHistory: (studentId) => api.get(`/teachers/${studentId}/academic-history`)
}

// Analysis APIs
export const analysisAPI = {
  getPerformanceHistory: (studentId) => api.get(`/analysis/students/${studentId}/performance`),
  getWeakSubjects: (studentId) => api.get(`/analysis/weak-subjects`)
}

// Authentication APIs
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data)
}

export default api
