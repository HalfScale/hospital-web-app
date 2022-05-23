import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import RoomReservationService from '../../services/RoomReservationService';

class ReservationDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.params.id
        }
    }

    componentDidMount() {
        let { id } = this.state;
        RoomReservationService.findById(id)
        .then(resp => {
            console.log('by id =>', resp);
        });
    }

    render() {
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Reservation Details" />
            </div>
        </>;
    }
}

export default ReservationDetails;
