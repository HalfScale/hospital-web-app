import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class AppointmentList extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Appointment List" />
            </div>
        </>;
    }
}

export default AppointmentList;
