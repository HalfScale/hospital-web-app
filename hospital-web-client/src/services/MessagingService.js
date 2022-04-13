import axios from "./CustomAxios";

class MessagingService {

    sendMessage(data) {
        return axios.post('/message/send', data);
    }
}

export default new MessagingService();