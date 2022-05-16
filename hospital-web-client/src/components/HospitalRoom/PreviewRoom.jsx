import './styles/main.css';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import defaultRoomImage from './room-default.png';
import { buildRoomImageURL } from '../../utils/Utils';

class PreviewRoom extends Component {
    constructor(props) {
        super(props);
        let { roomId, roomCode, roomName, description, action, image, imgHash } = props.location.state;
        this.state = {
            roomId: roomId,
            action: action,
            roomCode: roomCode,
            roomName: roomName,
            description: description,
            image: defaultRoomImage,
            file: image,
            imgHash: imgHash
        }

        this.back = this.back.bind(this);
    }

    componentDidMount() {
        let { state } = this.props.location;
        if (state.image) {
            let reader = new FileReader();
            reader.readAsDataURL(this.props.location.state.image);

            reader.onload = () => {
                this.setState({
                    image: reader.result
                });
            };
            reader.onerror = (error) => {
                console.log('Error: ', error);
            };
        }

        console.log('location state', state);
        console.log('state', this.state.action);
        if(this.state.action === 'edit' && this.state.image === defaultRoomImage) {
            console.log('edit mode');
            this.setState({
                image: buildRoomImageURL(state.imgHash)
            });
        }

    }

    back() {
        // return the data as a file object rather than a base64 string

        let { roomId, roomCode, roomName, description, action, file, imgHash } = this.state
        const data = {
            roomId: roomId,
            roomCode: roomCode,
            roomName: roomName,
            description: description,
            image: file,
            imgHash: imgHash
        }

        if(action === 'edit' && roomId) {
            this.props.navigate(`/hospital_rooms/edit/${roomId}`, { state: data });
        }else {
            this.props.navigate('/hospital_rooms/add', { state: data });
        }

    }

    render() {
        console.log('preview room state', this.props.location.state);
        let { roomCode, roomName, description, image } = this.state;

        return <>
            <div className="preview-room-container mt-3 m-auto rounded shadow">
                <HospitalHeader label="Room Preview" />


                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />
                <hr />

                <h3 className="text-center">{roomName}</h3>

                <section className="text-center m-2">
                    <label className="lead me-2 fs-4">Room Code:</label>
                    <label className="text-muted fs-5">{roomCode}</label>
                </section>

                <section className="text-center m-2">
                    <label className="lead me-2 fs-4">Description:</label>
                    <label className="text-muted fs-5">{description}</label>
                </section>

                <section className="pb-2 text-center">
                    <button onClick={this.back} className="btn btn-primary">Back</button>
                </section>

            </div>
        </>;
    }
}

export default PreviewRoom;
