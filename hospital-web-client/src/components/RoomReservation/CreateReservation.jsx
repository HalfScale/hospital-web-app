import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import HospitalRoomService from '../../services/HospitalRoomService';
import defaultRoomImg from '../HospitalRoom/room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';
import DatePicker from 'react-date-picker'
import { toHaveStyle } from '@testing-library/jest-dom/dist/matchers';
import { ErrorMessage, Field, Form, Formik } from 'formik';
import moment from 'moment';
import getYupValidation from '../../utils/YupValidationFactory';

class CreateReservation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomId: this.props.params.roomId,
            image: defaultRoomImg,
            roomName: '',
            hasAssociated: '',
            startDate: '',
            endDate: '',
            startHour: '01',
            startMinute: '00',
            startTimePeriod: 'am',
            endHour: '01',
            endMinute: '00',
            endTimePeriod: 'pm',
            hasAssociated: 'false',
            associatedId: ''
        }

        this.handleStartDateChange = this.handleStartDateChange.bind(this);
        this.handleEndDateChange = this.handleEndDateChange.bind(this);
        this.handleStartHourChange = this.handleStartHourChange.bind(this);
        this.handleStartMinuteChange = this.handleStartMinuteChange.bind(this);
        this.handleEndHourChange = this.handleEndHourChange.bind(this);
        this.handleEndMinuteChange = this.handleEndMinuteChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidMount() {
        // console.log('moment', moment('2022-04-20 09:00 pm', 'YYYY-MM-DD h:mm a').format('YYYY-MM-DD HH:mm:ss'));

        let { roomId } = this.state;
        console.log('roomId', roomId);
        HospitalRoomService.findRoomById(roomId)
            .then(resp => {
                let { roomImage, roomName } = resp.data;

                console.log('findRoomById', resp);
                this.setState({
                    image: buildRoomImageURL(roomImage),
                    roomName: roomName
                });
            });
    }

    handleStartDateChange(event) {
        console.log('handleStartDateChange', event.target.value);
    }

    handleEndDateChange(event) {
        console.log('handleEndDateChange', event.target.value);
    }

    handleStartHourChange(event) {
        console.log('handleStartHourChange', event.target.value);
    }

    handleStartMinuteChange(event) {
        console.log('handleStartMinuteChange', event.target.value);
    }

    handleEndHourChange(event) {
        console.log('handleEndHourChange', event.target.value);
    }

    handleEndMinuteChange(event) {
        console.log('handleEndMinuteChange', event.target.value);
    }

    onSubmit(values) {
        console.log('values on submit', values);
    }

    render() {
        let roomReservationSchema = getYupValidation('roomReservation');
        let { image, roomName, hasAssociated, associatedId, startDate, endDate,
            startHour,
            startMinute,
            startTimePeriod,
            endHour,
            endMinute,
            endTimePeriod } = this.state;
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow container">
                <HospitalHeader label="Room Reservation" />

                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>

                <hr />

                <Formik
                    initialValues={{ startDate, endDate, startHour, startMinute, startTimePeriod, endHour, 
                        endMinute, endTimePeriod, hasAssociated, associatedId }}
                    onSubmit={this.onSubmit}
                    validationSchema={roomReservationSchema}
                    validateOnBlur={false}
                    validateOnChange={false}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>
                                <div className="mb-3 row">
                                    <div className="col text-center">
                                        <div>Has Associated Appointment?</div>
                                        <div className="form-check form-check-inline">
                                            <Field className="form-check-input" type="radio" name="hasAssociated" value="true" />
                                            <label className="form-check-label" for="inlineRadio1">Yes</label>
                                        </div>
                                        <div className="form-check form-check-inline">
                                            <Field className="form-check-input" type="radio" name="hasAssociated" value="false"/>
                                            <label className="form-check-label" for="inlineRadio2">No</label>
                                        </div>
                                    </div>
                                </div>

                                <div className="mb-3  row">
                                    <div className="col text-end">
                                        <label className="col-form-label">Associated Appoinment ID: </label>
                                    </div>
                                    <div className="col">
                                        <Field className="w-25 col-4 form-control" type="text" name="associatedId" />
                                        <ErrorMessage name="associatedId" component="div" className="text-red" />
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label">Start Date:</label>
                                    <div className="col-sm-3">
                                        <Field type="date" className="form-control" name="startDate" />
                                        <ErrorMessage name="startDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="startHour" >
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                        return <option key={option} value={option}>{option}</option>
                                                    })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="startMinute" >
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-2">
                                        <Field as="select" className="form-select mb-3" name="startTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label">End Date:</label>
                                    <div className="col-sm-3">
                                        <Field type="date" className="form-control" name="endDate" />
                                        <ErrorMessage name="endDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="endHour" >
                                            <option selected disabled>HH</option>
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="endMinute" >
                                            <option selected disabled>MM</option>
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-2">
                                        <Field as="select" className="form-select mb-3" name="endTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <section className="pb-2 text-center">
                                    <button className="btn btn-primary me-2">Back</button>
                                    <button className="btn btn-primary me-2">Check Room Reservations</button>
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

export default CreateReservation;
