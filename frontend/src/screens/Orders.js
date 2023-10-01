import '../App.css';
import React, { useState } from "react";
import styled from 'styled-components';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import Navbar from "../components/Navbar";
import OrderForm from '../components/OrderForm';
import Button from "@mui/material/Button";

const Container = styled.div`
`;

const MainContent = styled.div`
    display: ${(props) => (props.visible ? "" : "none")};
    padding: 3rem;
    font-family: 'Roboto';
    color: #f0f0f0;
    display: flex;
    align-items: center;
    min-height: 90vh;
    flex-direction: column;
`;

const Title = styled.h1`
    margin-top: 20px;
    color: white;
    font-family: "Roboto";
    font-size: 30px;
    margin-left: 10px;
`;

const Modal = styled.div`
    display: ${(props) => (props.open ? "flex" : "none")};
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
    z-index: 9999;
`;

const ModalContent = styled.div`
    padding: 20px;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    max-height: 60vh;
    overflow-y: auto;
`;

function Orders() {

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);
    const [isOrderFormVisible, setOrderFormVisible] = useState(false);
    const isAdminOrManager = currentUser && (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");
    
    if (!currentUser) {
        return <Navigate to="/" />;
    }

    const contentVisible = !clicked;

    const handleAddOrderClick = () => {
        setOrderFormVisible(true);
    };

    const handleCloseOrderForm = () => {
        setOrderFormVisible(false);
    };

    return (

        <Container>

            <Navbar />
        
            <MainContent visible={contentVisible}>

                <div className="add-product">
                    {currentUser.role === "ADMIN" || currentUser.role === "MANAGER" || currentUser.role === "VALIDATEDEMPLOYEE"? (
                        <Button
                            variant="contained"
                            style={{
                                color: "white",
                                borderColor: "#007bff",
                                fontSize: "1.3rem",
                                height: "40px",
                            }}
                            onClick={handleAddOrderClick}>Add Order</Button>
                    ) : null}
                </div>

                <Modal open={isOrderFormVisible}>
                    <ModalContent>
                        {isOrderFormVisible && <OrderForm onCancel={handleCloseOrderForm} />}
                    </ModalContent>
                    
                </Modal>
                
            </MainContent>

        </Container>

    );
}

export default Orders;