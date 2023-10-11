import React from 'react'
import { styled } from 'styled-components';
// import 'fontsource-roboto';
import { useSelector } from 'react-redux';
import Navbar from "../components/Navbar";
import { Navigate } from 'react-router-dom';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
`;

const MainContent = styled.div`
    display: ${props => (props.visible ? 'flex' : 'none')};
    flex-direction: column;
    justify-content: center;
    align-items: center;
    flex-grow: 1;
`;

const WelcomeImage = styled.img`
    @media(max-width: 600px){
      width: 70%;
    }
`;

const Resource = styled.div`

    font-family: 'Roboto',serif;
    color: #a4d4cc;
    text-transform: uppercase;
    letter-spacing: 1px;
    text-align: center;
    font-size: 20px;
    margin-top: 5rem;

    h3{
        font-size: 100%;
    }
    p{
        padding: 1rem;
    }
    
`;


function HomePage(){

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);

    if (!currentUser) {
        return <Navigate to="/" />;
    }

    const contentVisible = !clicked;

    return(
        <Container>

            <Navbar />

            <MainContent visible={contentVisible}>
                <WelcomeImage src="/images/welcomeback2.png"/>
                <Resource>
                    <h3>{currentUser.email}</h3>
                    <h3>{currentUser.role}</h3>
                </Resource>
            </MainContent>

        </Container>
    )
}

export default HomePage;