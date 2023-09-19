import React from 'react';
import ReactDOM from 'react-dom';
import { createRoot } from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Route, Routes, createBrowserRouter, RouterProvider, Outlet, Navigate } from 'react-router-dom';
import Welcome from './screens/Welcome';
import SignInUpForm from './screens/SignInUpForm';
import Home from './screens/Home';
import Products from './screens/Products';
import Resources from './screens/Resources';
import HeaderProvider from './HeaderContext';
import { Provider } from 'react-redux';
import store from './redux/store';

const router = createBrowserRouter([
  { path: '/', element: <Welcome/> },
  { path: '/signinupform', element: <SignInUpForm/> },
  { path: '/homepage', element: <Home/> },
  { path: '/products', element: <Products/> },
  { path: '/resources', element: <Resources/> },
]);

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <RouterProvider router={router}/>
    </Provider>
  </React.StrictMode>
);