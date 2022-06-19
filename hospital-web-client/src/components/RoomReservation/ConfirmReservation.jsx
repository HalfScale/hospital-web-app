import './styles/confirm.reservation.css';
import { Component } from 'react';
import moment from 'moment';
import HospitalHeader from '../HospitalHeader';
import RoomReservationService from '../../services/RoomReservationService';

class ConfirmReservation extends Component {
    constructor(props) {
        super(props);
        let { roomName, roomImage, hasAssociatedAppointmentId,
            associatedAppointmentId,
            startDate, endDate } = props.location.state;

        this.state = {
            roomName: roomName,
            roomImage: roomImage,
            hasAssociatedId: hasAssociatedAppointmentId,
            associatedId: associatedAppointmentId,
            startDate: moment(startDate, 'YYYY-MM-DD HH:mm:ss').format('MMM DD YYYY, hh:mm a'),
            endDate: moment(endDate, 'YYYY-MM-DD HH:mm:ss').format('MMM DD YYYY, hh:mm a'),
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.back = this.back.bind(this);
    }

    onSubmit() {
        let { hospitalRoomId, associatedAppointmentId, hasAssociatedAppointmentId,
            startDate,
            endDate } = this.props.location.state;

        let data = {
            hospitalRoomId: hospitalRoomId,
            associatedAppointmentId: associatedAppointmentId,
            hasAssociatedAppointmentId: hasAssociatedAppointmentId,
            startDate: startDate,
            endDate: endDate
        }

        RoomReservationService.create(data).then(resp => {
            this.props.navigate('/reservations', { state: {
                message: 'Reservation created successfully!',
                type: 'success'
            }});
        });
    }

    back() {
        let { state } = this.props.location;
        let hospitalRoom = state.hospitalRoomId;
        this.props.navigate(`/reservations/create/${hospitalRoom}`, { state: state });
    }

    render() {
        let { roomName, roomImage, hasAssociatedId, associatedId, startDate, endDate } = this.state;
        return <>
            <div className="mt-3 confirm-reservation-container rounded shadow">
                <HospitalHeader label="Confirm Reservation" />

                <img src={roomImage} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>

                <hr />

                <div class="container text-center">
                    <div class="row">
                        <div class="col lead me-2 fs-4">
                            Has Associated Appointment Id?
                        </div>
                        <div class="col text-muted fs-5">
                            {hasAssociatedId}
                        </div>
                    </div>
                    <hr />
                    {
                        hasAssociatedId === 'true' && <div class="row">
                            <div class="col lead me-2 fs-4">
                                Associated Id
                            </div>
                            <div class="col text-muted fs-5">
                                {associatedId}
                            </div>
                        </div>
                    }

                    <div class="row">
                        <div class="col lead me-2 fs-4">
                            Start Date
                        </div>
                        <div class="col text-muted fs-5">
                            {startDate}
                        </div>
                    </div>

                    <hr />

                    <div class="row">
                        <div class="col lead me-2 fs-4">
                            End Date
                        </div>
                        <div class="col text-muted fs-5">
                            {endDate}
                        </div>
                    </div>

                    <section className="button-section p-2 text-center">
                        <button onClick={this.onSubmit} type="submit" className="btn btn-primary">Confirm</button>
                        <button onClick={this.back} type="button" className="btn btn-primary me-2">Back</button>
                    </section>

                </div>
            </div>
        </>;
    }
}

export default ConfirmReservation;
