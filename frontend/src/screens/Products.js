import React from "react";
import styled from 'styled-components';
import Header from '../components/Header';

const Container = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
`;

function Products(){

    return(
        <Container className='products'>
            <Header/>
            <div style={{color: 'white'}}>
                PRODUCTS
            </div>
        </Container>
    )
}

export default Products;