import axios from "./CustomAxios";

class DoctorService {

    getDoctors(queryParams) {
        return axios.get('/doctors', {
            params: queryParams
        });
    }
}

export default new DoctorService();