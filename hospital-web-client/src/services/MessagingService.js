import axios from "./CustomAxios";

class MessagingService {

    sendMessage(data) {
        return axios.post('/message/send', data);
    }

    deleteThread(data) {
        return axios.delete('/message/thread/delete', {
            data: data
        })
    }

    getMessageThread(receiverId, queryParams) {
        return axios.get(`/message/thread/${receiverId}`,{
            params: queryParams
        });
    }

    getThreadMessagesByuserId(userId, queryParams) {
        return axios.get(`/message/thread/messages/receiver/${userId}`, {
            params: queryParams
        });
    }

    getThreadMessages(threadId, queryParams) {
        return axios.get(`/message/thread/messages/${threadId}`, {
            params: queryParams
        })
    }

    getThreadByLoggedUser(queryParams) {
        return axios.get('/message/thread', {
            params: queryParams
        });
    }
}

export default new MessagingService();