import axios from "./CustomAxios";
import { AUTHENTICATED_USER } from "../constants/GlobalConstants";
import jwt_decode from "jwt-decode";

class AuthService {

    registerUser(data) {
        return axios.post('/auth/register', data);
    }

    checkIfEmailIsValid(email) {
        return axios.post('/auth/validate/email', {email: email});
    }

    checkIfDoctorCodeIsValid(doctorCode) {
        return axios.post('/auth/validate/doctorCode', {doctorCode: doctorCode});
    }

    login(data) {
        return axios.post('/auth/login', data);
    }

    logout() {
        sessionStorage.clear();
    }

    isLoggedIn() {
        return this.getAuthenticatedUser() !== null ? true: false
    }

    setAuthenticatedUser(user) {
        sessionStorage.setItem(AUTHENTICATED_USER, user);
    }

    getAuthenticatedUser() {
        return sessionStorage.getItem(AUTHENTICATED_USER);
    }

    getUserFullName() {
        if(this.isLoggedIn()) {
            let token = this.getAuthenticatedUser();
            let decodedToken = jwt_decode(token);
            return decodedToken.name;
        }
        return '';
    }

    getLoggedInUser() {
        return axios.get('/auth/user');
    }

    test() {
        return axios.get('/test');
    }

}

export default new AuthService();