import './styles/main.css';
import { Component } from 'react';
import HospitalRoomService from '../../services/HospitalRoomService';
import HospitalHeader from '../HospitalHeader';
import roomDefaultImg from './room-default.png';
import { buildRoomImageURL } from '../../utils/Utils';

class RoomDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomCode: '',
            roomName: '',
            description: '',
            image: roomDefaultImg
        }

        this.reserveRoom = this.reserveRoom.bind(this);
        this.editRoom = this.editRoom.bind(this);
        this.backToRoomList = this.backToRoomList.bind(this);
    }

    componentDidMount() {
        let { id } = this.props.params;
        HospitalRoomService.findRoomById(id)
            .then(resp => {
                console.log('resp findRoomById', resp);
                let { roomCode, roomName, roomImage, description } = resp.data;
                this.setState({
                    roomCode: roomCode,
                    roomName: roomName,
                    image: roomImage ? buildRoomImageURL(roomImage) : roomDefaultImg,
                    description: description
                });
            })
    }

    reserveRoom() {
        let { id } = this.props.params;
        this.props.navigate(`/reservations/create/${id}`);
    }

    editRoom() {
        let { id } = this.props.params;
        this.props.navigate(`/hospital_rooms/edit/${id}`);
    }

    backToRoomList() {
        this.props.navigate('/hospital_rooms');
    }

    render() {
        let { roomName, roomCode, description, image } = this.state;
        return <>
            <div className="room-details-container mt-3 rounded shadow">
                <HospitalHeader label="Room Details" />

                <section>
                    <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room-image" />
                </section>

                <hr />

                <main className="pb-3 text-center">
                    <h3>{roomName}</h3>

                    <section className="button-section p-2 text-center">
                        <button onClick={this.reserveRoom} className="m-2 btn btn-primary">Reserve Room</button>
                        <button  onClick={this.editRoom} className="m-2 btn btn-primary">Edit</button>
                    </section>

                    <section className="m-2">
                        <label className="lead me-2 fs-4">Room Code:</label>
                        <label className="text-muted fs-5">{roomCode}</label>
                    </section>

                    <section className="m-2">
                        <label className="lead me-2 fs-4">Description:</label>
                        <label className="text-muted fs-5">{description}</label>
                    </section>

                    <section className="button-section p-2 text-center">
                        <button onClick={this.backToRoomList} className="btn btn-primary">Back</button>
                    </section>
                </main>
            </div>
        </>;
    }
}

export default RoomDetails;
