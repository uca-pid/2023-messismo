import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/v1/manager/dashboard/getDashboard";


const getDashboard = (date) => {
  return axios.post(API_URL, date, { headers: authHeader() });
};

const dashboardService = {
    getDashboard,
};

export default dashboardService;