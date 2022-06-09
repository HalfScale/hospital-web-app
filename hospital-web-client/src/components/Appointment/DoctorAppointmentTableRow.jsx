import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class DoctorAppointmentTableRow extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { appointmentDetails: { startDate, endDate } } = this.props.data;
        return <tr>
            <td className="text-center">{startDate}</td>
            <td className="text-center">{endDate}</td>
        </tr>;
    }
}

export default DoctorAppointmentTableRow;
