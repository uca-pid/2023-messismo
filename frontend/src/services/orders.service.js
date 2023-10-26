import axios from "axios";
import authHeader from "./auth-header";
import apiUrl from "../deploy";

const API_URL_modify = apiUrl + "/api/v1/validatedEmployee/modifyOrder";
const API_URL_close = apiUrl + "/api/v1/validatedEmployee/closeOrder";
const API_URL_add = apiUrl + "/api/v1/validatedEmployee/addNewOrder";
const API_URL_get = apiUrl + "/api/v1/validatedEmployee/orders/getAllOrders";

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