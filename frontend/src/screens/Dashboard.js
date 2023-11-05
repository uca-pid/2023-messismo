import React, { useState, useEffect } from "react";
import { styled } from "styled-components";
// import 'fontsource-roboto';
import { useSelector } from "react-redux";
import Navbar from "../components/Navbar";
import { Navigate } from "react-router-dom";
import Box from "@mui/material/Box";
import Slider from "@mui/material/Slider";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import ProgressBar from "../components/ProgressBar";
import productsService from "../services/products.service";
import categoryService from "../services/category.service";
import { makeStyles } from "@mui/styles";
import dashboardService from "../services/dashboard.service";
import moment from "moment";
import dayjs from "dayjs";
import BarChart from "../components/BarChart";
import Doughnut from "../components/DoughnutChart";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { useTheme } from "@mui/material/styles";
import CircularProgress from "@mui/material/CircularProgress";
import Chip from "@mui/material/Chip";
import OutlinedInput from "@mui/material/OutlinedInput";

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
  display: ${(props) => (props.visible ? "flex" : "none")};
  justify-content: center;
  align-items: center;
`;

const Graphs = styled.div`
  @media (max-width: 1000px) {
    display: flex;
    flex-direction: column;
  }
`;

const Buttons = styled.div`
  margin-right: 2rem;
  margin-left: 5rem;

  @media (max-width: 1000px) {
    margin-right: 0rem;
    margin-left: 0rem;
  }
`;

const DateButton = styled.button`
  font-size: 1rem;
  border-radius: 3px;
  border: 1px solid black;
  background-color: #a4d4cc;
  color: black;
  text-transform: uppercase;
  cursor: pointer;
  font-family: "Roboto", serif;
  text-align: center;
  padding: 0.6rem;
  margin: 0.3rem;

  &:hover {
    background-color: #a7d0cd;
  }
  &:active {
    background-color: #a4d4cc;
    box-shadow: 0 3px #666;
    transform: translateY(4px);
  }
  &:focus {
    outline: none;
  }
`;

const ApplyButton = styled.button`
  font-size: 1rem;
  border-radius: 3px;
  border: 1px solid black;
  background-color: #a4d4cc;
  color: black;
  text-transform: uppercase;
  cursor: pointer;
  font-family: "Roboto", serif;
  text-align: center;
  padding: 0.6rem;
  margin: 0.3rem;
  margin-left: 2rem;

  &:hover {
    background-color: #a7d0cd;
  }
  &:active {
    background-color: #a4d4cc;
    box-shadow: 0 3px #666;
    transform: translateY(4px);
  }
  &:focus {
    outline: none;
  }

  @media (max-width: 1000px) {
    margin-left: 0rem;
    margin-right: 0rem;
    margin-top: 0rem;
    margin: 0.5rem;
  }
`;

const DatePick = styled.div`
  .datetext {
    color: white;
  }

  @media (max-width: 1000px) {
    padding: 1rem;
  }
`;

const DateFilter = styled.div`
  //background:white;
  width: 40%;
  height: 12vh;
  float: left;

  display: flex;
  align-items: center;
  margin-top: 2rem;

  @media (max-width: 1000px) {
    width: 100%;
    height: auto;
    flex-direction: column;
  }
`;

const TotalStats = styled.div`
  //background:yellow;
  width: 60%;
  height: 12vh;
  float: right;

  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;

  @media (max-width: 1000px) {
    width: 390px;
    height: auto;
    flex-direction: column;
    margin-top: 1rem;
  }
`;

const Stat = styled.div`
  width: 25%;
  font-size: 1.5rem;
  border: 1px solid black;
  color: black;
  letter-spacing: 1px;
  font-family: "Roboto", serif;
  padding: 1rem;
  margin: 1.5rem;
  color: white;
  box-shadow: -3px 0 0 rgb(157, 187, 191, 0.7);

  .statnumber {
    font-size: 2.2rem;
    margin-left: 1rem;
  }

  .stattext {
    font-size: 1.2rem;
    margin-left: 1rem;
  }

  &.total-revenue {
    background-color: rgb(157, 187, 191, 0.4);
  }

  &.total-orders {
    background-color: rgb(157, 187, 191, 0.3);
  }

  &.total-products {
    background-color: rgb(157, 187, 191, 0.4);
  }

  &.total-categories {
    background-color: rgb(157, 187, 191, 0.3);
  }

  @media (max-width: 1000px) {
    margin-left: 0rem;
    width: 60%;
    flex-direction: column;
    margin: 0;
    margin-top: 1rem;
    margin-bottom: 1rem;

    .statnumber {
      margin-left: 0rem;
    }

    .stattext {
      margin-left: 0rem;
    }
  }
