import React from 'react'
import { styled } from 'styled-components';
import 'fontsource-roboto';
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
    align-items: center;
`;

const WelcomeImage = styled.img`

`;

const Resource = styled.div`

    font-family: 'Roboto';
    color: #a4d4cc;
    text-transform: uppercase;
    letter-spacing: 1px;
    text-align: center;
    font-size: 20px;
    margin-top: 5rem;

    h3{
        font-size: 40px;

    }
    p{
        padding: 1rem;
    }
    
`;


const signedinuser = [
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'user', pending: 'no' }
  ];

function HomePage(){

    const renderUser = (user) => (
        <div key={user.id}>
          <h3>{user.username}</h3>
          <p>{user.email}</p>
        </div>
    );

    return(
        <Container>

            <SidebarContainer>
                <Header />
            </SidebarContainer>

            <MainContent>
        
                <WelcomeImage src="/images/welcomeback2.png"/>

                <Resource>
                    <p>{signedinuser.map(renderUser)}</p>
                </Resource>

            </MainContent>

        </Container>
    )
}

export default HomePage;