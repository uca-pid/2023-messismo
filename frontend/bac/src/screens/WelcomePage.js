import '../App.css';
import React, { useEffect, useState } from "react";
import styled from 'styled-components';

const WelcomeImage = styled.img`
  max-width: 100%;
  height: auto;
  display: flex;
  position: relative;
  margin: 30rem auto;
`;

function WelcomePage({ onWelcomeComplete }) {
  const [isBlurred, setIsBlurred] = useState(false);

  useEffect(() => {
    const blurTimer = setTimeout(() => {
      setIsBlurred(true);
    }, 3000);

    const hideTimer = setTimeout(() => {
      setIsBlurred(false);
      onWelcomeComplete();
    }, 5000);

    return () => {
      clearTimeout(blurTimer);
      clearTimeout(hideTimer);
    };
  }, [onWelcomeComplete]);

  return (
    <div className={`WelcomePage ${isBlurred ? "blur" : ""}`}>
      <WelcomeImage src="/images/welcome-logo.png" alt="Welcome Image" />
    </div>
  );
}

export default WelcomePage;