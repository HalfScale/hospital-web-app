import { Component } from 'react';

class RoomTableRow extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let { data, viewRoom } = this.props;
        let { id, roomCode, roomName, createdBy, updatedBy } = data;

        return <tr>
            <th scope="row">{roomCode}</th>
            <td>{roomName}</td>
            <td>{createdBy}</td>
            <td>{updatedBy}</td>
            <td className="text-center">
                <button onClick={e => viewRoom(id)} className="m-1 btn btn-info">View</button>
                <button className="m-1 btn btn-warning">Update</button>
                <button className="m-1 btn btn-danger">Delete</button>
            </td>
        </tr>;
    }
}

export default RoomTableRow;
