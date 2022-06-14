import axios from "./CustomAxios";

class AppointmentService {

    findDoctorsAppointment(doctorId, queryParam) {
        return axios.get(`/appointment/doctor/${doctorId}`, {
            params: queryParam
        });
    }

    findById(id) {
        return axios.get(`/appointment/details/${id}`);
    }
    
    findAll(queryParam) {
        return axios.get('/appointment', {
            params: queryParam
        });
    }

    create(data) {
        return axios.post('/appointment/add', data);
    }

    update(id, data) {
        return axios.put(`/appointment/edit/${id}`, data);
    }

    editAppointmentStatus(id, data) {
        return axios.put(`/appointment/edit/status/${id}`, data);
    }
}

export default new AppointmentService();