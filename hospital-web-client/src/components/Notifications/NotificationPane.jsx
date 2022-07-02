import { Component } from 'react';
import NotificationsService from '../../services/NotificationsService';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell, faStar } from "@fortawesome/free-solid-svg-icons";
import defaultImage from '../default.png';
import { buildProfileURL } from '../../utils/Utils';
import { APPOINTMENT_STATUS } from '../../constants/GlobalConstants';


class NotificationPane extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
        this.generateNotificationMessage = this.generateNotificationMessage.bind(this);
    }

    generateNotificationMessage(status) {

        // <Link onClick={e => {
        //     NotificationsService.setNotificationViewedDate(id);
        // }} to={`/appointment/details/${appointmentId}`}>Please click here to see the details.</Link>
        let message = '';

        if (status == APPOINTMENT_STATUS.APPROVED) {
            message = 'Approved your appointment.';
        }
        else if (status == APPOINTMENT_STATUS.REJECT) {
            message = 'Rejected your appointment'
        }
        else if (status == APPOINTMENT_STATUS.PENDING) {
            message = 'Created an appointment.'
        }
        else if (status == APPOINTMENT_STATUS.CANCELLED) {
            message = 'Cancelled an appointment.'
        }

        return message;
    }

    render() {
        let { isEmpty, toggleAppointment } = this.props;


        if (!isEmpty) {
            let { id, appointmentId, status, senderFirstName, senderLastName, message, senderImage, viewed } = this.props.data;
            return <div className="notification-pane bg-primary bg-gradient justify-content-between p-2 m-auto d-flex flex-row justify-content-center text-center m-2 mb-3 shadow rounded message-box ">
                {
                    !viewed && <span className="position-absolute translate-middle badge rounded-pill bg-danger">.</span>
                }
                <div className="d-flex flex-column justify-content-center me-4">
                    <img src={senderImage ? buildProfileURL(senderImage) : defaultImage} alt="mdo" width="48" height="48" className="me-3 rounded-circle" />
                    {/* <span className="notif-unread-badge position-relative badge rounded-pill bg-danger">unread</span> */}
                </div>
                <div className="text-white d-flex flex-column justify-content-center">
                    <span className="notification-msg-header">{`${senderFirstName} ${senderLastName}`}</span>
                    <span className="notification-msg-body">{this.generateNotificationMessage(status)}</span>
                </div>
                <div className="ms-4 d-flex flex-column justify-content-center">
                    <button onClick={e => {
                        NotificationsService.setNotificationViewedDate(id);
                        toggleAppointment(appointmentId);
                    }} type="button" className="btn btn-secondary btn-sm">View</button>
                </div>
            </div>;
        }

        return <div className="w-75 m-auto d-flex flex-row justify-content-center text-center m-2 mb-3 shadow rounded message-box ">
            <div className="mt-3 d-flex flex-column justify-content-center">
                <p>No notifications at the moment.</p>
            </div>
        </div>;
    }
}

export default NotificationPane;