`;

const OutOfStock = styled.div`
  //background:orange;
  width: 25%;
  height: 70vh;
  float: right;

  display: flex;
  justify-content: center;
  overflow-y: auto;

  @media (max-width: 1000px) {
    width: 390px;
    height: auto;
  }
`;

const StockList = styled.div`
  width: 80%;
  margin: 3rem;

  @media (max-width: 1000px) {
    margin: 2rem;
  }
`;

const RevenueBarChartDiv = styled.div`
  //background:red;
  width: 39%;
  height: 70vh;
  float: left;

  @media (max-width: 1000px) {
    width: 390px;
    height: 50vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
`;

const BarChartDiv = styled.div`
  max-height: 45%;
  margin-left: 6rem;
  margin-top: 1rem;

  @media (max-width: 1000px) {
    max-width: 90%;
    width: 100%;
    margin-left: 0rem;
  }
`;

const RevenueDoughnutDiv = styled.div`
  //background:purple;
  width: 36%;
  height: 70vh;
  float: right;

  @media (max-width: 1000px) {
    width: 390px;
    height: auto;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
`;

const DoughnutDiv = styled.div`
  max-width: 50%;
  display: flex;

  @media (max-width: 1000px) {
    max-width: 90%;
    height: auto;
    flex-direction: column;
  }
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
    color: "#a4d4cc",
  },
});

