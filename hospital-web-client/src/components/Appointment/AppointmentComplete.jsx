import './styles/main.css';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class AppointmentComplete extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }

        this.return = this.return.bind(this);
    }

    return() {
        this.props.navigate('/');
    }

    render() {
        return <>
            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Room Details" />
                <p className="text-center fs-5">
                    Appointment successfully created. Please wait for the approval
                    regarding the appointment date.
                </p>

                <section className="button-section pb-2 text-center">
                    <button type="button" onClick={this.return} className="btn btn-primary">Return to Homepage</button>
                </section>
            </div>
        </>;
    }
}

export default AppointmentComplete;
