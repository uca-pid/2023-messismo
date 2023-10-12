import axios from "axios";
import authHeader from "./auth-header";

const API_URL_add = "http://localhost:8080/api/v1/validatedEmployee/addNewOrder";
const API_URL_get = "http://localhost:8080/api/v1/validatedEmployee/orders/getAllOrders";

const getAllOrders = () => {
  return axios.get(API_URL_get, { headers: authHeader() });
};

const addOrders = (orderData) => {
  return axios.post(API_URL_add, orderData, { headers: authHeader() });
};

const ordersService = {
    getAllOrders,
    addOrders
};

export default ordersService;