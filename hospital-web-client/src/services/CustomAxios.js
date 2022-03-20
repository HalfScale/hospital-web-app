import axios from "axios"
import { BASE_API_SERVER_URL } from "../constants/GlobalConstants";

const CustomAxios = axios.create({
    baseURL: BASE_API_SERVER_URL
});

// instance.interceptors.request.use(request => {
//     console.log('request inteceptor', request);
//     if (AuthenticationService.isUserLoggedIn()) {
//         request.headers.authorization = `Bearer ${AuthenticationService.getLoggedInUser()}`;
//     }
//     return request;
// });

export default CustomAxios;