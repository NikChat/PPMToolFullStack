import { combineReducers } from "redux";
import errorReducer from "./errorReducer";

// will take an object that contains all the reducers
export default combineReducers({
  errors: errorReducer,
});
