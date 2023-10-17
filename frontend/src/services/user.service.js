import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/test/";

const getEmployeeBoard = () => {
  return axios.get(API_URL + "EMPLOYEE", { headers: authHeader() });
};

const getValidatedEmployeeBoard = () => {
    return axios.get(API_URL + "VALIDATEDEMPLOYEE", { headers: authHeader() });
};

const getManagerBoard = () => {
  return axios.get(API_URL + "MANAGER", { headers: authHeader() });
};

const getAdminBoard = () => {
  return axios.get(API_URL + "ADMIN", { headers: authHeader() });
};

const userService = {
    getEmployeeBoard,
    getValidatedEmployeeBoard,
    getManagerBoard,
    getAdminBoard,
};

export default userService