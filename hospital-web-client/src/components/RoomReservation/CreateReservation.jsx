import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import HospitalRoomService from '../../services/HospitalRoomService';
import defaultRoomImg from '../HospitalRoom/room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';
import DatePicker from 'react-date-picker'
import { toHaveStyle } from '@testing-library/jest-dom/dist/matchers';
import { ErrorMessage, Field, Form, Formik } from 'formik';

class CreateReservation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomId: this.props.params.roomId,
            image: defaultRoomImg,
            roomName: '',
            startDate: ''
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

    onSubmit() {

    }

    render() {
        let { image, roomName, startDate } = this.state;
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow container">
                <HospitalHeader label="Room Reservation" />

                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>

                <hr />

                <Formik
                    initialValues={{ startDate }}
                    onSubmit={this.onSubmit}
                    // validationSchema={SignUpSchema}
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
                                            <input className="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" />
                                            <label className="form-check-label" for="inlineRadio1">Yes</label>
                                        </div>
                                        <div className="form-check form-check-inline">
                                            <input className="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2" />
                                            <label className="form-check-label" for="inlineRadio2">No</label>
                                        </div>
                                    </div>
                                </div>

                                <div className="mb-3  row">
                                    <div className="col text-end">
                                        <label className="col-form-label">Associated Appoinment ID: </label>
                                    </div>
                                    <div className="col">
                                        <input type="text" className="w-25 col-4 form-control" />
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="text-center col-sm-4 col-form-label">Start Date:</label>
                                    <div className="col-sm-3">
                                        <input onChange={this.handleStartDateChange} type="date" className="form-control" />
                                    </div>
                                    <div className="col-sm-2">
                                        <select onChange={this.handleStartHourChange} className="form-select mb-3" aria-label=".form-select-lg example">
                                            <option selected disabled>HH</option>
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value="option">{option}</option>
                                                })
                                            }
                                        </select>
                                    </div>
                                    <div className="col-sm-2">
                                        <select onChange={this.handleStartMinuteChange} className="form-select mb-3" aria-label=".form-select-lg example">
                                            <option selected disabled>MM</option>
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="text-center col-sm-4 col-form-label">End Date:</label>
                                    <div className="col-sm-3">
                                        <input onChange={this.handleEndDateChange} type="date" className="form-control" />
                                    </div>
                                    <div className="col-sm-2">
                                        <select onChange={this.handleEndHourChange} className="form-select mb-3" aria-label=".form-select-lg example">
                                            <option selected disabled>HH</option>
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value="option">{option}</option>
                                                })
                                            }
                                        </select>
                                    </div>
                                    <div className="col-sm-2">
                                        <select onChange={this.handleEndMinuteChange} className="form-select mb-3" aria-label=".form-select-lg example">
                                            <option selected disabled>MM</option>
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </select>
                                    </div>
                                </div>

                                <section className="pb-2 text-center">
                                    <button className="btn btn-primary me-2">Back</button>
                                    <button className="btn btn-primary">Create</button>
                                </section>
                            </Form>
                        )
                    }
                </Formik>
{/* 
                <div className="mb-3 row">
                    <div className="col text-center">
                        <div>Has Associated Appointment?</div>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" />
                            <label className="form-check-label" for="inlineRadio1">Yes</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2" />
                            <label className="form-check-label" for="inlineRadio2">No</label>
                        </div>
                    </div>
                </div>

                <div className="mb-3  row">
                    <div className="col text-end">
                        <label className="col-form-label">Associated Appoinment ID: </label>
                    </div>
                    <div className="col">
                        <input type="text" className="w-25 col-4 form-control" />
                    </div>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-4 col-form-label">Start Date:</label>
                    <div className="col-sm-3">
                        <input onChange={this.handleStartDateChange} type="date" className="form-control" />
                    </div>
                    <div className="col-sm-2">
                        <select onChange={this.handleStartHourChange} className="form-select mb-3" aria-label=".form-select-lg example">
                            <option selected disabled>HH</option>
                            {
                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                    return <option value="option">{option}</option>
                                })
                            }
                        </select>
                    </div>
                    <div className="col-sm-2">
                        <select onChange={this.handleStartMinuteChange} className="form-select mb-3" aria-label=".form-select-lg example">
                            <option selected disabled>MM</option>
                            <option value="00">00</option>
                            <option value="30">30</option>
                        </select>
                    </div>
                </div>

                <div className="mb-3 row">
                    <label className="col-sm-4 col-form-label">End Date:</label>
                    <div className="col-sm-3">
                        <input onChange={this.handleEndDateChange} type="date" className="form-control" />
                    </div>
                    <div className="col-sm-2">
                        <select onChange={this.handleEndHourChange} className="form-select mb-3" aria-label=".form-select-lg example">
                            <option selected disabled>HH</option>
                            {
                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                    return <option value="option">{option}</option>
                                })
                            }
                        </select>
                    </div>
                    <div className="col-sm-2">
                        <select onChange={this.handleEndMinuteChange} className="form-select mb-3" aria-label=".form-select-lg example">
                            <option selected disabled>MM</option>
                            <option value="00">00</option>
                            <option value="30">30</option>
                        </select>
                    </div>
                </div>

                <section className="pb-2 text-center">
                    <button className="btn btn-primary me-2">Back</button>
                    <button className="btn btn-primary">Create</button>
                </section> */}

            </div>
        </>;
    }
}

export default CreateReservation;
