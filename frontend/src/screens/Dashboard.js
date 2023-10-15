import React, { useState, useEffect } from "react";
import { styled } from 'styled-components';
// import 'fontsource-roboto';
import { useSelector } from 'react-redux';
import Navbar from "../components/Navbar";
import { Navigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import ProgressBar from "../components/ProgressBar";
import productsService from "../services/products.service";
import { makeStyles } from '@mui/styles';

const CustomizedDateTimePicker = styled(DatePicker)`
   
    .MuiInputBase-input {
        color: #a4d4cc; 
    }
    .MuiInputLabel-root {
        color: #a4d4cc; 
    }
    .MuiOutlinedInput-notchedOutline {
        border-color: #a4d4cc;
    }
    .MuiSvgIcon-root {
        fill: #a4d4cc;
    }
    .MuiOutlinedInput-root {
        &:hover fieldset {
            border-color: #a4d4cc;
        }
        &.Mui-focused fieldset {
            border-color: #a4d4cc;
        }
    }
    :focus-within .MuiInputLabel-root {
        color: #a4d4cc;
    }

`;

const Container = styled.div`
    display: flex;
    flex-direction: column;
    height: 100vh;
`;

const MainContent = styled.div`
    display: ${props => (props.visible ? 'flex' : 'none')};
    justify-content: center;
    align-items: center;
    flex-grow: 1;
`;

const Graphs = styled.div`
    #wrap { overflow:auto; }
    #div2{ background:red; width: 45vw; height: 35vh; float:left; }
    #div3{ background:green; width: 45vw; height: 35vh; float:left;  }
    #div5{ background:blue; width: 32vw; height: 35vh; float:left; }
    #div6{ background:purple; width: 32vw; height: 35vh; float:left; }
`;

const Buttons = styled.div`
    margin-right: 2rem;
    margin-left: 2rem;
`;

const DateButton = styled.button`
    font-size: 1rem;
    border-radius: 3px;
    border: 1px solid black;
    background-color: #a4d4cc;
    color: black;
    text-transform: uppercase;
    cursor: pointer;
    box-shadow: 0 3px #999;
    font-family: 'Roboto',serif;
    text-align: center;
    padding: 0.6rem;
    margin: 0.3rem;

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

const DatePick = styled.div`
    .datetext{
        color: white
    }
`;

const DateFilter = styled.div`
    //background:white; 
    width: 45vw; 
    height: 12vh; 
    float:left;

    display: flex;
    align-items: center;
`;

const TotalStats = styled.div`
    //background:yellow; 
    width: 55vw; 
    height: 12vh; 
    float:right;

    display: flex;
    justify-content: center;
    align-items: center;
`;

const Stat = styled.div`
    width: 25%;
    font-size: 1.5rem;
    border: 1px solid black;
    color: black;
    letter-spacing: 1px;
    font-family: 'Roboto',serif;
    padding: 1rem;
    margin: 1.5rem;
    color: white;
    box-shadow: -3px 0 0 rgb(157,187,191,0.7);

    .statnumber{
        font-size: 2.2rem;
        margin-left: 1rem;
    }

    .stattext{
        font-size: 1.2rem;
        margin-left: 1rem;
    }

    &.total-revenue{
        background-color: rgb(157,187,191,0.4);
    }

    &.total-orders{
        background-color: rgb(157,187,191,0.3);
    }

    &.total-products{
        background-color: rgb(157,187,191,0.4);
    }

    &.total-categories{
        background-color: rgb(157,187,191,0.3);
    }
`;

const OutOfStock = styled.div`
    //background:orange; 
    width: 23vw; 
    height: 70vh; 
    float:right;

    display: flex;
    justify-content: center;
    overflow-y: auto;
`;

const StockList = styled.div`
    width: 80%;
    margin: 3rem;

`;

function abbreviateNumber(number) {
    const million = 1000000;
    const thousand = 1000;

    if (number >= million) {
        return `${(number / million).toFixed(1)}m`;
    } else if (number >= thousand) {
        return `${(number / thousand).toFixed(1)}k`;
    } else {
        return `${number}`;
    }
}

const useStyles = makeStyles({
    slider: {
      color: '#a4d4cc', 
    },
});


function Dashboard(){

    const { user: currentUser } = useSelector((state) => state.auth);
    const clicked = useSelector((state) => state.navigation.clicked);
    const classes = useStyles();

    const [products, setProducts] = useState([]);
    const [sliderValue, setSliderValue] = useState(15);
    const [datePickerVisible, setDatePickerVisible] = useState(false);
    const [datePickerType, setDatePickerType] = useState('');
    const [isDateButtonClicked, setIsDateButtonClicked] = useState(false);

    useEffect(() => {
        productsService
          .getAllProducts()
          .then((response) => {
            const filteredProducts = response.data.filter(product => product.stock <= sliderValue);
            setProducts(filteredProducts);
          })
          .catch((error) => {
            console.error("Error al mostrar los productos", error);
          });
      },);
      

    if (!currentUser) {
        return <Navigate to="/" />;
    }

    const contentVisible = !clicked;

    const handleDateButtonClick = (type) => {
        setDatePickerType(type);
        setDatePickerVisible(type === 'daily' || type === 'weekly' || type === 'monthly');
        setIsDateButtonClicked(true);
    };

    const handleOtherButtonClick = () => {
        setIsDateButtonClicked(false);
        setDatePickerVisible(false);
    };

    const handleSliderChange = (event, newValue) => {
      setSliderValue(newValue);
    };


    const totalRevenue = 12345678
    const abbreviatedRevenue = abbreviateNumber(totalRevenue);
    const totalOrders = 1000
    const totalProducts = 20
    const totalCategories = 5

    return(
        <Container>

            <Navbar />

            <MainContent visible={contentVisible}>

                <Graphs id="wrap">

                    <DateFilter>
                        <Buttons>
                            <DateButton onClick={() => handleDateButtonClick('daily')}>Daily</DateButton>
                            <DateButton onClick={() => handleDateButtonClick('weekly')}>Weekly</DateButton>
                            <DateButton onClick={() => handleDateButtonClick('monthly')}>Monthly</DateButton>
                            <DateButton onClick={handleOtherButtonClick}>Yearly</DateButton>
                            <br/>
                            <DateButton onClick={handleOtherButtonClick}>Last Week</DateButton>
                            <DateButton onClick={handleOtherButtonClick}>Last Month</DateButton>
                            <DateButton onClick={handleOtherButtonClick}>Last Year</DateButton>
                        </Buttons>

                        {datePickerVisible && isDateButtonClicked && (
                            <DatePick style={{ backgroundColor: 'transparent'}}>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    { datePickerType === 'weekly' && 
                                        <CustomizedDateTimePicker label={'YYYY-MM-DD'} views={['year', 'month', 'day']} format="YYYY-MM-DD" /> }

                                    { datePickerType === 'daily' && 
                                        <CustomizedDateTimePicker label={'YYYY-MM'} views={['month', 'year']} format="YYYY-MM" /> }

                                    { datePickerType === 'monthly' && 
                                        <CustomizedDateTimePicker label={'YYYY'} views={['year']} /> }
                                </LocalizationProvider>
                            </DatePick>
                        )}
                    </DateFilter>

                    <TotalStats>
                        <Stat className="total-revenue">
                            <p className="statnumber">${abbreviatedRevenue}</p>
                            <p className="stattext">Revenue</p>
                        </Stat>
                        <Stat className="total-orders">
                            <p className="statnumber">{totalOrders}</p> 
                            <p className="stattext">Orders</p>
                        </Stat>
                        <Stat className="total-products">
                            <p className="statnumber">{totalProducts}</p>
                            <p className="stattext">Products</p>
                        </Stat>
                        <Stat className="total-categories">
                            <p className="statnumber">{totalCategories}</p>
                            <p className="stattext">Categories</p>
                        </Stat>
                    </TotalStats>


                    <OutOfStock id="div4">
                        <StockList>
                            <h2 style={{color:'white'}}>Out of Stock soon</h2>
                            <Box>
                                <Slider
                                    defaultValue={5}
                                    aria-label="Default"
                                    valueLabelDisplay="auto"
                                    min={0}
                                    max={20}
                                    value={sliderValue}
                                    onChange={handleSliderChange}
                                    classes={{
                                        root: classes.slider,
                                        thumb: classes.slider,
                                        active: classes.slider,
                                        track: classes.slider,
                                        rail: classes.slider,
                                      }}
                                />
                                <p style={{color:'white', marginBottom:'2rem'}}>Products with {sliderValue} or less units</p>

                                {products.map((producto, index) => (
                                    <ProgressBar key={index} bgcolor={producto.stock !== 0 ? '#8CB9B1' : 'red'} name={producto.name} progress={producto.stock} sliderValue={sliderValue} />
                                ))}
                            </Box>
                        </StockList>
                    </OutOfStock>

                    <div id="div2">

                    </div>

                    <div id="div6">
                        
                    </div> 

                    <div id="div3">
                        
                    </div>  

                    <div id="div5">
                        
                    </div>  

                </Graphs>

            </MainContent>

        </Container>
    )
}

export default Dashboard;