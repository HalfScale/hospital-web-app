import axios from "./CustomAxios";

class HospitalRoomService {

    findRoomById(id) {
        return axios.get(`/hospitalRoom/rooms/${id}`);
    }
    
    findAllRoom(queryParams) {
        return axios.get('/hospitalRoom/rooms', {
            params: queryParams
        });
    }

    addRoom(data) {
        // form data
    }

    updateRoom() {
        // form data
    }

    deleteRoom(id) {
        return axios.delete(`/hospitalRoom/delete/${id}`);
    }
}

export default new HospitalRoomService();