import axios from "axios";
import authHeader from "./auth-header";
import apiUrl from "../deploy";

const API_URL_add = apiUrl + "/api/v1/validatedEmployee/addNewOrder";
const API_URL_get = apiUrl + "/api/v1/validatedEmployee/orders/getAllOrders";

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