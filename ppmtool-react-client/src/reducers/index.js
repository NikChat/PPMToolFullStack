import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";

// will take an object that contains all the reducers
export default combineReducers({
  errors: errorReducer,
  project: projectReducer,
});
