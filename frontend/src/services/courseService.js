import api from './api';

export const courseService = {
  // Get all published courses
  getPublishedCourses: async (params = {}) => {
    const { page = 0, size = 10, sortBy = 'createdAt', sortDir = 'desc' } = params;
    const response = await api.get('/courses/public', {
      params: { page, size, sortBy, sortDir }
    });
    return response;
  },

  // Search courses
  searchCourses: async (filters = {}) => {
    const response = await api.get('/courses/search', { params: filters });
    return response;
  },

  // Get course by ID
  getCourseById: async (id) => {
    const response = await api.get(`/courses/${id}`);
    return response;
  },

  // Create course (instructor/admin)
  createCourse: async (courseData) => {
    const response = await api.post('/courses', courseData);
    return response;
  },

  // Update course
  updateCourse: async (id, courseData) => {
    const response = await api.put(`/courses/${id}`, courseData);
    return response;
  },

  // Enroll in course
  enrollInCourse: async (courseId) => {
    const response = await api.post(`/courses/${courseId}/enroll`);
    return response;
  },

  // Publish course
  publishCourse: async (courseId) => {
    const response = await api.post(`/courses/${courseId}/publish`);
    return response;
  },

  // Get course statistics
  getCourseStatistics: async () => {
    const response = await api.get('/courses/statistics');
    return response;
  },
};

export const quizService = {
  // Get quiz by ID
  getQuizById: async (id) => {
    const response = await api.get(`/quizzes/${id}`);
    return response;
  },

  // Get quizzes for course
  getQuizzesByCourse: async (courseId) => {
    const response = await api.get(`/quizzes/course/${courseId}`);
    return response;
  },

  // Create quiz
  createQuiz: async (quizData) => {
    const response = await api.post('/quizzes', quizData);
    return response;
  },

  // Submit quiz
  submitQuiz: async (quizId, answers) => {
    const response = await api.post(`/quizzes/${quizId}/submit`, answers);
    return response;
  },

  // Update quiz
  updateQuiz: async (id, quizData) => {
    const response = await api.put(`/quizzes/${id}`, quizData);
    return response;
  },
};