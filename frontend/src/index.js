import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import Welcome from './screens/Welcome';
import SignInUpForm from './screens/SignInUpForm';
import Home from './screens/Home';
import Products from './screens/Products';
import Resources from './screens/Resources';

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
    <RouterProvider router={router}/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
