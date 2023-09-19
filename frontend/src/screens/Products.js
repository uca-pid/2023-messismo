import React from "react";
import styled from 'styled-components';
import Navbar from "../components/Navbar";

const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
`;

const MainContent = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    flex-grow: 1;
`;

function Products(){

    return(
        <Container className='products'>
            
            <Navbar />
            <MainContent>
                <div style={{color: 'white'}}>
                    PRODUCTS
                </div>
            </MainContent>

        </Container>
    )
}

export default Products;