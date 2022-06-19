import './styles/main.css';
import defaultRoomImg from '../HospitalRoom/room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import RoomReservationService from '../../services/RoomReservationService';
import { RESERVATION_STATUS_CODE } from '../../constants/GlobalConstants'
import AuthService from '../../services/AuthService';
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Modal, Button } from 'react-bootstrap'

class ReservationDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            reservedById: -1,
            reservationId: props.params.id,
            roomImage: defaultRoomImg,
            roomName: '',
            roomCode: '',
            hasAssociated: '',
            associatedId: '',
            reservationStatus: ''
        }

        this.setReservationToCancel = this.setReservationToCancel.bind(this);
        this.setReservationToDone = this.setReservationToDone.bind(this);
        this.back = this.back.bind(this);
        this.editReservation = this.editReservation.bind(this);
        this.deleteReservation = this.deleteReservation.bind(this);
        this.closeDeleteModal = this.closeDeleteModal.bind(this);
        this.showDeleteModal = this.showDeleteModal.bind(this);
    }

    componentDidMount() {
        let { reservationId } = this.state;
        RoomReservationService.findById(reservationId)
            .then(resp => {

                let { hospitalRoomResponse: { roomName, roomImage, roomCode },
                    startDate, endDate, hasAssociatedAppointmentId, associatedAppointmentId,
                    reservationStatus, reservedById } = resp.data;

                this.setState({
                    reservedById: reservedById,
                    roomImage: roomImage ? buildRoomImageURL(roomImage) : defaultRoomImg,
                    roomName: roomName,
                    roomCode: roomCode,
                    hasAssociated: hasAssociatedAppointmentId ? 'true' : 'false',
                    associatedId: associatedAppointmentId,
                    startDate: startDate,
                    endDate: endDate,
                    reservationStatus: reservationStatus
                });
            });
    }

    setReservationToCancel() {
        RoomReservationService.setStatusToCancelled(this.state.reservationId)
            .then(resp => {
                window.location.pathname = `/reservations/details/${this.state.reservationId}`
            }).catch(error => {
                this.props.navigate('/reservations');
            });
    }

    setReservationToDone() {
        RoomReservationService.setStatusToDone(this.state.reservationId)
            .then(resp => {
                window.location.pathname = `/reservations/details/${this.state.reservationId}`
            }).catch(error => {
                this.props.navigate('/reservations');
            });
    }

    showDeleteModal() {
        this.setState({
            showModal: true
        });
    }

    closeDeleteModal() {
        this.setState({
            showModal: false
        });
    }

    editReservation() {
        this.props.navigate(`/reservations/edit/${this.state.reservationId}`);
    }

    deleteReservation() {
        RoomReservationService.deleteById(this.state.reservationId)
            .then(resp => {
                this.props.navigate('/reservations', { state: 'Successfully deleted reservation!' });
            });
    }

    back() {
        this.props.navigate('/reservations');
    }

    render() {
        let { showModal, roomImage, roomName, roomCode, startDate, endDate,
            hasAssociated, associatedId, reservationStatus, reservedById } = this.state;


        return <>
            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Reservation Details" />

                <img src={roomImage} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>


                <section className="button-section p-2 text-center">
                    {
                        RESERVATION_STATUS_CODE.CREATED != reservationStatus && reservedById === AuthService.getUserId() &&
                        <FontAwesomeIcon onClick={this.showDeleteModal} icon={faTrash} size="2xl" className="pointer ms-3" />
                    }

                    {
                        RESERVATION_STATUS_CODE.CREATED == reservationStatus && reservedById === AuthService.getUserId() && <>
                            <button onClick={this.setReservationToCancel} type="button" className="btn btn-primary me-2">Cancel</button>
                            <button onClick={this.setReservationToDone} type="button" className="btn btn-primary me-2">Done</button>
                        </>
                    }

                </section>

                <hr />

                <div class="container text-center">

                    <div class="row">
                        <div class="col lead me-2 fs-5">
                            Room Code
                        </div>
                        <div class="col text-muted fs-6">
                            {roomCode}
                        </div>
                    </div>

                    <hr />

                    <div class="row">
                        <div class="col lead me-2 fs-5">
                            Reservation Status
                        </div>
                        <div class="col text-muted fs-6">
                            {
                                Object.keys(RESERVATION_STATUS_CODE).find(key => {
                                    if (RESERVATION_STATUS_CODE[key] == reservationStatus) {
                                        return true;
                                    }
                                    return false
                                })
                            }
                        </div>
                    </div>

                    <hr />

                    <div class="row">
                        <div class="col lead me-2 fs-5">
                            Has Associated Appointment Id?
                        </div>
                        <div class="col text-muted fs-6">
                            {hasAssociated}
                        </div>
                    </div>

                    {
                        hasAssociated === 'true' && <>
                            <hr />
                            <div class="row">

                                <div class="col lead me-2 fs-5">
                                    Associated Appointment Id
                                </div>
                                <div class="col text-muted fs-6">
                                    {associatedId}
                                </div>
                            </div>
                        </>
                    }

                    <hr />

                    <div class="row">
                        <div class="col lead me-2 fs-5">
                            Start Date
                        </div>
                        <div class="col text-muted fs-6">
                            {startDate}
                        </div>
                    </div>

                    <hr />

                    <div class="row">
                        <div class="col lead me-2 fs-5">
                            End Date
                        </div>
                        <div class="col text-muted fs-6">
                            {endDate}
                        </div>
                    </div>

                    <section className="button-section p-2 text-center">
                        {
                            RESERVATION_STATUS_CODE.CREATED == reservationStatus && reservedById === AuthService.getUserId() &&
                            <button onClick={this.editReservation} type="submit" className="btn btn-primary">Edit</button>
                        }
                        <button onClick={this.back} type="button" className="btn btn-primary me-2">Back</button>
                    </section>

                </div>
            </div>

            <Modal
                show={showModal}
                onHide={this.closeDeleteModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>Delete Reservation</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    Are you sure you want to delete this reservation?
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeDeleteModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={this.deleteReservation}>
                        Delete
                    </Button>
                </Modal.Footer>

            </Modal>
        </>;
    }
}

export default ReservationDetails;
