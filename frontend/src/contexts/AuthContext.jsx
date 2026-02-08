import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Check for existing auth token on app load
    const token = localStorage.getItem('authToken')
    const userRole = localStorage.getItem('userRole')
    const userId = localStorage.getItem('userId')

    if (token && userRole && userId) {
      setUser({
        id: userId,
        role: userRole,
        token: token
      })
    }
    setLoading(false)
  }, [])

  const login = (userData) => {
    setUser(userData)
    localStorage.setItem('authToken', userData.token)
    localStorage.setItem('userRole', userData.role)
    localStorage.setItem('userId', userData.id)
  }

  const logout = () => {
    setUser(null)
    localStorage.removeItem('authToken')
    localStorage.removeItem('userRole')
    localStorage.removeItem('userId')
  }

  const isAuthenticated = () => {
    return !!user && !!user.token
  }

  const hasRole = (role) => {
    return user?.role === role
  }

  const value = {
    user,
    login,
    logout,
    isAuthenticated,
    hasRole,
    loading
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}