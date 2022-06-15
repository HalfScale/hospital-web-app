import './styles/main.css';
import { Component } from 'react';
import moment from 'moment';
import HospitalHeader from '../HospitalHeader';
import AppointmentService from '../../services/AppointmentService';

class ConfirmAppointment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            doctorId: props.location.state.doctorId
        }

        this.formatDateTime = this.formatDateTime.bind(this);
        this.back = this.back.bind(this);
        this.confirmAppointment = this.confirmAppointment.bind(this);
    }

    componentDidMount() {
        console.log('passed state', this.props.location.state);
    }

    formatDateTime({ startDate, startHour, startMinute, startTimePeriod,
        endDate, endHour, endMinute, endTimePeriod }, format) {

        return {
            startDate: moment(`${startDate} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD hh:mm a').format(format),
            endDate: moment(`${endDate} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD hh:mm a').format(format)
        };
    }

    confirmAppointment() {
        const values = this.props.location.state;
        const { doctorId } = this.state;

        let data = {
            doctorId: doctorId,
            firstTime: values.firstTime,
            reasonForAppointment: values.reasonForAppointment,
            startDate: this.formatDateTime(values, 'YYYY-MM-DD HH:mm:ss').startDate,
            endDate: this.formatDateTime(values, 'YYYY-MM-DD HH:mm:ss').endDate,
            address: values.address
        };
        console.log('data', data);
        AppointmentService.create(data)
        .then(resp => {
            this.props.navigate('/appointment/create/complete', { state: 'success'});
        });
    }

    back() {
        let values = this.props.location.state;
        this.props.navigate(`/appointment/create/${this.state.doctorId}`, { state: values });
    }

    render() {
        let { firstName, lastName, address, gender, firstTime, email, mobileNo,
            reasonForAppointment } = this.props.location.state;
        return <>
            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Appointment Confirmation" />

                <p>Please confirm the appointment if the details displayed below are correct:</p>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">First name:</label>
                    <label className="col-sm-4 col-form-label text-muted">{firstName}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Last name:</label>
                    <label className="col-sm-4 col-form-label text-muted">{lastName}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Address:</label>
                    <label className="col-sm-4 col-form-label text-muted">{address}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Gender:</label>
                    <label className="col-sm-4 col-form-label text-muted">{gender == 1 ? 'Male' : 'Female'}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-4 col-form-label">First Time Hospital Visit?:</label>
                    <label className="col-sm-4 col-form-label text-muted">{firstTime === 'true' ? 'Yes' : 'No'}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Start Date:</label>
                    <label className="col-sm-4 col-form-label text-muted">{this.formatDateTime(this.props.location.state, 'YYYY-MM-DD hh:mm a').startDate}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">End Date:</label>
                    <label className="col-sm-4 col-form-label text-muted">{this.formatDateTime(this.props.location.state, 'YYYY-MM-DD hh:mm a').endDate}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Email:</label>
                    <label className="col-sm-4 col-form-label text-muted">{email}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-2 col-form-label">Mobile No:</label>
                    <label className="col-sm-4 col-form-label text-muted">{mobileNo}</label>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-4 col-form-label">Reason For Appointment:</label>
                    <label className="col-sm-4 col-form-label text-muted">{reasonForAppointment}</label>
                </div>

                <section className="button-section pb-2 text-center">
                    <button type="button" onClick={this.back} className="btn btn-primary me-2">Back</button>
                    <button type="submit" onClick={this.confirmAppointment} className="btn btn-primary">Confirm</button>
                </section>

            </div>
        </>;
    }
}

export default ConfirmAppointment;
