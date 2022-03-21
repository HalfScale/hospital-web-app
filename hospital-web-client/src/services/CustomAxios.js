import axios from "axios"
import { BASE_API_SERVER_URL } from "../constants/GlobalConstants";
import AuthService from "./AuthService";

const CustomAxios = axios.create({
    baseURL: BASE_API_SERVER_URL
});

CustomAxios.interceptors.request.use(request => {
    console.log('request inteceptor', request);
    if(AuthService.getAuthenticatedUser()) {
        request.headers.authorization = `Bearer ${AuthService.getAuthenticatedUser()}`;
    }
    return request;
});

export default CustomAxios;