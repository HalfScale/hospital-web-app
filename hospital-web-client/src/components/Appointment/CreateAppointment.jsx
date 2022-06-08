import './styles/main.css';
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import moment from 'moment';
import HospitalHeader from '../HospitalHeader';
import AuthService from '../../services/AuthService';
import AppointmentService from '../../services/AppointmentService';
import DoctorsService from '../../services/DoctorsService';
import getYupValidation from '../../utils/YupValidationFactory';

class CreateAppointment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            doctorId: props.params.doctorId,
            firstName: '',
            lastName: '',
            address: '',
            email: '',
            mobileNo: '',
            firstTime: 'true',
            startDate: '',
            endDate: '',
            startHour: '01',
            startMinute: '00',
            startTimePeriod: 'am',
            endHour: '01',
            endMinute: '00',
            endTimePeriod: 'pm',
            reasonForAppointment: ''
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.handleStartHourChange = this.handleStartHourChange.bind(this);
        this.handleStartMinuteChange = this.handleStartMinuteChange.bind(this);
        this.handleStartTimePeriodChange = this.handleStartTimePeriodChange.bind(this);
        this.handleStartDateChange = this.handleStartDateChange.bind(this);
        this.handleEndDateChange = this.handleEndDateChange.bind(this);
        this.handleEndHourChange = this.handleEndHourChange.bind(this);
        this.handleEndMinuteChange = this.handleEndMinuteChange.bind(this);
        this.handleEndTimePeriodChange = this.handleEndTimePeriodChange.bind(this);
    }

    componentDidMount() {
        AuthService.fetchUserFromAPI()
            .then(resp => {
                console.log('fetchUserFromAPI', resp);
                let { firstName, lastName, address, users: { email }, mobileNo } = resp.data;
                this.setState({
                    firstName: firstName,
                    lastName: lastName,
                    address: address,
                    email: email,
                    mobileNo: mobileNo
                });

                return DoctorsService.getDoctorDetails(this.state.doctorId);
            })
            .then(resp => {
                console.log('getDoctorDetails', resp);
            }).catch(error => this.props.navigate('/'));
    }

    handleStartDateChange(event) {
        console.log('handleStartDateChange', event.target.value);
    }

    handleStartHourChange(event) {
        console.log('handleStartDateChange', event.target.value);
    }

    handleStartMinuteChange(event) {
        console.log('handleStartDateChange', event.target.value);
    }

    handleStartTimePeriodChange(event) {
        console.log('handleStartDateChange', event.target.value);
    }

    handleEndDateChange(event) {
        console.log('handleEndDateChange', event.target.value);
    }

    handleEndHourChange(event) {
        console.log('handleEndHourChange', event.target.value);
    }

    handleEndMinuteChange(event) {
        console.log('handleEndMinuteChange', event.target.value);
    }

    handleEndTimePeriodChange(event) {
        console.log('handleEndTimePeriodChange', event.target.value);
    }

    onSubmit(values) {
        console.log('onSubmit', values);
        this.props.navigate('/appointment/create/confirm', { state: values });
    }

    render() {
        let { firstName, lastName, address, email, mobileNo, startDate, endDate,
            startHour, startMinute, startTimePeriod, endHour,
            endMinute, endTimePeriod, reasonForAppointment, firstTime } = this.state;

        let appointmentSchema = getYupValidation('appointment');

        return <>
            <div className="mt-3 common-container m-auto w-50 rounded shadow">
                <HospitalHeader label="Create Appointment" />

                <Formik
                    initialValues={{
                        startDate, endDate, startHour, startMinute, startTimePeriod, endHour,
                        endMinute, endTimePeriod, reasonForAppointment, firstTime
                    }}
                    onSubmit={this.onSubmit}
                    validationSchema={appointmentSchema}
                    validateOnBlur={false}
                    validateOnChange={false}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">First name:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{firstName}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">Last Name:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{lastName}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">Address:</label>
                                    {/* <label className="col-sm-6 col-form-label text-muted">{address}</label> */}
                                    <div className="col-sm-6">

                                        <Field type="text" className="col-sm-6 form-control" name="startDate" />
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-4 col-form-label fs-5">First Time Hospital Visit?:</label>
                                    <div className="col-sm-2 form-check form-check-inline">
                                        <Field className="form-check-input" type="radio" name="firstTime" value="true" />
                                        <label className="form-check-label">Yes</label>
                                    </div>
                                    <div className="col-sm-2 form-check form-check-inline">
                                        <Field className="form-check-input" type="radio" name="firstTime" value="false" />
                                        <label className="form-check-label">No</label>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">Start Date:</label>
                                    <div className="col-sm-3">
                                        <Field min={moment().format('YYYY-MM-DD')} onChange={this.handleStartDateChange} type="date" className="form-control" name="startDate" />
                                        <ErrorMessage name="startDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleStartHourChange} as="select" className="form-select mb-3" name="startHour" >
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleStartMinuteChange} as="select" className="form-select mb-3" name="startMinute" >
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleStartTimePeriodChange} as="select" className="form-select mb-3" name="startTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">End Date:</label>
                                    <div className="col-sm-3">
                                        <Field min={moment().format('YYYY-MM-DD')} onChange={this.handleEndDateChange} type="date" className="form-control" name="endDate" />
                                        <ErrorMessage name="endDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleEndHourChange} as="select" className="form-select mb-3" name="endHour" >
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleEndMinuteChange} as="select" className="form-select mb-3" name="endMinute" >
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleEndTimePeriodChange} as="select" className="form-select mb-3" name="endTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">Email:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{email}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label fs-5">Mobile No:</label>
                                    <label className="col-sm-3 col-form-label text-muted">{mobileNo}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-4 col-form-label fs-5">Reason for Appointment</label>
                                    <div className="col-sm-6">
                                        <Field as="textarea" className="form-control" name="reasonForAppointment" />
                                        <ErrorMessage name="reasonForAppointment" component="div" className="text-red" />
                                    </div>
                                </div>

                                <section className="pb-2 text-center">
                                    <button type="button" onClick={this.back} className="btn btn-primary me-2">Back</button>
                                    <button type="submit" className="btn btn-primary">Create</button>
                                </section>

                            </Form>
                        )
                    }
                </Formik>
            </div>
        </>;
    }
}

export default CreateAppointment;
