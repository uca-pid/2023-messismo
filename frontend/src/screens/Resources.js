import React, { useEffect, useState } from "react";
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import Header from '../components/Header';

const SIDEBAR_WIDTH = '16%';

const Container = styled.div`
    display: flex;
    

`;

const MainContent = styled.div`
    flex: 1;
    padding: 100px;

    @media (max-width: 1600px){
        inset: 0 95% 0 0;
    }
    min-width: 600px;
`;

const SidebarContainer = styled.div`
    width: ${SIDEBAR_WIDTH};
`;

const Title = styled.h1`
    margin-top: 20px;
    color: white;
    font-family: 'Roboto';
    font-size: 30px;
    margin-left: 10px;
`;

const UserContainer = styled.div`

    display: flex;
    margin-top: 20px;

    @media (max-width: 1600px){
        display: block;
    }
`;

const Resource = styled.div`

    font-family: 'Roboto';

    .user-data{
        border-radius: 3px;
        padding: 1rem 3.5rem;
        border: 1px solid black;
        background-color: rgb(164,212,204,0.7);
        color: black;
        text-transform: uppercase;
        letter-spacing: 1px;
        text-align: center;
        font-size: 14px;
        margin: 10px;
    }

    h3{
        font-size: 20px;
        padding: 1rem;
    }
    
`;


const Button = styled.button`
    border-radius: 3px;
    padding: 1rem;
    margin-top: 1rem;
    border: 1px solid black;
    color: black;
    text-transform: uppercase;
    cursor: pointer;
    letter-spacing: 1px;
    box-shadow: 0 3px #999;
    font-family: 'Roboto';
    text-align: center;
    font-size: 14px;

    &.button-accept{

        background-color: #a4d4cc;
        margin-right: 1rem;

        &:hover{
            background-color: green;
        }

    }

    &.button-reject{

        background-color: #a4d4cc;

        &:hover{
            background-color: red;
        }
    }

    &.button-delete{

        background-color: #a4d4cc;

        &:hover{
            background-color: red;
        }
    }

    &:active{
        background-color: #a4d4cc;
        box-shadow: 0 3px #666;
        transform: translateY(4px);
    }

`;

const UserItem = styled.div`
    width: 100%;
`;

const Subheader = styled.h2`
    color: white;
    font-size: 24px;
    margin-left: 10px;
`;


const users = [
    { id: 101, username: 'JillayneHazlett', email: 'JillayneHazlett@moes.com', password: 'password', joined: '14/05/2023', type: 'admin', pending: 'no' },
    { id: 102, username: 'DoriceGemini', email: 'DoriceGemini@moes.com', password: 'password', joined: '30/09/2021', type: 'employee', pending: 'no' },
    { id: 103, username: 'StarlaBarrus', email: 'StarlaBarrus@moes.com', password: 'password', joined: '18/11/2018', type: 'employee', pending: 'no' },
    { id: 104, username: 'SindeeBlake', email: 'SindeeBlake@moes.com', password: 'password', joined: '11/04/2021', type: 'employee', pending: 'no' },
    { id: 105, username: 'ModestiaHashim', email: 'ModestiaHashim@moes.com', password: 'password', joined: '05/03/2019', type: 'admin', pending: 'yes' },
    { id: 106, username: 'KaylaKelula', email: 'KaylaKelula@moes.com', password: 'password', joined: '25/08/2022', type: 'employee', pending: 'yes' },
    { id: 107, username: 'AllisJena', email: 'AllisJena@moes.com', password: 'password', joined: '20/01/2019', type: 'admin', pending: 'yes' },
    { id: 108, username: 'ThaliaAde', email: 'ThaliaAde@moes.com', password: 'password', joined: '04/03/2020', type: 'employee', pending: 'yes' },
    { id: 109, username: 'DevinaGeoras', email: 'DevinaGeoras@moes.com', password: 'password', joined: '01/06/2018', type: 'employee', pending: 'yes' },
  ];

function Resources(){
    const pending = users.filter(user => user.pending === 'yes');
    const admins = users.filter(user => user.type === 'admin' && user.pending === 'no');
    const employees = users.filter(user => user.type === 'employee' && user.pending === 'no');
    
    const handleAccept = (id) => {

        console.log(`Accepted: ${id}`);
    };
    
    const handleReject = (id) => {

        console.log(`Rejected: ${id}`);
    };

    const handleDelete = (id) => {

        console.log(`Deleted: ${id}`);
    };

    const renderUser = (user) => (
        
        <div key={user.id} className="user-data">
            <h3 className="user-username">{user.username}</h3>
            <p className="user-email">{user.email}</p>
            <p className="user-hiredate">Hire Date {user.joined}</p>
            <p className="user-type">{user.type}</p>
            {user.pending === 'yes' && (
                <div>
                    <Button className="button-accept" onClick={() => handleAccept(user.id)}>Accept</Button>
                    <Button className="button-reject" onClick={() => handleReject(user.id)}>Reject</Button>
                </div>
            )}
            {user.pending === 'no' && (
                <div>
                    <Button className="button-delete" onClick={() => handleDelete(user.id)}>Delete</Button>
                </div>
            )}
        </div>
    );

    return(
        <Container>

            <SidebarContainer>
                <Header />
            </SidebarContainer>
            
            <MainContent>

                <Title>
                    RESOURCES
                </Title>

                <UserContainer>

                    <UserItem>
                        <Subheader>Pending</Subheader>
                        <Resource>
                            {pending.map(renderUser)}
                        </Resource>
                    </UserItem>

                    <UserItem>
                        <Subheader>Admins</Subheader>
                        <Resource>
                            {admins.map(renderUser)}
                        </Resource>
                    </UserItem>

                    <UserItem>
                        <Subheader>Employees</Subheader>
                        <Resource>
                            {employees.map(renderUser)}
                        </Resource>
                    </UserItem>

                </UserContainer>

            </MainContent>

        </Container>
    )
}

export default Resources;