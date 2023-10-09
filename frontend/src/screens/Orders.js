import '../App.css';
import React, { useState, useEffect, useMemo } from "react";
import styled from 'styled-components';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import Navbar from "../components/Navbar";
import OrderForm from '../components/OrderForm';
import ordersService from "../services/orders.service";
import { useTheme } from "@mui/material/styles";
import { Box, Typography, gridClasses, useMediaQuery } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { MdFastfood } from 'react-icons/md';
import moment from 'moment'


const Container = styled.div`

`;

const Button = styled.button`
    display: block;
    font-size: 1.5rem;
    border-radius: 3px;
    padding: 1rem 3.5rem;
    margin-top: 3rem;
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

    @media (max-width: 477px) {
        margin-top: 1rem;
        font-size: 1rem;
        padding: 1rem 2.5rem;
    }

`;

const MainContent = styled.div`
    display: ${(props) => (props.visible ? "" : "none")};
    width: 80%;
    margin: auto;
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

const OrdersTable = styled.div`

`;

const Details = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3);
    z-index: 9999;
    padding: 1rem;
`;

const DetailsContent = styled.div`
    padding: 2rem;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    max-height: 60vh;
    overflow-y: auto;
    width: 20%;
    margin: auto;
    background-color: black;
    strong{
        color: white;
        font-family: 'Roboto';
        font-size: 2rem;
    }

    @media (max-width: 1500px) {
        width: 30%;
    }
    @media (max-width: 1000px) {
        width: 40%;
    }
    @media (max-width: 800px) {
        width: 50%;
    }
`;

const DetailsButton = styled.button`
    display: block;
    width: 100%;
    font-size: 1.5rem;
    border-radius: 3px;
    padding: 1rem 3.5rem;
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

const MoreDetails = styled(MdFastfood)`
    cursor: pointer;
    @media (max-width: 1000px) {
        font-size: 1.5rem,
    }
    @media (max-width: 760px) {
        font-size: 1rem,
    }
    @media (max-width: 500px) {
        font-size: 0.8rem,
    }
