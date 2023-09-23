import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
  email: '',
  token: '',
  isLoading: false,
  error: null,
};

export const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    loginRequest: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    loginSuccess: (state, action) => {
      state.isLoading = false;
      state.email = action.payload.email;
      state.token = action.payload.token;
    },
    loginFailure: (state, action) => {
      state.isLoading = false;
      state.error = action.payload;
    },
  },
});

export const { loginRequest, loginSuccess, loginFailure } = loginSlice.actions;

// Async action to perform the login
export const loginUser = (email, password) => async (dispatch) => {
  dispatch(loginRequest());

  try {
    const response = await axios.post('http://localhost:8080/api/v1/auth/authentication', {
      email,
      password,
    });

    const { email, token } = response.data;

    dispatch(loginSuccess({ email, token }));
    // Redirige a la página deseada después del inicio de sesión exitoso utilizando React Router v6
  } catch (error) {
    dispatch(loginFailure(error.message || 'Error desconocido'));
  }
};

// Selector para acceder a los datos de inicio de sesión desde el estado de Redux
export const selectLogin = (state) => state.login;

export default loginSlice.reducer;