import './styles/main.css';
import { Component } from 'react';
import AuthService from '../../services/AuthService';
import { ToastContainer, toast } from 'react-toastify';
import HospitalHeader from '../HospitalHeader';

class ConfirmRegistration extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            data: this.props.location.state
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.return = this.return.bind(this);
    }

    onSubmit() {
        this.setState({ submitting: true });

        AuthService.registerUser(this.state.data)
            .then(resp => this.props.navigate('/registration/success', { state: 'success' }))
            .catch(err => console.log('error', err));
    }

    return() {
        this.props.navigate('/registration', { state: this.state.data });
    }

    render() {
        const { firstName, lastName, email, gender, mobileNo, hospitalCode } = this.props.location.state;

        return <>
            <div className="mt-3 registration-container p-3 shadow rounded text-center">

                <HospitalHeader label="Confirm details" />

                <div className="row mb-3">
                    <div className="col-sm-6">
                        <label className="me-2 fw-bold fs-4">First Name:</label>
                        <label className="fs-4">{firstName}</label>
                    </div>
                    <div className="col-sm-6">
                        <label className="me-2 fw-bold fs-4">Last Name:</label>
                        <label className="fs-4">{lastName}</label>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-sm-6">
                        <label className="me-2 fw-bold fs-4">Email:</label>
                        <label className="fs-4">{email}</label>
                    </div>
                    <div className="col-sm-6">
                        <label className="me-2 fw-bold fs-4">Gender:</label>
                        <label className="fs-4">{gender == "1" ? 'Male' : 'Female'}</label>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col-sm-6">
                        <label className="me-2 fw-bold fs-4">Mobile No:</label>
                        <label className="fs-4">{mobileNo}</label>
                    </div>
                    {
                        hospitalCode && <div className="col-sm-6">
                            <label className="me-2 fw-bold fs-4">Doctor Code:</label>
                            <label className="fs-4">{hospitalCode}</label>
                        </div>
                    }

                </div>

                <section className="button-section pb-2 text-center">
                    <button onClick={this.onSubmit} type="button" className="me-3 btn btn-primary " disabled={this.state.submitting}>
                        {
                            this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                Loading...</>) : 'Register'

                        }
                    </button>
                    <button onClick={this.return} type="submit" className="btn btn-primary" disabled={this.state.submitting}>
                        Back
                    </button>
                </section>
                <ToastContainer />
            </div>
        </>;
    }
}

export default ConfirmRegistration;
