import axios from "./CustomAxios";

class RoomReservation {

    findById(id) {
        return axios.get(`/roomReservation/details/${id}`);
    }

    findAll(queryParams) {
        return axios.get('/roomReservation', {
            params: queryParams
        });
    }

    findOverlappingReservation(data, queryParams) {
        return axios.post('/roomReservation/checkAvailability', data, {
            params: queryParams
        });
    }

    create(data) {
        return axios.post('/roomReservation/create', data);
    }

    update(data) {
        return axios.put('/roomReservation/update', data);
    }

    setStatusToCancelled(id) {
        return axios.put(`/roomReservation/update/cancel/${id}`);
    }

    setStatusToDone(id) {
        return axios.put(`/roomReservation/update/done/${id}`);
    }

    deleteById(id) {
        return axios.delete(`/roomReservation/delete/${id}`);
    }
}

export default new RoomReservation();