`;

const MOBILE_COLUMNS = {
    id: true,
    username: false,
    dateCreated: true,
    totalPrice: true,
    details: false,
};
const ALL_COLUMNS = {
    id: true,
    username: true,
    dateCreated: true,
    totalPrice: true,
    details: true,
};


function Orders() {

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);
    const [isOrderFormVisible, setOrderFormVisible] = useState(false);
    const [isDetailsVisible, setDetailsVisible] = useState(false);
    const [open, setOpen] = React.useState(false);
    const [orders, setOrders] = useState([]);
    const [selectedOrderDetails, setSelectedOrderDetails] = useState(null);
    const [pageSize, setPageSize] = useState(5);
    const isAdminOrManager = currentUser && (currentUser.role === "MANAGER" || currentUser.role === "ADMIN");
    
    const theme = useTheme();
    const matches = useMediaQuery(theme.breakpoints.up("sm"));
  
    const [columnVisible, setColumnVisible] = useState(ALL_COLUMNS);
    useEffect(() => {
      const newColumns = matches ? ALL_COLUMNS : MOBILE_COLUMNS;
      setColumnVisible(newColumns);
    }, [matches]);

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

    const rows = orders.map((order) => ({
        id: order.id,
        username: order.user.username,
        dateCreated: order.dateCreated,
        totalPrice: order.totalPrice.toLocaleString('en-US', { style: 'currency', currency: 'USD' }),
    }));

    const columns = [
        { field: 'id', headerName: 'ID', flex:1, align:'center', headerAlign: 'center', sortable:false },
        { field: 'username', headerName: 'Vendor', flex:1, align:'center', headerAlign: 'center', sortable:true },
        { field: 'dateCreated', headerName: 'Date', flex:1, align:'center', headerAlign: 'center', sortable:true,
        renderCell: params => moment(params.row.dateCreated).format('YYYY-MM-DD HH:MM:SS') },
        { field: 'totalPrice', headerName: 'Total', flex:1, align:'center', headerAlign: 'center', sortable:true },
        {
          field: 'details',
          headerName: 'Products',
          flex:1,
          align:'center',
          headerAlign: 'center', 
          sortable:false,
          renderCell: (params) => (
            <MoreDetails onClick={() => handleViewDetails(params.row.id)} />
          ),
        },
    ];

    return (

        <Container>

            <Navbar />

            <MainContent visible={contentVisible}>

                {currentUser.role === "ADMIN" || currentUser.role === "MANAGER" || currentUser.role === "VALIDATEDEMPLOYEE"? (
                    <Button variant="contained" onClick={handleAddOrderClick}>Add Order</Button>
                ) : null}
            
                <div visible={contentVisible}>

                    <Modal open={isOrderFormVisible}>
                        <ModalContent>
                            {isOrderFormVisible && <OrderForm onCancel={handleCloseOrderForm} />}
                        </ModalContent>
                    </Modal>

                    {!isOrderFormVisible && (

                        <div>

                            <OrdersTable>
                                <Box sx={{height:400, width:'100%'}}>
                                    <Typography variant='h3' component='h3' sx={{textAlign:'center', mt:3, mb:3, color:'white'}}>
                                        Orders
                                    </Typography>
                                    <DataGrid 
                                    initialState={{
                                        pagination: { paginationModel: { pageSize: 5 } },
                                        sorting: {
                                        sortModel: [{ field: 'dateCreated', sort: 'desc' }],
                                        },
                                    }}
                                    autoHeight={true}
                                    columns={columns}
                                    columnVisibilityModel={columnVisible}
                                    rows={rows}
                                    getRowId={(row) => row.id}
                                    pageSizeOptions={[5, 10, 25]}
                                    //rowsPerPageOptions={[5, 10, 25]}
                                    pagination
                                    pageSize={pageSize}
                                    onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
                                    getRowSpacing={params => ({
                                        top:params.isFirstVisible ? 0 : 5,
                                        bottom: params.isLastVisible ? 0 : 5
                                    })}
                                    sx={{
                                        border: 2,
                                        borderColor: '#a4d4cc',
                                        "& .MuiButtonBase-root": {
                                            color:'white',
                                        },
                                        '& .MuiDataGrid-cell:hover': {
                                        color: '#a4d4cc',
                                        },
                                        '.MuiDataGrid-columnSeparator': {
                                            display: 'none',
                                        },
                                        color: 'white',
                                        fontFamily:'Roboto',
                                        fontSize:'2rem',  
                                        ".MuiTablePagination-displayedRows": {
                                            color: "white",
                                            fontSize:'1.5rem',
                                        },
                                        ".MuiTablePagination-selectLabel": {
                                            color: "white",
                                            fontSize:'1.5rem',
                                        },
                                        '& .MuiSelect-select.MuiSelect-select': {
                                            color: 'white',
                                            fontSize:'1.5rem',
                                            marginTop: '1rem'
                                        },
                                        '.MuiDataGrid-sortIcon': {
                                            opacity: 'inherit !important',
                                            color:'white'
                                        },
                                        '& .MuiDataGrid-cell:focus': {
                                            outline: 'none',
                                        },
                                        '@media (max-width: 1000px)': {
                                            fontSize: '1.5rem',
                                        },
                                        '@media (max-width: 760px)': {
                                            fontSize: '1rem',
                                        },
                                        '@media (max-width: 600px)': {
                                            fontSize: '1.5rem',
                                        },
                                        '@media (max-width: 535px)': {
                                            fontSize: '1.2rem',
                                        },
                                        '@media (max-width: 435px)': {
                                            fontSize: '1rem',
                                        },
                                        '@media (max-width: 335px)': {
                                            fontSize: '0.8rem',
                                        },
                                    }}
                                    />
                                </Box>
                            </OrdersTable>

                            {isDetailsVisible && (

                                <Details>
                                    <DetailsContent>
                                        {selectedOrderDetails.map(productOrder => (
                                                <div key={productOrder.productOrderId}>
                                                    <strong>{productOrder.quantity}x {productOrder.product.name}</strong><br />
                                                    <strong>${productOrder.product.unitPrice} ea.</strong><br />
                                                    <strong></strong><br />
                                                </div>
                                            ))}

                                        <DetailsButton onClick={() => handleCloseDetails()}>
                                            Close
                                        </DetailsButton>
                                    </DetailsContent>
                                </Details>

                            )}

                        </div>
                    )}


                    
                </div>
            </MainContent>

        </Container>

    );
}

export default Orders;