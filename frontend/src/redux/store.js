
import { configureStore } from '@reduxjs/toolkit';
import authReducer from "./auth";
import messageReducer from "./message";
import userReducer from './userSlice';
import navigationReducer from './navSlice';
import filtersReducer from './filtersSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    message: messageReducer,
    users: userReducer,
    navigation: navigationReducer,
    filters: filtersReducer,
  },
  devTools: true,
});

export default store;