function Dashboard() {
  const { user: currentUser } = useSelector((state) => state.auth);
  const clicked = useSelector((state) => state.navigation.clicked);
  const classes = useStyles();
  const theme = useTheme();
  const [products, setProducts] = useState([]);
  const [totalproducts, setTotalProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [sliderValue, setSliderValue] = useState(15);
  const [datePickerVisible, setDatePickerVisible] = useState(false);
  const [datePickerType, setDatePickerType] = useState("");
  const [isDateButtonClicked, setIsDateButtonClicked] = useState(false);
  const [dashboardData, setDashboardData] = useState({
    data: {
      quantityCategoryDonut: {},
      quantityProductDonut: {},
      earningProductDonut: {},
      averageByOrder: {},
      orderByQuantity: {},
      earningCategoryDonut: {},
      orderByEarnings: {},
    },
  });

  const [chartType, setChartType] = useState("product");
  const [isLoading, setIsLoading] = useState(true);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [dateToShow, setDateToShow] = useState("");

  useEffect(() => {
    dashboardService
      .getDashboard({ dateRequested: "", categoryList: selectedCategories })
      .then((response) => {
        console.log(response);
        setDashboardData(response);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error(error);
        setIsLoading(false);
      });
  }, []);

  useEffect(() => {
    console.log(selectedCategories);
    dashboardService
      .getDashboard({ dateRequested: "", categoryList: selectedCategories })
      .then((response) => {
        console.log(response);
        setDashboardData(response);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error(error);
        setIsLoading(false);
      });
  }, [selectedCategories]);

  useEffect(() => {
    productsService
      .getAllProducts()
      .then((response) => {
        const filteredProducts = response.data.filter(
          (product) => product.stock <= sliderValue
        );
        setProducts(filteredProducts);
        setTotalProducts(response.data);
      })
      .catch((error) => {
        console.error("Error al mostrar los productos", error);
      });
  }, []);

  useEffect(() => {
    categoryService
      .getAllCategories()
      .then((response) => {
        setCategories(response.data);
        console.log(categories)
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
    setDatePickerVisible(
      type === "daily" || type === "weekly" || type === "monthly"
    );
    setIsDateButtonClicked(true);
  };

  const handleOtherButtonClick = (type) => {
    setDatePickerType(type);
    setIsDateButtonClicked(false);
    setDatePickerVisible(false);

    if (type === "yearly") {
      const yearly = "";
      setSelectedDate(yearly);
    } else if (type === "lastweek") {
      const today = new Date();
      const lastWeekDate = new Date(today.setDate(today.getDate() - 7));
      setSelectedDate(lastWeekDate);
    } else if (type === "lastmonth") {
      const today = new Date();
      const lastMonthFirstDay = new Date(
        today.getFullYear(),
        today.getMonth() - 1,
        1
      );
      setSelectedDate(lastMonthFirstDay);
    } else if (type === "lastyear") {
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
      case "daily":
      case "lastmonth":
        date = `${dateObject.getFullYear()}-${String(
          dateObject.getMonth() + 1
        ).padStart(2, "0")}`;
        break;
      case "weekly":
      case "lastweek":
        date = dateObject.toISOString().slice(0, 10);
        break;
      case "monthly":
      case "lastyear":
        date = dateObject.getFullYear().toString();
        break;
      case "yearly":
        date = selectedDate;
        break;
      default:
        date = "Tipo no reconocido";
    }


    setDateToShow(date);
    dashboardService
      .getDashboard({ dateRequested: date, categoryList: selectedCategories })
      .then((response) => {
        console.log(response);
        setDashboardData(response);
      })
      .catch((error) => {
        console.error(error);
      });
  };



  const handleChange = (event) => {
    setChartType(event.target.value);
  };

  let totalRevenue = 0;
  let abbreviatedRevenue = 0;
  let totalOrders = 0;
  let totalProducts = 0;
  let totalCategories = 0;

  if (dashboardData.data) {
    totalRevenue = Object.values(dashboardData.data.orderByEarnings).reduce(
      (a, b) => a + b,
      0
    );
    abbreviatedRevenue = abbreviateNumber(totalRevenue);
    totalOrders = Object.values(dashboardData.data.orderByQuantity).reduce(
      (a, b) => a + b,
      0
    );
    totalProducts = totalproducts.length;
    totalCategories = categories.length;
  }
  const handleCategoryChange = (event) => {
    const { value: selectedCategoryIds } = event.target;
  
    const selectedCats = categories.filter((category) =>
      selectedCategoryIds.includes(category.categoryId)
    );
  
    setSelectedCategories(selectedCats);
    handleButtonClick();
  };
  
  return (
    <Container>
      <Navbar />

      <MainContent visible={contentVisible}>
        {isLoading ? (
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              marginTop: "20%",
            }}
          >
            <CircularProgress style={{ color: "#a4d4cc" }} />
          </Box>
        ) : (
          <Graphs id="wrap">
            <DateFilter>
              <Buttons>
                <DateButton
                  onClick={() => handleDateButtonClick("daily")}
                  style={{
                    backgroundColor:
                      datePickerType === "daily" ? "#82B7AE" : "#a4d4cc",
                  }}
                >
                  Daily
                </DateButton>
                <DateButton
                  onClick={() => handleDateButtonClick("weekly")}
                  style={{
                    backgroundColor:
                      datePickerType === "weekly" ? "#82B7AE" : "#a4d4cc",
                  }}
                >
                  Weekly
                </DateButton>
                <DateButton
                  onClick={() => handleDateButtonClick("monthly")}
                  style={{
                    backgroundColor:
                      datePickerType === "monthly" ? "#82B7AE" : "#a4d4cc",
                  }}
                >
                  Monthly
                </DateButton>
                <DateButton
                  onClick={() => handleOtherButtonClick("yearly")}
                  style={{
                    backgroundColor:
                      datePickerType === "yearly" ? "#82B7AE" : "#a4d4cc",
                  }}
                >
                  Yearly
                </DateButton>
                {/* <br/>
                            <DateButton onClick={() => handleOtherButtonClick('lastweek')}>Last Week</DateButton>
                            <DateButton onClick={() => handleOtherButtonClick('lastmonth')}>Last Month</DateButton>
                            <DateButton onClick={() => handleOtherButtonClick('lastyear')}>Last Year</DateButton> */}
              </Buttons>

              {datePickerVisible && isDateButtonClicked && (
                <DatePick style={{ backgroundColor: "transparent" }}>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    {datePickerType === "weekly" && (
                      <CustomizedDateTimePicker
                        label={"YYYY-MM-DD"}
                        views={["year", "month", "day"]}
                        format="YYYY-MM-DD"
                        value={selectedDate}
                        onChange={handleDateChange}
                      />
                    )}

                    {datePickerType === "daily" && (
                      <CustomizedDateTimePicker
                        label={"YYYY-MM"}
                        views={["month", "year"]}
                        format="YYYY-MM"
                        value={selectedDate}
                        onChange={handleDateChange}
                      />
                    )}

                    {datePickerType === "monthly" && (
                      <CustomizedDateTimePicker
                        label={"YYYY"}
                        views={["year"]}
                        value={selectedDate}
                        onChange={handleDateChange}
                      />
                    )}
                  </LocalizationProvider>
                </DatePick>
              )}

              <ApplyButton onClick={handleButtonClick}>Apply</ApplyButton>
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
                <h2 style={{ color: "white" }}>Out of Stock soon</h2>
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
                  <p style={{ color: "white", marginBottom: "2rem" }}>
                    Products with {sliderValue} or less units
                  </p>

                  {products.map((producto, index) => (
                    <ProgressBar
                      key={index}
                      bgcolor={producto.stock !== 0 ? "#9fc16c" : "#d496bb"}
                      name={producto.name}
                      progress={producto.stock}
                      sliderValue={sliderValue}
                    />
                  ))}
                </Box>
              </StockList>
            </OutOfStock>

            <RevenueBarChartDiv>
              <BarChartDiv>
                <BarChart
                  data={Object(dashboardData.data.orderByEarnings)}
                  label={"Revenue"}
                  max={Math.max(
                    ...Object.values(dashboardData.data.orderByEarnings)
                  )}
                  color={"#b5a4e3"}
                />
              </BarChartDiv>

              <BarChartDiv>
                <BarChart
                  data={Object(dashboardData.data.orderByQuantity)}
                  label={"Sales"}
                  max={Math.max(
                    ...Object.values(dashboardData.data.orderByQuantity)
                  )}
                  color={"#d496bb"}
                />
              </BarChartDiv>
            </RevenueBarChartDiv>

            <RevenueDoughnutDiv>
              <Box
                sx={{
                  width: "50%",
                  [theme.breakpoints.down("sm")]: {
                    width: "80%",
                  },
                }}
              >
                <FormControl fullWidth variant="outlined">
                  <Select
                    labelStyle={{ color: "#ff0000" }}
                    sx={{
                      backgroundColor: "transparent",
                      color: "white",
                      fontSize: "1.5rem",
                      margin: "2rem",
                      color: "white",
                      ".MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      "&:hover .MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      ".MuiSvgIcon-root ": {
                        fill: "white !important",
                      },
                    }}
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    label="By"
                    value={chartType}
                    defaultValue="product"
                    onChange={handleChange}
                  >
                    <MenuItem sx={{ fontSize: "1.2rem" }} value="product">
                      By Product
                    </MenuItem>
                    <MenuItem sx={{ fontSize: "1.2rem" }} value="category">
                      By Category
                    </MenuItem>
                  </Select>
                </FormControl>
              </Box>
              <p style={{ color: "white"}}>Select Categories</p>
              <Box sx={{ width: "80%" }}>
                <FormControl fullWidth variant="outlined">
                  <Select
                    labelStyle={{ color: "#ff0000" }}
                    sx={{
                      backgroundColor: "transparent",
                      color: "white",
                      fontSize: "1.5rem",
                      margin: "2rem",
                      color: "white",
                      ".MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      "&:hover .MuiOutlinedInput-notchedOutline": {
                        borderColor: "rgba(228, 219, 233, 0.25)",
                      },
                      ".MuiSvgIcon-root ": {
                        fill: "white !important",
                      },
                    }}
                    labelId="category-select-label"
                    id="category-select"
                    value={selectedCategories.map((category) => category.categoryId)}
                    multiple
                    onChange={handleCategoryChange}
                    renderValue={(selected) =>
                        selectedCategories
                          .filter((category) => selected.includes(category.categoryId))
                          .map((category) => category.name)
                          .join(", ")
                      }
                      displayEmpty
                  >
                     <MenuItem disabled value="">
        <em>Categories</em>
      </MenuItem>
                    {categories.map((category) => (
                       <MenuItem key={category.categoryId} value={category.categoryId} style={{
                        backgroundColor: selectedCategories.some((selected) => selected.categoryId === category.categoryId)
                          ? "#A4D4CC"
                          : "transparent",
                        color: selectedCategories.some((selected) => selected.categoryId === category.categoryId) ? "white" : "black",
                      }}>
                        {category.name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Box>

              <DoughnutDiv>
                {chartType === "product" ? (
                  <>
                    <Doughnut
                      data={Object(dashboardData.data.earningProductDonut)}
                      label={"Revenue"}
                    />

                    <Doughnut
                      data={Object(dashboardData.data.quantityProductDonut)}
                      label={"Sales"}
                    />
                  </>
                ) : (
                  <>
                    <Doughnut
                      data={Object(dashboardData.data.earningCategoryDonut)}
                      label={"Revenue"}
                    />

                    <Doughnut
                      data={Object(dashboardData.data.quantityCategoryDonut)}
                      label={"Sales"}
                    />
                  </>
                )}
              </DoughnutDiv>
            </RevenueDoughnutDiv>
          </Graphs>
        )}
      </MainContent>
    </Container>
  );
}

export default Dashboard;
