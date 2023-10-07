import '../App.css';
import React, { useState, useEffect } from "react";
import styled from 'styled-components';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import Navbar from "../components/Navbar";
import OrderForm from '../components/OrderForm';
import Button from "@mui/material/Button";
import ordersService from "../services/orders.service";

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

const OrderList = styled.tr`

`;

const DetailsButton = styled.button`
    display: block;
    width: 100%;
    font-size: 1.5rem;
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

const Details = styled.div`
    background-color: black;
`;

const Order = styled.div`
    background-color: blue;
    margin-bottom: 1rem;
`;



function Orders() {

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);
    const [isOrderFormVisible, setOrderFormVisible] = useState(false);
    const [isDetailsVisible, setDetailsVisible] = useState(false);
    const [open, setOpen] = React.useState(false);
    const [orders, setOrders] = useState([]);
    const [selectedOrderDetails, setSelectedOrderDetails] = useState(null);
    const isAdminOrManager = currentUser && (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");
    
    useEffect(() => {
        ordersService
          .getAllOrders()
          .then((response) => {
            setOrders(response.data);
          })
          .catch((error) => {
            console.error("Error al mostrar las ordenes", error);
          });
      }, [isOrderFormVisible, open]);

    if (!currentUser) {
        return <Navigate to="/" />;
    }

    const contentVisible = !clicked;

    const handleAddOrderClick = () => {
        setOrderFormVisible(true);
        setOpen(true);
    };

    const handleCloseOrderForm = () => {
        setOrderFormVisible(false);
        setOpen(false);
    };

    const handleViewDetails = (orderId) => {
        const selectedOrder = orders.find(order => order.id === orderId);
        setSelectedOrderDetails(selectedOrder.productOrders);
        setDetailsVisible(true);
    };

    const handleCloseDetails = () => {
        setDetailsVisible(false);
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

                {!isOrderFormVisible && (
                    <div className='order-list'>
                        <h1>Orders</h1>
                        <OrderList>
                            {orders.map(order => (

                            <Order key={order.id}>
                                <td> {order.id}</td>
                                <td> {order.user.username}</td>
                                <td> {order.dateCreated}</td>
                                <td> {order.totalPrice}</td>
                                <td>
                                    <DetailsButton onClick={() => handleViewDetails(order.id)}>
                                        Details
                                    </DetailsButton>
                                </td>
                            </Order>

                            ))}
                        </OrderList>

                        {isDetailsVisible && (

                            <Details>
                                {selectedOrderDetails.map(productOrder => (
                                        <div key={productOrder.productOrderId}>
                                            <strong>Product</strong> {productOrder.product.name}<br />
                                            <strong>Price</strong> {productOrder.product.unitPrice}<br />
                                            <strong>Units</strong> {productOrder.quantity}<br />
                                        </div>
                                    ))}

                                <DetailsButton onClick={() => handleCloseDetails()}>
                                    Close
                                </DetailsButton>
                            </Details>

                        )}

                        
                    </div>
                )}


                
            </MainContent>

        </Container>

    );
}

export default Orders;