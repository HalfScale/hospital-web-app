import { Component } from 'react';

class SuccessfulRegistration extends Component {
    constructor(props) {
        super(props);

    }

    render() {
        return (
        <div className="mt-3 m-auto w-50 p-3 shadow rounded text-center">
            <h1 className="mb-3 text-primary">Registration Successful</h1>
            <p>
                Registration has been successfully confirmed. Please login.
            </p>
            <button onClick={() => this.props.navigate('/login')} type="submit" className="btn btn-primary">Login</button>
        </div>
        );
    }
}

export default SuccessfulRegistration;
