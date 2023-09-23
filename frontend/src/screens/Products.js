import React from "react";
import styled from 'styled-components';
import Navbar from "../components/Navbar";
import { useSelector } from 'react-redux';
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
    justify-content: center;
    align-items: center;
    flex-grow: 1;
    font-size:1.5rem;
`;

function Products(){

    const clicked = useSelector((state) => state.navigation.clicked);
    const contentVisible = !clicked;

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