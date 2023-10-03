import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/auth";

const register = (username, email, password) => {
  return axios.post(API_URL + "/register", {
    username,
    email,
    password,
  });
};

const login = (email, password) => {
  return axios
    .post(API_URL + "/authenticate", {
      email,
      password,
    })
    .then((response) => {
      if ((response.data.access_token) && (response.data.role !== 'EMPLOYEE')) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }

      return response.data;
      
    });
};

const logout = () => {
  localStorage.removeItem("user");
};

const authService = {
  register,
  login,
  logout,
};

export default authService;