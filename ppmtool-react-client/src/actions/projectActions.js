import axios from "axios";
import { GET_ERRORS, GET_PROJECTS, GET_PROJECT } from "./types";

/*** CREATE PROJECT ***/
export const createProject = (project, history) => async (dispatch) => {
  try {
    const res = await axios.post("http://localhost:8080/api/project", project);
    history.push("/dashboard");
    // clear errors object in redux state after successfull update:
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

/*** GET PROJECTS ***/
export const getProjects = () => async (dispatch) => {
  const res = await axios.get("http://localhost:8080/api/project/all");

  dispatch({
    type: GET_PROJECTS,
    payload: res.data,
  });
};

/*** GET PROJECT BY ID ***/
export const getProject = (id, history) => async (dispatch) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/project/${id}`);
    dispatch({
      type: GET_PROJECT,
      payload: res.data,
    });
  } catch (error) {
    history.push("/dashboard");
  }
};
