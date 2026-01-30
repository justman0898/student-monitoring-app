import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json'
  }
})

// Add request interceptor to include auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Add response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken')
      localStorage.removeItem('userRole')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Authentication APIs
export const authAPI = {
  adminLogin: (credentials) => api.post('/admin/login', credentials),
  teacherLogin: (credentials) => api.post('/teacher/login', credentials),
  parentLogin: (credentials) => api.post('/parent/login', credentials)
}

// Admin Class APIs
export const classAPI = {
  createClass: (data) => api.post('/admin/classes/create', data),
  getAllClasses: () => api.get('/admin/classes'),
  updateClass: (classId, patch) => api.patch(`/admin/classes/${classId}`, patch),
  deleteClass: (classId) => api.delete(`/admin/classes/de-activate/${classId}`),
  assignTeacher: (classId, teacherId) => api.post(`/admin/classes/${classId}/assign/${teacherId}`),
  unassignTeacher: (classId, teacherId) => api.patch(`/admin/classes/${classId}/un-assign/${teacherId}`),
  addParent: (data) => api.post('/admin/classes/create-parent', data),
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
  getProfile: (teacherId) => api.get('/teachers/me'),
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
  updateScore: (subjectId, patch) => api.patch(`/teachers/${subjectId}`, patch),
  deleteScore: (subjectId, patch) => api.delete(`/teachers/${subjectId}`, { data: patch }),
  addSubjectComment: (subjectId, patch) => api.patch(`/teachers/${subjectId}/subject-comment`, patch),
  addStudentComment: (studentId, patch) => api.patch(`/teachers/${studentId}/comment`, patch),
  getAcademicHistory: (studentId) => api.get(`/teachers/${studentId}/academic-history`)
}

// Analysis APIs
export const analysisAPI = {
  getPerformanceHistory: (studentId) => api.get(`/analysis/students/${studentId}/performance`),
  getWeakSubjects: (studentId) => api.get(`/analysis/weak-subjects?studentId=${studentId}`)
}

export default api
