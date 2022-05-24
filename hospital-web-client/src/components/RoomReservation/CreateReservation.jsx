import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import HospitalRoomService from '../../services/HospitalRoomService';
import defaultRoomImg from '../HospitalRoom/room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';

class CreateReservation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomId: this.props.params.roomId,
            image: defaultRoomImg,
            roomName: ''
        }
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

    render() {
        let { image, roomName } = this.state;
        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Room Reservation" />

                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <h3 className="text-center mt-2">{roomName}</h3>

                <div class="mb-3 row">
                    <div className="col">
                        <div>Has Associated Appoinment?</div>
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

                <div class="mb-3 row">
                    <label className="col-sm-4 col-form-label">Associated Appoinment ID: </label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" id="inputPassword" />
                    </div>
                </div>

                <div class="mb-3 row">
                    <label className="col-sm-4 col-form-label">Start Date:</label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" id="inputPassword" />
                    </div>
                </div>

                <div class="mb-3 row">
                    <label className="col-sm-4 col-form-label">End Date:</label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" id="inputPassword" />
                    </div>
                </div>

                <section className="pb-2 text-center">
                    <button className="btn btn-primary me-2">Back</button>
                    <button className="btn btn-primary">Create</button>
                </section>

            </div>
        </>;
    }
}

export default CreateReservation;
