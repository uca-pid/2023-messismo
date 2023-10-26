import axios from "axios";
import authHeader from "./auth-header";
import apiUrl from "../deploy";

const API_URL = apiUrl + "/api/v1/manager/dashboard/getDashboard";


const getDashboard = (date) => {
  return axios.post(API_URL, date, { headers: authHeader() });
};

const dashboardService = {
    getDashboard,
};

export default dashboardService;