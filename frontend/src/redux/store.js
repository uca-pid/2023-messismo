
import { configureStore } from '@reduxjs/toolkit';
import authReducer from "./auth";
import messageReducer from "./message";
import userReducer from './userSlice';
import navigationReducer from './navSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    message: messageReducer,
    users: userReducer,
    navigation: navigationReducer,
  },
  devTools: true,
});

export default store;