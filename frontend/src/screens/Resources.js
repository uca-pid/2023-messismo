import React from "react";
import styled from 'styled-components';
import { useSelector, useDispatch } from 'react-redux';
import { acceptUser, rejectUser, deleteUser } from '../redux/userSlice';
import Navbar from "../components/Navbar";

const Container = styled.div`
    display: flex;
    flex-direction: column;
`;

const MainContent = styled.div`
    display: ${props => (props.visible ? '' : 'none')};
    padding: 3rem;
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
    flex-wrap: wrap;

    @media (max-width: 800px){
        display: block;
    }
`;

const Resource = styled.div`

    display: flex;
    flex-wrap: wrap;
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

function Resources(){

    const users = useSelector(state => state.users)
    const dispatch = useDispatch()

    const pending = users.filter(user => user.pending === 'yes');
    const admins = users.filter(user => user.type === 'admin' && user.pending === 'no');
    const employees = users.filter(user => user.type === 'employee' && user.pending === 'no');

    const clicked = useSelector((state) => state.navigation.clicked);
    const contentVisible = !clicked;
    
    const handleAccept = (id) => {
        dispatch(acceptUser(id))
    };
    
    const handleReject = (id) => {
        dispatch(rejectUser(id))
    };

    const handleDelete = (id) => {
        dispatch(deleteUser(id))
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

            <Navbar />
            
            <MainContent visible={contentVisible}>

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