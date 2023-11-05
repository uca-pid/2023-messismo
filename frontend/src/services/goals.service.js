import axios from "axios";
import authHeader from "./auth-header";
import apiUrl from "../deploy";

const get_goals = apiUrl + "/api/v1/manager/goals/getGoals";
const add_goal = apiUrl + "/api/v1/manager/goals/addGoal";
const edit_goal = apiUrl + "/api/v1/manager/goals/modifyGoal";
const delete_goal = apiUrl + "/api/v1/manager/goals/deleteGoal";

const getAllGoals = (goalData) => {
  return axios.post(get_goals, goalData, { headers: authHeader() });
};

const addGoal = (goalData) => {
  return axios.post(add_goal, goalData, { headers: authHeader() });
};

const modifyGoal = (goalData) => {
  return axios.post(edit_goal, goalData, { headers: authHeader() });
};

const deleteGoal = (goalId) => {
  return axios.post(delete_goal, goalId, { headers: authHeader() });
};


const goalsService = {
    getAllGoals,
    addGoal,
    modifyGoal,
    deleteGoal
};

export default goalsService;