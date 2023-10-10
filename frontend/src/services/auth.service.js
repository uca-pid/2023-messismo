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
      if (response.data.access_token) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }

      return response.data;
    });
};

const logout = () => {
  localStorage.removeItem("user");
};

const forgotPassword = (email) => {
  return axios.post(API_URL + "/forgotPassword", email ,{
    headers: {
      'Content-Type': 'text/plain'
    }})
  .then(response => {
    console.log(response)
    return response.data;
  })
  .catch(error => {
    console.log(error);
    throw error
  });
  
  
};
const changePassword = (form) => {
  return axios.post(API_URL + "/changeForgottenPassword", form ,{ headers: {
    'Content-Type': 'application/json',
  }})
  .then(response => {
    console.log(response.data)
    return response.data;
  })
  .catch(error => {
    console.log(error)
    throw error
  });
  
};

const authService = {
  register,
  login,
  logout,
  forgotPassword,
  changePassword
};

export default authService;