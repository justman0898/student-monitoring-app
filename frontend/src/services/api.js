import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// Admin Class APIs
export const classAPI = {
  createClass: (data) => api.post('/admin/classes', data),
  getAllClasses: () => api.get('/admin/classes'),
  updateClass: (classId, patch) => api.patch(`/admin/classes/${classId}`, patch),
  deleteClass: (classId) => api.delete(`/admin/classes/${classId}`),
  assignTeacher: (classId, teacherId) => api.post(`/admin/classes/${classId}/assign/${teacherId}`),
  unassignTeacher: (teacherId) => api.post(`/admin/classes/unassign/${teacherId}`),
  addParent: (data) => api.post('/admin/classes/parents', data)
}

// Admin Teacher APIs
export const teacherAPI = {
  registerTeacher: (data) => api.post('/admin/teachers/register', data),
  getTeachers: () => api.get('/admin/teachers'),
  getTeacher: (teacherId) => api.get(`/admin/teachers/${teacherId}`),
  removeTeacher: (teacherId) => api.delete(`/admin/teachers/${teacherId}`)
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
  getLinkedStudents: (parentId) => api.get(`/parents/${parentId}/students`),
  viewResults: (studentId) => api.get(`/parents/students/${studentId}/results`)
}

// Teacher Assessment APIs
export const assessmentAPI = {
  submitScore: (data) => api.post('/teacher/assessments/scores', data),
  updateScore: (subjectId, patch) => api.patch(`/teacher/assessments/scores/${subjectId}`, patch),
  deleteScore: (subjectId, patch) => api.delete(`/teacher/assessments/scores/${subjectId}`, { data: patch }),
  addSubjectComment: (subjectId, patch) => api.patch(`/teacher/assessments/subjects/${subjectId}/comment`, patch),
  addStudentComment: (studentId, patch) => api.patch(`/teacher/assessments/students/${studentId}/comment`, patch),
  getAcademicHistory: (studentId) => api.get(`/teacher/assessments/students/${studentId}/history`)
}

// Analysis APIs
export const analysisAPI = {
  getPerformanceHistory: (studentId) => api.get(`/analysis/students/${studentId}/performance`),
  getWeakSubjects: (studentId) => api.get(`/analysis/students/${studentId}/weak-subjects`)
}

export default api
