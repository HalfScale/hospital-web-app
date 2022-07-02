import axios from "./CustomAxios";
import { AUTHENTICATED_USER, BASE_API_SERVER_URL } from "../constants/GlobalConstants";
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

    getUserId() {
        if(this.isLoggedIn()) {
            let token = this.getAuthenticatedUser();
            let decodedToken = jwt_decode(token);
            return decodedToken.id;
        }

        return null;
    }

    getUserRole() {
        if(this.isLoggedIn()) {
            let token = this.getAuthenticatedUser();
            let decodedToken = jwt_decode(token);
            return decodedToken.roles[0].authority;
        }

        return null;
    }

    getUserProfileImage() {
        if(this.isLoggedIn()) {
            let token = this.getAuthenticatedUser();
            let decodedToken = jwt_decode(token);

            if(decodedToken['profile_img']) {
                return `${BASE_API_SERVER_URL}/api/file/img/profile/${decodedToken.profile_img}`;
            }
        }
        return null;
    }

    fetchUserFromAPI() {
        return axios.get('/auth/user');
    }
}

export default new AuthService();