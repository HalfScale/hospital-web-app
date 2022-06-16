import { Component } from 'react';
import NotificationsService from '../../services/NotificationsService';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-solid-svg-icons";

class NotificationPane extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let { id, appointmentId, status, message, viewed } = this.props.data;
        let { toggleAppointment } = this.props;

        return <div className="w-75 m-auto d-flex flex-row justify-content-center fw-bold text-center m-2 mb-3 shadow rounded message-box ">
            <div className="d-flex flex-column justify-content-center me-4">
                <FontAwesomeIcon className="" icon={faBell} size="2xl" />
            </div>
            <div className="d-flex flex-column justify-content-center">
                <p>{message}</p>
                <Link onClick={e => {
                    NotificationsService.setNotificationViewedDate(id);
                }} to={`/appointment/details/${appointmentId}`}>Please click here to see the details.</Link>
            </div>
        </div>;
    }
}

export default NotificationPane;
