import { Link } from 'react-router-dom'
import { Users, GraduationCap, BookOpen, TrendingUp, Shield, Clock, Award } from 'lucide-react'

const Home = () => {
  const features = [
    {
      icon: Users,
      title: 'Multi-Role Access',
      description: 'Separate dashboards for administrators, teachers, and parents with role-based permissions.'
    },
    {
      icon: GraduationCap,
      title: 'Class Management',
      description: 'Efficiently organize classes, assign teachers, and manage student enrollments.'
    },
    {
      icon: BookOpen,
      title: 'Subject Tracking',
      description: 'Comprehensive subject management with assessment types and grading configurations.'
    },
    {
      icon: TrendingUp,
      title: 'Performance Analytics',
      description: 'Real-time analytics and insights into student performance and academic progress.'
    },
    {
      icon: Shield,
      title: 'Secure Platform',
      description: 'Enterprise-grade security with JWT authentication and encrypted data storage.'
    },
    {
      icon: Clock,
      title: 'Real-time Updates',
      description: 'Instant notifications and updates on student progress and academic activities.'
    }
  ]

  const userTypes = [
    {
      title: 'Administrators',
      description: 'Manage the entire school system, teachers, classes, and generate comprehensive reports.',
      features: ['Teacher Management', 'Class Organization', 'System Analytics', 'Parent Account Creation'],
      color: 'bg-blue-500',
      link: '/login'
    },
    {
      title: 'Teachers',
      description: 'Submit grades, track student progress, and manage classroom assessments efficiently.',
      features: ['Grade Submission', 'Student Progress', 'Assessment Management', 'Class Analytics'],
      color: 'bg-green-500',
      link: '/login'
    },
    {
      title: 'Parents',
      description: 'Monitor your children\'s academic progress and stay connected with their education.',
      features: ['Student Progress', 'Grade Reports', 'Performance Analytics', 'Academic History'],
      color: 'bg-purple-500',
      link: '/login'
    }
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <h1 className="text-2xl font-bold text-blue-600">Student Monitor</h1>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <Link
                to="/login"
                className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                Sign In
              </Link>
              <Link
                to="/register"
                className="bg-blue-600 text-white hover:bg-blue-700 px-4 py-2 rounded-md text-sm font-medium"
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
          <div className="text-center">
            <h1 className="text-4xl md:text-6xl font-bold mb-6">
              Student Monitoring System
            </h1>
            <p className="text-xl md:text-2xl mb-8 text-blue-100">
              Comprehensive academic tracking and management platform for schools
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                to="/register"
                className="bg-white text-blue-600 hover:bg-gray-100 px-8 py-3 rounded-lg text-lg font-semibold transition-colors"
              >
                Get Started Free
              </Link>
              <Link
                to="/login"
                className="border-2 border-white text-white hover:bg-white hover:text-blue-600 px-8 py-3 rounded-lg text-lg font-semibold transition-colors"
              >
                Sign In
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="py-24 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Powerful Features for Modern Education
            </h2>
            <p className="text-xl text-gray-600">
              Everything you need to manage and monitor student academic progress
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => {
              const Icon = feature.icon
              return (
                <div key={index} className="text-center p-6 rounded-xl hover:shadow-lg transition-shadow">
                  <div className="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                    <Icon className="w-8 h-8 text-blue-600" />
                  </div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">{feature.title}</h3>
                  <p className="text-gray-600">{feature.description}</p>
                </div>
              )
            })}
          </div>
        </div>
      </div>

      {/* User Types Section */}
      <div className="py-24 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Built for Every Role
            </h2>
            <p className="text-xl text-gray-600">
              Tailored experiences for administrators, teachers, and parents
            </p>
          </div>
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {userTypes.map((userType, index) => (
              <div key={index} className="bg-white rounded-xl shadow-lg p-8 hover:shadow-xl transition-shadow">
                <div className={`${userType.color} w-12 h-12 rounded-lg flex items-center justify-center mb-6`}>
                  <Award className="w-6 h-6 text-white" />
                </div>
                <h3 className="text-2xl font-bold text-gray-900 mb-4">{userType.title}</h3>
                <p className="text-gray-600 mb-6">{userType.description}</p>
                <ul className="space-y-2 mb-8">
                  {userType.features.map((feature, featureIndex) => (
                    <li key={featureIndex} className="flex items-center text-gray-700">
                      <div className="w-2 h-2 bg-green-500 rounded-full mr-3"></div>
                      {feature}
                    </li>
                  ))}
                </ul>
                <Link
                  to={userType.link}
                  className={`${userType.color} text-white hover:opacity-90 px-6 py-3 rounded-lg font-semibold transition-opacity block text-center`}
                >
                  Access Dashboard
                </Link>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-blue-600 text-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Ready to Transform Your School Management?
          </h2>
          <p className="text-xl text-blue-100 mb-8">
            Join thousands of schools already using our platform
          </p>
          <Link
            to="/register"
            className="bg-white text-blue-600 hover:bg-gray-100 px-8 py-4 rounded-lg text-lg font-semibold transition-colors inline-block"
          >
            Start Your Free Trial
          </Link>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <h3 className="text-xl font-bold mb-4">Student Monitor</h3>
              <p className="text-gray-400">
                Comprehensive academic tracking and management platform for modern schools.
              </p>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Features</h4>
              <ul className="space-y-2 text-gray-400">
                <li>Class Management</li>
                <li>Grade Tracking</li>
                <li>Performance Analytics</li>
                <li>Parent Portal</li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Users</h4>
              <ul className="space-y-2 text-gray-400">
                <li>Administrators</li>
                <li>Teachers</li>
                <li>Parents</li>
                <li>Students</li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold mb-4">Support</h4>
              <ul className="space-y-2 text-gray-400">
                <li>Documentation</li>
                <li>Help Center</li>
                <li>Contact Us</li>
                <li>System Status</li>
              </ul>
            </div>
          </div>
          <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2024 Student Monitor. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}

export default Home