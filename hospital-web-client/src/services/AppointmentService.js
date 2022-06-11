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
    
    findAll() {

    }

    create(data) {
        return axios.post('/appointment/add', data);
    }

    update(id, data) {
        return axios.put(`/appointment/edit/${id}`, data);
    }

    deleteById() {
        
    }
}

export default new AppointmentService();