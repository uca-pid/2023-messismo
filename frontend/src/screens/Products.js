import React from "react";
import styled from 'styled-components';
import Navbar from "../components/Navbar";
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import ProductsList from "../components/ProductsList";

const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
    font-size:1.5rem;
`;

const MainContent = styled.div`
    display: ${props => (props.visible ? 'flex' : 'none')};
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    flex-grow: 1;
    font-size:1.5rem;
`;

function Products(){

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);

    const isAdminOrManager = currentUser && (currentUser.role === 'MANAGER' || currentUser.role === 'ADMIN' ||  currentUser.role === 'VALIDATEDEMPLOYEE' ||  currentUser.role === 'EMPLOYEE') ;
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
                <ProductsList/>
            </MainContent>
        </Container>
    )
}

export default Products;