import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import HospitalRoomService from '../../services/HospitalRoomService';
import defaultRoomImg from '../HospitalRoom/room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';
import { ErrorMessage, Field, Form, Formik } from 'formik';
import moment from 'moment';
import getYupValidation from '../../utils/YupValidationFactory';
import { Modal, Button } from 'react-bootstrap'
import RoomReservationService from '../../services/RoomReservationService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import OverlappingReservationTableRow from './OverlappingReservationTableRow';

class CreateReservation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
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
            associatedId: '',
            overlappingReservations: [],
            page: 0,
            size: 5,
            sort: 'id,desc'
        }

        this.handleStartDateChange = this.handleStartDateChange.bind(this);
        this.handleEndDateChange = this.handleEndDateChange.bind(this);
        this.handleStartHourChange = this.handleStartHourChange.bind(this);
        this.handleStartMinuteChange = this.handleStartMinuteChange.bind(this);
        this.handleStartTimePeriodChange = this.handleStartTimePeriodChange.bind(this);
        this.handleEndHourChange = this.handleEndHourChange.bind(this);
        this.handleEndMinuteChange = this.handleEndMinuteChange.bind(this);
        this.handleEndTimePeriodChange = this.handleEndTimePeriodChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.showReservationModal = this.showReservationModal.bind(this);
        this.showReservationModal = this.showReservationModal.bind(this);
        this.displayReservationRows = this.displayReservationRows.bind(this);
    }

    componentDidMount() {
        console.log('moment', moment.now());

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
        this.setState({ startDate: event.target.value });
    }

    handleEndDateChange(event) {
        console.log('handleEndDateChange', event.target.value);
        this.setState({ endDate: event.target.value });
    }

    handleStartHourChange(event) {
        console.log('handleStartHourChange', event.target.value);
        this.setState({ startHour: event.target.value });
    }

    handleStartMinuteChange(event) {
        console.log('handleStartMinuteChange', event.target.value);
        this.setState({ startMinute: event.target.value });
    }

    handleStartTimePeriodChange(event) {
        console.log('handleStartTimePeriodChange', event.target.value);
        this.setState({ startTimePeriod: event.target.value });
    }

    handleEndHourChange(event) {
        console.log('handleEndHourChange', event.target.value);
        this.setState({ endHour: event.target.value });
    }

    handleEndMinuteChange(event) {
        console.log('handleEndMinuteChange', event.target.value);
        this.setState({ endMinute: event.target.value });
    }

    handleEndTimePeriodChange(event) {
        console.log('handleEndTimePeriodChange', event.target.value);
        this.setState({ endTimePeriod: event.target.value });
    }

    onSubmit(values) {
        console.log('values on submit', values);
    }

    closeModal() {
        this.setState({ showModal: false });
    }

    displayReservationRows() {
        let { overlappingReservations } = this.state;

        if(overlappingReservations.length !== 0) {
            return overlappingReservations.map(reservation => {
                return <OverlappingReservationTableRow key={reservation.id} data={reservation}/>;
            })
        }

        return <tr>
            <td className="text-muted text-center" colSpan={3}>No result</td>
        </tr>;
    }

    showReservationModal() {
        let { startDate, startHour, startMinute, startTimePeriod,
            endDate, endHour, endMinute, endTimePeriod, page, size, sort } = this.state;

        if (startDate !== '' && endDate !== '') {

            let today = moment().format('YYYY-MM-DD');
            let selectedStartDate = moment(startDate, 'YYYY-MM-DD');
            let selectedEndDate = moment(endDate, 'YYYY-MM-DD');

            if ((selectedStartDate.isSame(today) || selectedStartDate.isAfter(today)) &&
                (selectedEndDate.isSame(today) || selectedEndDate.isAfter(today)) &&
                (selectedStartDate.isSame(selectedEndDate) || selectedStartDate.isBefore(selectedEndDate))) {

                let formattedStartDate = moment(`${startDate} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD h:mm a').format('YYYY-MM-DD HH:mm:ss');
                let formattedEndDate = moment(`${endDate} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD h:mm a').format('YYYY-MM-DD HH:mm:ss');
                this.setState({ showModal: true }, () => {
                    RoomReservationService.findOverlappingReservation({
                        startDate: formattedStartDate,
                        endDate: formattedEndDate
                    }, { page: page, size: size, sort: sort}).then(resp => {
                        console.log('findOverlappingReservation resp', resp);
                        this.setState({ overlappingReservations: resp.data.content});
                    });
                });
            } else {
                toast.warn('Choose a valid date.');
            }

        } else {
            toast.warn('Please choose a date range.');
        }

    }

    render() {
        let roomReservationSchema = getYupValidation('roomReservation');
        let { showModal, image, roomName, hasAssociated, associatedId, startDate, endDate,
            startHour,
            startMinute,
            startTimePeriod,
            endHour,
            endMinute,
            endTimePeriod } = this.state;
        return <>

            <Modal
                show={showModal}
                onHide={this.closeModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>Reservations</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <table className="table">
                        <thead>
                            <tr>
                                <th scope="col">Room Code</th>
                                <th scope="col">Start Date</th>
                                <th scope="col">End Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            { this.displayReservationRows() }
                        </tbody>
                    </table>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeModal}>
                        Close
                    </Button>
                </Modal.Footer>

            </Modal>

            <div className="mt-3 m-auto w-50 rounded shadow container">
                <HospitalHeader label="Room Reservation" />

                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>

                <hr />

                <Formik
                    initialValues={{
                        startDate, endDate, startHour, startMinute, startTimePeriod, endHour,
                        endMinute, endTimePeriod, hasAssociated, associatedId
                    }}
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
                                            <Field className="form-check-input" type="radio" name="hasAssociated" value="false" />
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
                                        <Field onChange={this.handleStartDateChange} type="date" className="form-control" name="startDate" />
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
                                    <div className="col-2">
                                        <Field onChange={this.handleStartTimePeriodChange} as="select" className="form-select mb-3" name="startTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label">End Date:</label>
                                    <div className="col-sm-3">
                                        <Field onChange={this.handleEndDateChange} type="date" className="form-control" name="endDate" />
                                        <ErrorMessage name="endDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleEndHourChange} as="select" className="form-select mb-3" name="endHour" >
                                            <option selected disabled>HH</option>
                                            {
                                                ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field onChange={this.handleEndMinuteChange} as="select" className="form-select mb-3" name="endMinute" >
                                            <option selected disabled>MM</option>
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-2">
                                        <Field onChange={this.handleEndTimePeriodChange} as="select" className="form-select mb-3" name="endTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <section className="pb-2 text-center">
                                    <button type="button" className="btn btn-primary me-2">Back</button>
                                    <button type="button" onClick={this.showReservationModal} className="btn btn-primary me-2">Check Room Reservations</button>
                                    <button type="submit" className="btn btn-primary">Create</button>
                                </section>
                            </Form>
                        )
                    }
                </Formik>

                <ToastContainer
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
            </div>
        </>;
    }
}

export default CreateReservation;
