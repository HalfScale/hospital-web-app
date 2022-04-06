import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class SuccessfulRegistration extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
            <div className="mt-3 m-auto w-50 p-3 shadow rounded text-center">
                <HospitalHeader label="Registration Successfull"/>

                <p>
                    Registration has been successfully confirmed. Please login.
                </p>
                <button onClick={() => this.props.navigate('/login')} type="submit" className="btn btn-primary">Login</button>
            </div>
        );
    }
}

export default SuccessfulRegistration;
