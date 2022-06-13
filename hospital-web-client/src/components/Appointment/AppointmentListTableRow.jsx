import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class AppointmentListTableRow extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Room Details" />
            </div>
        </>;
    }
}

export default new AppointmentListTableRow();
