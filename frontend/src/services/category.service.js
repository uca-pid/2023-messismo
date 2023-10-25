import axios from "axios";
import authHeader from "./auth-header";
import { useSelector } from "react-redux";
import apiUrl from "../deploy";

const API_URL = "http://localhost:8080//api/v1/validatedEmployee/getAllProducts";

const getAllCategories = () => {
  
  return axios.get(apiUrl + "/api/v1/validatedEmployee/getAllCategories", { headers: authHeader() , method: 'GET',      
  'Content-Type' : 'application/json'});
};

const addCategory = (categoryName) => {
  
    return axios.post(apiUrl + "/api/v1/manager/category/addCategory", {categoryName: categoryName}, { headers: authHeader() , method: 'POST',      
    'Content-Type' : 'application/json'})
    .then(response => {
        console.log("Categoria agregada con exito:", response.data);
    })
    .catch(error => {
        console.error("Error al agregar la categoria:", error);
        throw error;
    })
  };

  const deleteCategory = (categoryName) => {
    const data = {
      categoryName: categoryName,
    };
    console.log(data);
    return axios.delete(apiUrl + "/api/v1/manager/category/deleteCategory" ,{data: data, headers: authHeader() ,      
    'Content-Type' : 'application/json'})
    .then(response => {
        console.log("Categoria eliminada con exito:", response.data);
    })
    .catch(error => {
        console.error("Error al eliminar la categoria:", error);
        throw error;
    })
  };

const categoryService = {
    getAllCategories,
    addCategory,
    deleteCategory
};

export default categoryService;