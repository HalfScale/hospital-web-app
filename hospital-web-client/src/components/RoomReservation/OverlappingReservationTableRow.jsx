import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class OverlappingReservationTableRow extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let { hospitalRoomResponse: { roomCode }, startDate, endDate } = this.props.data;
        return <tr>
            <td>{startDate}</td>
            <td>{endDate}</td>
        </tr>;
    }
}

export default OverlappingReservationTableRow;
