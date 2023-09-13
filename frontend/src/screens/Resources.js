import React, { useEffect, useState } from "react";
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import Header from '../components/Header';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
`;

const users = [
    { id: 101, username: 'JillayneHazlett', email: 'JillayneHazlett@moes.com', password: 'password', joined: '14/05/2023', type: 'admin', pending: 'no' },
    { id: 102, username: 'DoriceGemini', email: 'DoriceGemini@moes.com', password: 'password', joined: '30/09/2021', type: 'user', pending: 'no' },
    { id: 103, username: 'StarlaBarrus', email: 'StarlaBarrus@moes.com', password: 'password', joined: '18/11/2018', type: 'user', pending: 'no' },
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'user', pending: 'no' },
    { id: 105, username: 'ModestiaHashim', email: 'ModestiaHashim@moes.com', password: 'password', joined: '05/03/2019', type: 'admin', pending: 'yes' },
    { id: 106, username: 'KaylaKelula', email: 'KaylaKelula@moes.com', password: 'password', joined: '25/08/2022', type: 'user', pending: 'yes' },
    { id: 107, username: 'AllisJena', email: 'AllisJena@moes.com', password: 'password', joined: '20/01/2019', type: 'admin', pending: 'yes' },
    { id: 108, username: 'ThaliaAde', email: 'ThaliaAde@moes.com', password: 'password', joined: '04/03/2020', type: 'user', pending: 'yes' },
    { id: 109, username: 'DevinaGeoras', email: 'DevinaGeoras@moes.com', password: 'password', joined: '01/06/2018', type: 'user', pending: 'yes' }
  ];

function Resources(){
    const pending = users.filter(user => user.pending === 'yes');
    const admins = users.filter(user => user.type === 'admin' && user.pending === 'no');
    const employees = users.filter(user => user.type === 'user' && user.pending === 'no');

    const handleAccept = (id) => {

        console.log(`Accepted: ${id}`);
    };
    
    const handleReject = (id) => {

        console.log(`Rejected: ${id}`);
    };

    const renderUser = (user) => (
        <div key={user.id}>
            <h3>{user.username}</h3>
            <p>{user.email}</p>
            <p>Hire Date {user.joined}</p>
            <p>{user.type}</p>
            {user.pending === 'yes' && (
                <div>
                    <button onClick={() => handleAccept(user.id)}>Accept</button>
                    <button onClick={() => handleReject(user.id)}>Reject</button>
                </div>
            )}
        </div>
    );

    return(
        <Container>
            <Header/>
            <div style={{color: 'white'}}>
                RESOURCES
            </div>

            <div>
                <div>
                    <h2>Pending</h2>
                    {pending.map(renderUser)}
                </div>
                <div>
                    <h2>Admins</h2>
                    {admins.map(renderUser)}
                </div>
                <div>
                    <h2>Employees</h2>
                    {employees.map(renderUser)}
                </div>
            </div>
        </Container>
    )
}

export default Resources;