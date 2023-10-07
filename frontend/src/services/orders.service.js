import axios from "axios";
import authHeader from "./auth-header";

const API_URL_add = "http://localhost:8080/api/v1/validatedEmployee/addNewOrder";
const API_URL_get = "http://localhost:8080/api/v1/manager/orders/getAllOrders";

const getAllOrders = () => {
  return axios.get(API_URL_get, { headers: authHeader() });
};

const addOrders = (order) => {
  const newOrder = {
    products: order.name,
    email: order.unitPrice,
    date: order.category,
    payment_method: order.payment_method,
  };
  
  return axios.post(API_URL_add, order, { headers: authHeader() , method: 'POST',      
  'Content-Type' : 'application/json'})
  .then(response => {
    console.log("Orden agregada con éxito:", response.data);
  })
  .catch(error => {
    console.error("Error al agregar la orden:", error);
  });
};



const ordersService = {
    getAllOrders,
    addOrders
};

export default ordersService;