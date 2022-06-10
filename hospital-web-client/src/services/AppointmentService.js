import axios from "./CustomAxios";

class AppointmentService {

    findDoctorsAppointment(doctorId, queryParam) {
        return axios.get(`/appointment/doctor/${doctorId}`, {
            params: queryParam
        });
    }

    findById(id) {

    }
    
    findAll() {

    }

    create(data) {
        return axios.post('/appointment/add', data);
    }

    update() {

    }

    deleteById() {
        
    }
}

export default new AppointmentService();