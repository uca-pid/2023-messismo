import { emphasize } from "@mui/material";
import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/test/";

const getAllEmployees = () => {
  return axios.get("http://localhost:8080/api/v1/manager/getAllEmployees", { headers: authHeader() , method: 'GET',      
  'Content-Type' : 'application/json'});
};


const validateEmployee = (employeeId) => {
    console.log(employeeId)
    return axios.put(`http://localhost:8080/api/v1/manager/validateEmployee/${employeeId}`, { headers: authHeader() , method: 'PUT',      
    'Content-Type' : 'application/json'})
    .then(response => {
        console.log("Empleado validado con éxito:", response.data);
      })
      .catch(error => {
        console.error("Error al validar al empleado:", error);
      });
  };
  
const validateAdmin = (employeeId) => {
    return axios.put(`http://localhost:8080/api/v1/admin/validateAdmin/${employeeId}`,{ headers: authHeader() , method: 'PUT',      
    'Content-Type' : 'application/json'})
    .then(response => {
        console.log("Administrador validado con éxito:", response.data);
      })
      .catch(error => {
        console.error("Error al validar al administrador:", error);
      });
}



const employeeService = {
    getAllEmployees,
    validateEmployee,
    validateAdmin,

};

export default employeeService;