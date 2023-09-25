import axios from "axios";
import authHeader from "./auth-header";
import { useSelector } from "react-redux";

const API_URL = "http://localhost:8080/api/v1/employee/getAllProducts";

const getAllProducts = () => {
  
  return axios.get(API_URL, { headers: authHeader() , method: 'GET',      
  'Content-Type' : 'application/json'});
};

const addProducts = (product) => {
  const newProduct = {
    name: product.name,
    unitPrice: product.unitPrice,
    category: product.category,
    description: product.description,
  };
  
  return axios.post("http://localhost:8080/api/v1/validatedEmployee/product/addProduct", product, { headers: authHeader() , method: 'POST',      
  'Content-Type' : 'application/json'})
  .then(response => {
    console.log("Producto agregado con éxito:", response.data);
  })
  .catch(error => {
    console.error("Error al agregar el producto:", error);
  });
};


const productsService = {
    getAllProducts,
    addProducts,
};

export default productsService;