import axios from "./CustomAxios";

class DoctorService {

    getDoctors(queryParams) {
        return axios.get('/doctors', {
            params: queryParams
        });
    }

    getDoctorDetails(id) {
        return axios.get(`/doctors/details/${id}`);
    }
}

export default new DoctorService();