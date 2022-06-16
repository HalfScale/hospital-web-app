import Notifications from "../components/Notifications/Notifications";
import axios from "./CustomAxios";

class NotificationsService {

    getNotificationsByLoggedUser(queryParams) {
        return axios.get('/notifications', {
            params: queryParams
        });
    }

    getUnviewedNotifications() {
        return axios.get('/notifications/unviewed/count');
    }
    
    setNotificationViewedDate(notificationId) {
        return axios.put(`/notifications/viewed/${notificationId}`);
    }
}

export default new NotificationsService();