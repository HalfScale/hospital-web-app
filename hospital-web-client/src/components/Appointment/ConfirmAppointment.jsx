import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class ConfirmAppointment extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Confirm Appointment" />
            </div>
        </>;
    }
}

export default ConfirmAppointment;
