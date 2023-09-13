import './App.css';
import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import WelcomePage from './screens/WelcomePage';
import SignInUpForm from './screens/SignInUpForm';
import HomePage from './screens/HomePage';

function App() {
  const [showWelcome, setShowWelcome] = useState(true);

  const handleWelcomeComplete = () => {
    setShowWelcome(false);
  };

  useEffect(() => {
    if (showWelcome) {
      const timer = setTimeout(() => {
        handleWelcomeComplete();
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [showWelcome]);

  return (
    <div className="App">
      <div className={`content ${showWelcome ? "" : ""}`}>
        {showWelcome ? (<WelcomePage onWelcomeComplete={handleWelcomeComplete} />) : (<SignInUpForm />)}
      </div>
    </div>
  );
}

export default App;