import api from './api';

export const authService = {
  // Login user
  login: async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    return response;
  },

  // Register user
  register: async (userData) => {
    const response = await api.post('/auth/register', userData);
    return response;
  },

  // Validate token
  validateToken: async (token) => {
    const response = await api.post('/auth/validate', { token });
    return response;
  },

  // Get user profile
  getProfile: async () => {
    const response = await api.get('/auth/profile');
    return response;
  },

  // Logout (client-side only)
  logout: () => {
    localStorage.removeItem('auth_token');
    return Promise.resolve();
  },
};