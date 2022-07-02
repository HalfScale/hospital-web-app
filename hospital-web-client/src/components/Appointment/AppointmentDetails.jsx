import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import AppointmentService from '../../services/AppointmentService';
import AuthService from '../../services/AuthService';
import { APPOINTMENT_STATUS, ROLE_DOCTOR, ROLE_PATIENT } from '../../constants/GlobalConstants';
import { Modal, Button } from 'react-bootstrap'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class AppointmentDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            appointmentId: props.params.appointmentId,
            firstName: '',
            lastName: '',
            gender: '',
            address: '',
            email: '',
            mobileNo: '',
            firstTime: '',
            startDate: '',
            endDate: '',
            reasonForAppointment: '',
            status: '',
            displayStatus: 'Pending',
            modalHeaderText: '',
            modalBodyText: '',
            cancelReason: '',
            cancelReasonDisplay: '',
            toBeApproved: false,
        }

        this.editAppointmentStatus = this.editAppointmentStatus.bind(this);
        this.editAppointment = this.editAppointment.bind(this);
        this.displayStatus = this.displayStatus.bind(this);
        this.fetchAppointment = this.fetchAppointment.bind(this);
        this.back = this.back.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.reasonOnChange = this.reasonOnChange.bind(this);
    }

    componentDidMount() {
        this.fetchAppointment();
    }

    fetchAppointment() {
        let { appointmentId } = this.state;
        AppointmentService.findById(appointmentId)
            .then(resp => {
                let { status, appointmentDetails: { firstName, lastName, gender,
                    address, email, firstTime, mobileNo, startDate, endDate,
                    reasonForAppointment, cancelReason } } = resp.data;

                this.setState({
                    displayStatus: status,
                    cancelReasonDisplay: cancelReason,
                    firstName: firstName,
                    lastName: lastName,
                    gender: gender,
                    address: address,
                    firstTime: firstTime,
                    email: email,
                    mobileNo: mobileNo,
                    startDate: startDate,
                    endDate: endDate,
                    reasonForAppointment: reasonForAppointment
                });
            }).catch(err => {
                this.props.navigate('/');
            });
    }

    editAppointmentStatus({ modalHeaderText, modalBodyText, toBeApproved, status }) {

        this.setState({
            showModal: true,
            modalHeaderText: modalHeaderText,
            modalBodyText: modalBodyText,
            toBeApproved: toBeApproved == true,
            status: status
        });
    }

    displayStatus(status) {

        if (status == 1) {
            return 'Pending';
        }
        if (status == 2) {
            return 'Approved';
        }
        if (status == 3) {
            return 'Cancelled';
        }
        if (status == 4) {
            return 'Rejected';
        }
    }

    closeModal() {
        this.setState({
            showModal: false,
            cancelReason: ''
        });
    }

    reasonOnChange(event) {
        this.setState({
            cancelReason: event.target.value
        });
    }

    onSubmit() {
        let { appointmentId, cancelReason, status } = this.state;

        let data = {
            statusCode: status,
            reason: cancelReason ? cancelReason : null
        }

        AppointmentService.editAppointmentStatus(appointmentId, data)
            .then(resp => {
                this.setState({
                    showModal: false
                }, () => {
                    toast.success('Edit appointment successful!');
                    this.fetchAppointment();
                });
            });
    }

    editAppointment() {
        let { appointmentId } = this.state;
        this.props.navigate(`/appointment/edit/${appointmentId}`);
    }

    back() {
        this.props.navigate('/appointment');
    }

    render() {
        let { showModal, displayStatus, cancelReasonDisplay, firstName, lastName, address, gender, firstTime, mobileNo,
            email, reasonForAppointment, startDate, endDate, modalHeaderText, modalBodyText,
            toBeApproved } = this.state;

        return <>
            <Modal
                show={showModal}
                onHide={this.closeModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>{modalHeaderText}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <p>{`${modalBodyText}:`}</p>
                    {
                        !toBeApproved && <textarea onChange={this.reasonOnChange} className="form-control"></textarea>
                    }
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={this.onSubmit}>
                        Submit
                    </Button>
                </Modal.Footer>

            </Modal>

            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Appointment Details" />

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">First name:</label>
                    <label className="col-sm-4 col-form-label text-muted">{firstName}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Last name:</label>
                    <label className="col-sm-4 col-form-label text-muted">{lastName}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Address:</label>
                    <label className="col-sm-4 col-form-label text-muted">{address}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Gender:</label>
                    <label className="col-sm-4 col-form-label text-muted">{gender == 1 ? 'Male' : 'Female'}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">First Time:</label>
                    <label className="col-sm-4 col-form-label text-muted">{firstTime == 'true' ? 'Yes' : 'No'}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Start Date:</label>
                    <label className="col-sm-4 col-form-label text-muted">{startDate}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">End Date:</label>
                    <label className="col-sm-4 col-form-label text-muted">{endDate}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Mobile No:</label>
                    <label className="col-sm-4 col-form-label text-muted">{mobileNo}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Email:</label>
                    <label className="col-sm-4 col-form-label text-muted">{email}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Reason for Appointment:</label>
                    <label className="col-sm-4 col-form-label text-muted">{reasonForAppointment}</label>
                </div>

                <div className="justify-content-center text-center mb-3 row">
                    <label className="fw-bold col-sm-2 col-form-label">Appointment Status:</label>
                    <label className="col-sm-4 col-form-label text-muted">{this.displayStatus(displayStatus)}</label>
                </div>

                {
                    cancelReasonDisplay && <div className="justify-content-center text-center mb-3 row">
                        <label className="fw-bold col-sm-2 col-form-label">Cancel Reason:</label>
                        <label className="col-sm-4 col-form-label text-muted">{cancelReasonDisplay}</label>
                    </div>
                }

                <section className="button-section pb-2 text-center">
                    {
                        AuthService.getUserRole() === ROLE_PATIENT &&
                        displayStatus == APPOINTMENT_STATUS.PENDING &&
                        <>
                            <button type="submit" onClick={this.editAppointment} className="btn btn-primary me-2">Edit</button>
                            <button type="button" onClick={e => {
                                this.editAppointmentStatus({
                                    modalHeaderText: 'Cancel Appointment',
                                    modalBodyText: 'Reason for cancellation',
                                    status: APPOINTMENT_STATUS.CANCELLED
                                });
                            }} className="btn btn-primary me-2">Cancel Appointment</button>
                        </>
                    }
                    {
                        AuthService.getUserRole() === ROLE_DOCTOR &&
                        displayStatus == APPOINTMENT_STATUS.PENDING &&
                        <>
                            <button type="submit" onClick={e => {
                                this.editAppointmentStatus({
                                    modalHeaderText: 'Approve Appointment',
                                    modalBodyText: 'Are you sure you want to approve the scheduled appointment',
                                    status: APPOINTMENT_STATUS.APPROVED,
                                    toBeApproved: true
                                });
                            }} className="btn btn-primary me-2">Approve Appointment</button>

                            <button type="submit" onClick={e => {
                                this.editAppointmentStatus({
                                    modalHeaderText: 'Reject Appointment',
                                    modalBodyText: 'Reason for rejection',
                                    status: APPOINTMENT_STATUS.REJECT
                                });
                            }} className="btn btn-primary me-2">Reject Appointment</button>
                        </>
                    }

                    <button type="button" onClick={this.back} className="btn btn-primary">Back</button>
                </section>
            </div>

            <ToastContainer className="text-center"
                position="bottom-center"
                autoClose={3000}
                hideProgressBar={true}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                pauseOnHover={false}
                draggable={false}
                theme="colored" />
        </>;
    }
}

export default AppointmentDetails;
