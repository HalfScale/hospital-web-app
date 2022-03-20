import axios from "./CustomAxios";

class AuthService {

    checkIfEmailIsValid(email) {
        console.log('email', email);
        return axios.post('/auth/validate/email', {email: email});
    }

}

export default new AuthService();