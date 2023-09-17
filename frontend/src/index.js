import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import Home from './components/Home';
import Menu from './components/Menu';
import Card from './components/Card';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter, Route } from 'react-router-dom'
import Form from './components/Form';
import Navbar from './components/Navbar';
import Products from './components/Products';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);


reportWebVitals();