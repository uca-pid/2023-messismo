import React, { useState } from 'react'
import { styled } from 'styled-components';
import 'fontsource-roboto';
import { Link } from 'react-router-dom';
import Header from '../components/Header';

const SIDEBAR_WIDTH = '16%';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
`;

const SidebarContainer = styled.div`
    width: ${SIDEBAR_WIDTH};
`;

const MainContent = styled.div`

`;

const WelcomeImage = styled.img`
    
`;

const Resource = styled.div`

`;


const signedinuser = [
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'user', pending: 'no' }
  ];

function HomePage(){

    const renderUser = (user) => (
        <div key={user.id}>
          <h3>{user.username}</h3>
          <p>{user.email}</p>
          <p>{user.joined}</p>
          <p>{user.type}</p>
        </div>
    );

    return(
        <Container>

            <SidebarContainer>
                <Header />
            </SidebarContainer>

            <MainContent>
        
                <WelcomeImage src="/images/welcomeback3.png"/>

                <Resource>
                    <p>{signedinuser.map(renderUser)}</p>
                </Resource>

            </MainContent>

        </Container>
    )
}

export default HomePage;