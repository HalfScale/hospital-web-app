import { Component } from 'react';
import { RESERVATION_STATUS_CODE } from '../../constants/GlobalConstants';
import AuthService from '../../services/AuthService';

class ReservationTableRow extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let currentUserId = AuthService.getUserId();
        let currentUserRole = AuthService.getUserRole();

        let status = "CREATED";
        let { viewReservation, editReservation, data } = this.props
        let { id, hospitalRoomResponse: { roomCode }, hospitalRoomResponse: { roomName },
            startDate, endDate, reservationStatus, reservedByUsername, reservedById } = data;

        Object.keys(RESERVATION_STATUS_CODE).some(key => {
            if (RESERVATION_STATUS_CODE[key] == reservationStatus) {
                status = key;
                return true;
            }
            return false;
        });

        return <tr>
            <th scope="row">{roomCode}</th>
            <td>{roomName}</td>
            <td>{reservedByUsername}</td>
            <td>{startDate}</td>
            <td>{endDate}</td>
            <td className="text-center">{status}</td>
            <td className="text-center">
                <div className="room-row-options">
                    <button onClick={e => viewReservation(id)} className="m-1 btn btn-info">View</button>
                    {
                        currentUserId === reservedById && <button onClick={e => editReservation(id)} className="m-1 btn btn-warning">Update</button>
                    }

                    {
                        currentUserId === reservedById &&
                        RESERVATION_STATUS_CODE.CREATED != reservationStatus &&
                        <button className="m-1 btn btn-danger">Delete</button>
                    }

                </div>
            </td>
        </tr>;
    }
}

export default ReservationTableRow;
