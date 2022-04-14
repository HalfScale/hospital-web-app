import axios from "./CustomAxios";

class MessagingService {

    sendMessage(data) {
        return axios.post('/message/send', data);
    }

    getMessageThread(receiverId, queryParams) {
        return axios.get(`/message/thread/${receiverId}`,{
            params: queryParams
        });
    }

    getThreadMessages(threadId, queryParams) {
        return axios.get(`/message/thread/messages/${threadId}`, {
            params: queryParams
        })
    }
}

export default new MessagingService();