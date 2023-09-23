import React from "react";
import styled from 'styled-components';
import Navbar from "../components/Navbar";
import { useSelector } from 'react-redux';
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

function Products(){

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);

    const isAdminOrManager = currentUser && (currentUser.role === 'MANAGER' || currentUser.role === 'ADMIN');

    const contentVisible = !clicked;

    if (!currentUser) {
        return <Navigate to="/" />;
    }
    if (!isAdminOrManager) {
        return <Navigate to="/homepage" />;
    }

    return(
        <Container className='products'>
            
            <Navbar />
            <MainContent visible={contentVisible}>
                <div style={{color: 'white'}}>
                    PRODUCTS
                </div>
            </MainContent>

        </Container>
    )
}

export default Products;