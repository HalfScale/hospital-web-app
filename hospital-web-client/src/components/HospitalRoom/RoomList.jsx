import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import ReactPaginate from 'react-paginate';
import HospitalRoomService from '../../services/HospitalRoomService'
import RoomTableRow from './RoomTableRow';

class RoomList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomCodeFilter: '',
            roomNameFilter: '',
            page: 0,
            size: 8,
            sort: 'id,asc',
            totalPages: 0,
            rooms: [],
            roomIdToDelete: null
        }

        this.handlePageChange = this.handlePageChange.bind(this);
        this.displayRoomTableRows = this.displayRoomTableRows.bind(this);
        this.searchRoom = this.searchRoom.bind(this);
        this.roomCodeFilterOnChange = this.roomCodeFilterOnChange.bind(this);
        this.roomNameFilterOnChange = this.roomNameFilterOnChange.bind(this);
        this.clear = this.clear.bind(this);
        this.viewRoomDetails = this.viewRoomDetails.bind(this);
        this.addRoom = this.addRoom.bind(this);
    }

    componentDidMount() {
        this.fetchHospitalRooms();
    }

    fetchHospitalRooms() {
        let { page, size, sort, roomCodeFilter, roomNameFilter } = this.state;
        HospitalRoomService.findAllRoom({
            page: page,
            size: size,
            sort: sort,
            roomCode: roomCodeFilter,
            roomName: roomNameFilter
        }).then(resp => {
            console.log('resp findAllRoom', resp);
            this.setState({
                rooms: resp.data.content,
                totalPages: resp.data.totalPages
            });
        });
    }

    displayRoomTableRows() {
        let { rooms } = this.state;

        if (rooms.length === 0) {
            return <td colSpan="5" className="text-muted fs-2 text-center">No results</td>
        };

        return rooms.map(room => {
            return <RoomTableRow key={room.id} data={room} viewRoom={this.viewRoomDetails} />
        });
    }

    addRoom() {
        this.props.navigate('/hospital_rooms/add');
    }

    searchRoom(event) {
        event.preventDefault();
        this.fetchHospitalRooms();
    }

    viewRoomDetails(id) {
        this.props.navigate(`/hospital_rooms/details/${id}`);
    }

    roomCodeFilterOnChange(event) {
        this.setState({
            roomCodeFilter: event.target.value
        });
    }

    roomNameFilterOnChange(event) {
        this.setState({
            roomNameFilter: event.target.value
        });
    }

    clear() {
        this.setState({
            roomCodeFilter: '',
            roomNameFilter: ''
        });
    }

    handlePageChange() {

    }

    render() {
        let { rooms } = this.state;

        return <div>
            <HospitalHeader label='Room List' />

            <div>
                <nav className="w-50 mx-auto mt-3 mb-4 navbar navbar-light rounded shadow">
                    <div className="container-fluid">
                        <a className="navbar-brand text-muted">Search Doctor</a>
                        <form onSubmit={this.searchRoom} className="d-flex">
                            <input onChange={this.roomCodeFilterOnChange} className="form-control me-2" type="search" placeholder="Room Code" aria-label="Search" />
                            <input onChange={this.roomNameFilterOnChange} className="form-control me-2" type="search" placeholder="Room Name" aria-label="Search" />
                            <button className="btn btn-outline-success me-2" type="submit">Search</button>
                            <button className="btn btn-outline-success" onClick={this.clear} type="reset">Clear</button>
                        </form>
                    </div>
                </nav>
            </div>

            <button onClick={this.addRoom} className="m-3 btn btn-primary">Create</button>

            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">Room Code</th>
                        <th scope="col">Room Name</th>
                        <th scope="col">Created By</th>
                        <th scope="col">Updated By</th>
                        <th className="text-center" scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.displayRoomTableRows()
                    }
                </tbody>
            </table>

            {
                rooms.length !== 0 && <ReactPaginate
                    className="pagination justify-content-center"
                    nextLabel="next >"
                    onPageChange={this.handlePageChange}
                    pageRangeDisplayed={3}
                    marginPagesDisplayed={2}
                    pageCount={this.state.totalPages}
                    previousLabel="< prev"
                    pageClassName="page-item"
                    pageLinkClassName="page-link"
                    previousClassName="page-item"
                    previousLinkClassName="page-link"
                    nextClassName="page-item"
                    nextLinkClassName="page-link"
                    breakLabel="..."
                    breakClassName="page-item"
                    breakLinkClassName="page-link"
                    containerClassName="pagination"
                    activeClassName="active"
                    renderOnZeroPageCount={null}
                />
            }

        </div>;
    }
}

export default RoomList;
