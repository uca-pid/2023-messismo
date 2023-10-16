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
import categoryService from "../services/category.service";
import { makeStyles } from '@mui/styles';
import dashboardService from "../services/dashboard.service";
import moment from 'moment';
import dayjs from 'dayjs';
import BarChart from "../components/BarChart";

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
    #div5{ background:blue; width: 30%; height: 35vh; float:left; }
    #div6{ background:purple; width: 30%; height: 35vh; float:left; }
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
    width: 45%; 
    height: 12vh; 
    float:left;

    display: flex;
    align-items: center;
`;

const TotalStats = styled.div`
    //background:yellow; 
    width: 55%; 
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
    width: 25%; 
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

const RevenueBarChartDiv = styled.div`
    //background:red; 
    width: 45%; 
    height: 35vh; 
    float:left;
`;

const SalesBarChartDiv = styled.div`
    //background:white; 
    width: 45%; 
    height: 35vh; 
    float:left;
`;

const BarChartDiv = styled.div`
    max-height: 95%;
    margin-left: 8rem;
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
    const [totalproducts, setTotalProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedDate, setSelectedDate] = useState("");
    const [sliderValue, setSliderValue] = useState(15);
    const [datePickerVisible, setDatePickerVisible] = useState(false);
    const [datePickerType, setDatePickerType] = useState('');
    const [isDateButtonClicked, setIsDateButtonClicked] = useState(false);
    const [dashboardData, setDashboardData] = useState({
        data: {
            quantityCategoryDonut: {},
            quantityProductDonut: {},
            earningProductDonut: {},
            averageByOrder: {},
            orderByQuantity: {},
            earningCategoryDonut: {},
            orderByEarnings: {}
        }
    });
    

    useEffect(() => {
        dashboardService.getDashboard({ dateRequested: "" }).then((response) => {
            console.log(response);
            setDashboardData(response);
        }).catch((error) => {
            console.error(error);
        });
    },[]);

    useEffect(() => {
        productsService
          .getAllProducts()
          .then((response) => {
            const filteredProducts = response.data.filter(product => product.stock <= sliderValue);
            setProducts(filteredProducts);
            setTotalProducts(response.data);
          })
          .catch((error) => {
            console.error("Error al mostrar los productos", error);
          });
    },);

    useEffect(() => {
        categoryService
          .getAllCategories()
          .then((response) => {
            setCategories(response.data);
          })
          .catch((error) => {
            console.error("Error al obtener categorías:", error);
          });
      }, []);


    if (!currentUser) {
        return <Navigate to="/" />;
    }

    const contentVisible = !clicked;

    const handleDateButtonClick = (type) => {
        setDatePickerType(type);
        setDatePickerVisible(type === 'daily' || type === 'weekly' || type === 'monthly');
        setIsDateButtonClicked(true);
    };

    const handleOtherButtonClick = (type) => {
        setDatePickerType(type);
        setIsDateButtonClicked(false);
        setDatePickerVisible(false);


        if (type === 'yearly') {
            const yearly = "";
            setSelectedDate(yearly);
        }
        else if (type === 'lastweek') {
            const today = new Date();
            const lastWeekDate = new Date(today.setDate(today.getDate() - 7));
            setSelectedDate(lastWeekDate);
        }
        else if (type === 'lastmonth') {
            const today = new Date();
            const lastMonthFirstDay = new Date(today.getFullYear(), today.getMonth() - 1, 1);
            setSelectedDate(lastMonthFirstDay);
        }
        else if (type === 'lastyear') {
            const lastYearDate = new Date();
            lastYearDate.setFullYear(lastYearDate.getFullYear() - 1);
            lastYearDate.setMonth(0);
            lastYearDate.setDate(1);
            setSelectedDate(lastYearDate);
        }

    };

    const handleSliderChange = (event, newValue) => {
      setSliderValue(newValue);
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
      };

    const handleButtonClick = () => {
        const dateObject = new Date(selectedDate);
        let date = "";
    
        switch (datePickerType) {
            case 'daily':
            case'lastmonth':
                date = (`${dateObject.getFullYear()}-${String(dateObject.getMonth() + 1).padStart(2, '0')}`);
                break;
            case 'weekly':
            case'lastweek':
                date = (dateObject.toISOString().slice(0, 10));
                break;
            case 'monthly':
            case'lastyear':
                date = (dateObject.getFullYear()).toString();
                break;
            case 'yearly':
                date = (selectedDate);
                break;
            default:
                date = ('Tipo no reconocido');
        }

        dashboardService.getDashboard({ dateRequested: date }).then((response) => {
            console.log(response);
            setDashboardData(response);
        }).catch((error) => {
            console.error(error);
        });        
        
      };

    let totalRevenue = 0;
    let abbreviatedRevenue = 0;
    let totalOrders = 0;
    let totalProducts = 0;
    let totalCategories = 0;

    if (dashboardData.data) {
        totalRevenue = Object.values(dashboardData.data.orderByEarnings).reduce((a, b) => a + b, 0);
        abbreviatedRevenue = abbreviateNumber(totalRevenue);
        totalOrders = Object.values(dashboardData.data.orderByQuantity).reduce((a, b) => a + b, 0);
        totalProducts = totalproducts.length;
        totalCategories = categories.length;
    }

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
                            <DateButton onClick={() => handleOtherButtonClick('yearly')}>Yearly</DateButton>
                            {/* <br/>
                            <DateButton onClick={() => handleOtherButtonClick('lastweek')}>Last Week</DateButton>
                            <DateButton onClick={() => handleOtherButtonClick('lastmonth')}>Last Month</DateButton>
                            <DateButton onClick={() => handleOtherButtonClick('lastyear')}>Last Year</DateButton> */}
                        </Buttons>

                        {datePickerVisible && isDateButtonClicked && (
                            <DatePick style={{ backgroundColor: 'transparent'}}>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    { datePickerType === 'weekly' && 
                                        <CustomizedDateTimePicker label={'YYYY-MM-DD'} views={['year', 'month', 'day']} format="YYYY-MM-DD" 
                                        value={selectedDate} onChange={handleDateChange} /> }

                                    { datePickerType === 'daily' && 
                                        <CustomizedDateTimePicker label={'YYYY-MM'} views={['month', 'year']} format="YYYY-MM" 
                                        value={selectedDate} onChange={handleDateChange} /> }

                                    { datePickerType === 'monthly' && 
                                        <CustomizedDateTimePicker label={'YYYY'} views={['year']} 
                                        value={selectedDate} onChange={handleDateChange} /> }
                                </LocalizationProvider>
                            </DatePick>
                        )}

                        <DateButton onClick={handleButtonClick}>Submit</DateButton>
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

                    <RevenueBarChartDiv>
                        <BarChartDiv>
                            <BarChart 
                            data={Object(dashboardData.data.orderByEarnings)} 
                            label={'Revenue'} 
                            max={Math.max(...Object.values(dashboardData.data.orderByEarnings))}/>
                        </BarChartDiv>
                    </RevenueBarChartDiv>

                    <div id="div6">
                        
                    </div> 

                    <SalesBarChartDiv>
                        <BarChartDiv>
                            <BarChart 
                            data={Object(dashboardData.data.orderByQuantity)} 
                            label={'Sales'} 
                            max={Math.max(...Object.values(dashboardData.data.orderByQuantity))}/>
                        </BarChartDiv>
                    </SalesBarChartDiv>  

                    <div id="div5">
                        
                    </div>  

                </Graphs>

            </MainContent>

        </Container>
    )
}

export default Dashboard;