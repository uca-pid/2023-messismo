import '../App.css';
import React, { useEffect, useState } from "react";
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const WelcomeImage = styled.img`
  max-width: 100%;
  max-height: 100%;
  transition: filter 0.5s ease-in-out;
  &.blurred {
    filter: blur(5px);
  }
`;

function WelcomePage() {

  const [isBlurred, setIsBlurred] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const blurTimer = setTimeout(() => {
      setIsBlurred(true);
    }, 2000);

    const redirectTimer = setTimeout(() => {
      navigate('/signinupform');
    }, 3500);

    return () => {
      clearTimeout(blurTimer);
      clearTimeout(redirectTimer);
    };
  }, [navigate]);

  return (
    <Container>
      <WelcomeImage
        src="/images/welcome-logo.png"
        alt="Welcome Image"
        className={isBlurred ? 'blurred' : ''}
      />
    </Container>
  );
}

export default WelcomePage;