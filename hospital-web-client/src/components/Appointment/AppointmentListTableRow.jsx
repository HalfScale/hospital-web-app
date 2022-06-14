import { Component } from 'react';
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

        let nameDisplay = ""

        if (userRole == ROLE_DOCTOR) {
            nameDisplay = `${patient.firstName} ${patient.lastName}`;
        } else {
            nameDisplay = `${doctor.firstName} ${doctor.lastName}`;
        }

        const statusDisplay = Object.keys(APPOINTMENT_STATUS).find(key => {
            if (APPOINTMENT_STATUS[key] == status) {
                return true;
            }
            return false;
        });

        return <tr>
            <td>{id}</td>
            <td>{nameDisplay}</td>
            <td>{startDate}</td>
            <td>{endDate}</td>
            <td>{email}</td>
            <td>{statusDisplay}</td>
            <td>
                <div>
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
