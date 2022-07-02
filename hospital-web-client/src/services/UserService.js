import axios from "./CustomAxios";

class UserService {

    updateProfile(data) {
        return axios.put('/user/edit', data, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    getProfileImage(imageHash) {
        return axios.get(`/api/file/image/${imageHash}`);
    }

    getUserById(id) {
        return axios.get(`/user/${id}`);
    }
}

export default new UserService();