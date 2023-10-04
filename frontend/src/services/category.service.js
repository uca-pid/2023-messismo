import axios from "axios";
import authHeader from "./auth-header";
import { useSelector } from "react-redux";

const API_URL = "http://localhost:8080//api/v1/validatedEmployee/getAllProducts";

const getAllCategories = () => {
  
  return axios.get("http://localhost:8080/api/v1/validatedEmployee/getAllCategories", { headers: authHeader() , method: 'GET',      
  'Content-Type' : 'application/json'});
};


const categoryService = {
    getAllCategories,
};

export default categoryService;