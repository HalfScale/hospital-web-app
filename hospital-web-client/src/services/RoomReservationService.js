import axios from "./CustomAxios";

class RoomReservation {

    findById(id) {
        return axios.get(`/roomReservations/rooms/${id}`);
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

    create() {

    }

    update() {

    }

    deleteById() {

    }
}

export default new RoomReservation();