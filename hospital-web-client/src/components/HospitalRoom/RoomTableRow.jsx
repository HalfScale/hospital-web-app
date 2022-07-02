import { Component } from 'react';

class RoomTableRow extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let { data, viewRoom, editRoom, showModal, toggleRoom } = this.props;
        let { id, roomCode, roomName, createdBy, updatedBy } = data;

        return <tr>
            <th scope="row">{roomCode}</th>
            <td>{roomName}</td>
            <td>{createdBy}</td>
            <td>{updatedBy}</td>
            <td className="text-center">
                <div className="room-row-options">
                    <button onClick={e => viewRoom(id)} className="m-1 btn btn-info">View</button>
                    <button onClick={e => editRoom(id)} className="m-1 btn btn-warning">Update</button>
                    <button onClick={e => {
                        showModal();
                        toggleRoom(id);
                    }} className="m-1 btn btn-danger">Delete</button>
                </div>
            </td>
        </tr>;
    }
}

export default RoomTableRow;
