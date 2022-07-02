import { Component } from 'react';
import { Link } from 'react-router-dom';
import { APPOINTMENT_STATUS, ROLE_DOCTOR } from '../../constants/GlobalConstants';
import AuthService from '../../services/AuthService';
import HospitalHeader from '../HospitalHeader';

class AppointmentListTableRow extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        const userRole = AuthService.getUserRole();

        let { editAppointment, viewAppointment } = this.props;
        let { id, status, patient, doctor, appointmentDetails: { startDate, endDate, email } } = this.props.data;

        let nameDisplay = '';
        let emailDisplay = '';

        if (userRole == ROLE_DOCTOR) {
            nameDisplay = `${patient.firstName} ${patient.lastName}`;
            emailDisplay = patient.email;
        } else {
            nameDisplay = `${doctor.firstName} ${doctor.lastName}`;
            emailDisplay = doctor.email;
        }

        const statusDisplay = Object.keys(APPOINTMENT_STATUS).find(key => {
            if (APPOINTMENT_STATUS[key] == status) {
                return true;
            }
            return false;
        });

        return <tr>
            <td>{id}</td>
            <td>
                {
                    userRole == ROLE_DOCTOR ? <Link to={`/user/info/${patient.id}`}>{nameDisplay}
                    </Link> : nameDisplay
                }
            </td>
            <td>{startDate}</td>
            <td>{endDate}</td>
            <td>{emailDisplay}</td>
            <td>{statusDisplay}</td>
            <td>
                <div className="appointment-row-options">
                    <button onClick={e => viewAppointment(id)} className="m-1 btn btn-info">View</button>
                    {
                        status == APPOINTMENT_STATUS.PENDING && <button onClick={e => editAppointment(id)} className="m-1 btn btn-warning">Update</button>
                    }
                </div>
            </td>
        </tr>;
    }
}

export default AppointmentListTableRow;
