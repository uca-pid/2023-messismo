import React, { useState } from 'react'
import { styled } from 'styled-components';
import 'fontsource-roboto';
import { Link } from 'react-router-dom';
import Header from '../components/Header';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
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
            <Header/>
            <div style={{color: 'white'}}>
                HOMEPAGE
            </div>

            <div>
                <div>
                    <h2>Welcome Back</h2>
                    <p>{signedinuser.map(renderUser)}</p>
                </div>
            </div>
        </Container>
    )
}

export default HomePage;