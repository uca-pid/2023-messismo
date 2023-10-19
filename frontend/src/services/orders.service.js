import axios from "axios";
import authHeader from "./auth-header";

const API_URL_add = "http://localhost:8080/api/v1/validatedEmployee/addNewOrder";
const API_URL_get = "http://localhost:8080/api/v1/validatedEmployee/orders/getAllOrders";
const API_URL_modify = "http://localhost:8080/api/v1/validatedEmployee/modifyOrder";
const API_URL_close = "http://localhost:8080/api/v1/validatedEmployee/closeOrder";

const getAllOrders = () => {
  return axios.get(API_URL_get, { headers: authHeader() });
};

const addOrders = (orderData) => {
  return axios.post(API_URL_add, orderData, { headers: authHeader() });
};

const modifyOrder = (orderData) => {
  return axios.post(API_URL_modify, orderData, { headers: authHeader() });
};

const closeOrder = (orderId) => {
  return axios.post(API_URL_close, orderId, { headers: authHeader() });
};

const ordersService = {
    getAllOrders,
    addOrders,
    modifyOrder,
    closeOrder
};

export default ordersService;