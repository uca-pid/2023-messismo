import React from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Form from './components/Form';
import Home from './components/Home'
import Navbar from './components/Navbar';
import Menu from './components/Menu';
import Products from './components/Products';


function App() {
  
  return (
    <BrowserRouter>
      <div style={styles.container}>
      <Navbar></Navbar>
      <div style={styles.content}>
      <Routes>
        <Route path="/menu" Component={Menu}></Route>
        <Route path="/products" Component={Form}></Route>
        <Route path="/" Component={Products}></Route>
      </Routes>
      </div>
      </div>
    </BrowserRouter>
  );
}


const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
  },
  content: {
    flex: 1, 
    display: 'flex',
    
  },
};
export default App;