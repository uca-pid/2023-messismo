import React from 'react';
import styled from 'styled-components';
// import 'fontsource-roboto';
import { Link } from 'react-router-dom';

const PopUp = styled.div`
    width: 25%;
    height: 25%;
    right: 37.4%;
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #011627;
    border-radius: 23px;
    box-shadow: -2px 5px 5px black;
    background-color: black;
    flex-direction: column;
    color: #011627;
    max-width: 80%;
    max-height: 80vh;
    overflow-y: auto;

    @media (max-width: 730px) {
        width: 50%;
        right: 25%;
    }

    @media (max-width: 353px) {
        width: 100%;
        right: 10%;
    }
`;

const Content = styled.div`
    width: 80%;
    position: absolute;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    font-size: 1rem;
    letter-spacing: 0.1rem;
    color: white;
    font-family: 'Roboto',serif;

    @media (max-width: 1000px) {
        max-width: 90%;
        max-height: 90vh;
        font-size: 0.8rem;
    }
`;

const AcceptLink = styled(Link)`
    border-radius: 3px;
    padding: 1rem 3.5rem;
    margin-top: 2rem;
    border: 1px solid black;
    background-color: #a4d4cc;
    color: black;
    text-transform: uppercase;
    cursor: pointer;
    letter-spacing: 1px;
    box-shadow: 0 3px #999;
    font-family: 'Roboto',serif;
    text-align: center;
    text-decoration: none;

    &:hover{
        background-color: #a7d0cd;
    }
    &:active{
        background-color: #a4d4cc;
        box-shadow: 0 3px #666;
        transform: translateY(4px);
    }
    &:focus{
        outline: none;
    }
`;

const SignUpPopUp = ({isRegistered, setSignUpPopUp}) => {

    return (
        <PopUp>

            <Content>
                <h1>{isRegistered ? 'An account is already registered' : 'Pending confirmation'}</h1>
                <h4>{isRegistered ? 'with your email address' : 'An admin needs to approve this request'}</h4>
                <AcceptLink onClick={ () => setSignUpPopUp(false) }> Accept </AcceptLink>
            </Content>

        </PopUp>
    );
}

export default SignUpPopUp;