
import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import userReducer from './userSlice';
import navigationReducer from './navSlice';

const store = configureStore({
  reducer: {
    login: loginReducer,
    users: userReducer,
    navigation: navigationReducer,
  },
});

export default store;