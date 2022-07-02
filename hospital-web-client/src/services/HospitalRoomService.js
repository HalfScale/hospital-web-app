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
        return axios.post('/hospitalRoom/add', data, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    updateRoom(data) {
        return axios.put('/hospitalRoom/update', data, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    deleteRoom(id) {
        return axios.delete(`/hospitalRoom/delete/${id}`);
    }

    validateRoom(queryParams) {
        return axios.get('/hospitalRoom/validate', {
            params: queryParams
        });
    }
}

export default new HospitalRoomService();