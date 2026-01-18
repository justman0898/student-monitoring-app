# Student Monitoring System - Frontend

A modern, responsive frontend application for the Student Monitoring System built with React, Vite, and Tailwind CSS.

## Features

- **Dashboard**: Overview of system statistics and quick actions
- **Teachers Management**: Register, view, and manage teachers
- **Classes Management**: Create classes and assign teachers
- **Subjects Management**: Add and manage subjects
- **Parents Portal**: Add parents and view linked students
- **Assessments**: Submit scores and view academic history
- **Analytics**: Performance analysis and weak subject identification

## Tech Stack

- **React 18** - UI library
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **Lucide React** - Icon library

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Backend API running on port 8080

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

## Running the Application

### Development Mode

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Production Build

Build for production:
```bash
npm run build
```

Preview production build:
```bash
npm run preview
```

## API Configuration

The frontend is configured to proxy API requests to `http://localhost:8080`. If your backend runs on a different port, update the `vite.config.js` file:

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:YOUR_PORT',
      changeOrigin: true
    }
  }
}
```

## Project Structure

```
frontend/
├── src/
│   ├── components/      # Reusable components
│   │   └── Layout.jsx   # Main layout with sidebar
│   ├── pages/           # Page components
│   │   ├── Dashboard.jsx
│   │   ├── Teachers.jsx
│   │   ├── Classes.jsx
│   │   ├── Subjects.jsx
│   │   ├── Parents.jsx
│   │   ├── Assessments.jsx
│   │   └── Analytics.jsx
│   ├── services/        # API service layer
│   │   └── api.js
│   ├── App.jsx          # Main app component
│   ├── main.jsx         # Entry point
│   └── index.css        # Global styles
├── index.html
├── package.json
├── vite.config.js
└── tailwind.config.js
```

## Available API Endpoints

The frontend integrates with the following backend endpoints:

### Admin Class APIs
- `POST /api/admin/classes` - Create class
- `GET /api/admin/classes` - Get all classes
- `PATCH /api/admin/classes/:id` - Update class
- `DELETE /api/admin/classes/:id` - Delete class
- `POST /api/admin/classes/:classId/assign/:teacherId` - Assign teacher
- `POST /api/admin/classes/parents` - Add parent

### Admin Teacher APIs
- `POST /api/admin/teachers/register` - Register teacher
- `GET /api/admin/teachers` - Get all teachers
- `GET /api/admin/teachers/:id` - Get teacher by ID
- `DELETE /api/admin/teachers/:id` - Remove teacher

### Subject APIs
- `POST /api/subjects` - Create subject
- `GET /api/subjects` - Get all subjects
- `PATCH /api/subjects/:id` - Update subject
- `DELETE /api/subjects/:id` - Delete subject

### Parent APIs
- `GET /api/parents/:parentId/students` - Get linked students
- `GET /api/parents/students/:studentId/results` - View results

### Assessment APIs
- `POST /api/teacher/assessments/scores` - Submit score
- `PATCH /api/teacher/assessments/scores/:id` - Update score
- `GET /api/teacher/assessments/students/:id/history` - Get academic history

### Analysis APIs
- `GET /api/analysis/students/:id/performance` - Get performance history
- `GET /api/analysis/students/:id/weak-subjects` - Get weak subjects

## Responsive Design

The application is fully responsive and works seamlessly across:
- Mobile devices (320px and up)
- Tablets (768px and up)
- Desktops (1024px and up)
- Large screens (1280px and up)

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

MIT
