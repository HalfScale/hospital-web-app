import './styles/main.css';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import ReactPaginate from 'react-paginate';
import HospitalRoomService from '../../services/HospitalRoomService'
import RoomTableRow from './RoomTableRow';
import { Modal, Button } from 'react-bootstrap'
import { ToastContainer, toast } from 'react-toastify';

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
            roomIdToDelete: null,
            showModal: false
        }

        this.handlePageChange = this.handlePageChange.bind(this);
        this.displayRoomTableRows = this.displayRoomTableRows.bind(this);
        this.searchRoom = this.searchRoom.bind(this);
        this.roomCodeFilterOnChange = this.roomCodeFilterOnChange.bind(this);
        this.roomNameFilterOnChange = this.roomNameFilterOnChange.bind(this);
        this.clear = this.clear.bind(this);
        this.viewRoomDetails = this.viewRoomDetails.bind(this);
        this.addRoom = this.addRoom.bind(this);
        this.editRoom = this.editRoom.bind(this);
        this.closeDeleteModal = this.closeDeleteModal.bind(this);
        this.deleteRoom = this.deleteRoom.bind(this);
        this.showModal = this.showModal.bind(this);
        this.toggleRoomToDelete = this.toggleRoomToDelete.bind(this);
    }

    componentDidMount() {
        this.fetchHospitalRooms();

        if (this.props.location.state) {
            let { message, showToast } = this.props.location.state;

            if (showToast) {
                setTimeout(() => {
                    toast.success(message);
                }, 500);
            }

        }
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
            console.log('fetch rooms', resp);
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
            return <RoomTableRow key={room.id} data={room} viewRoom={this.viewRoomDetails}
                editRoom={this.editRoom}
                showModal={this.showModal}
                toggleRoom={this.toggleRoomToDelete}
            />
        });
    }

    addRoom() {
        this.props.navigate('/hospital_rooms/add');
    }

    searchRoom(event) {
        event.preventDefault();
        this.setState({ page: 0 }, () => this.fetchHospitalRooms());
    }

    viewRoomDetails(id) {
        this.props.navigate(`/hospital_rooms/details/${id}`);
    }

    editRoom(id) {
        this.props.navigate(`/hospital_rooms/edit/${id}`);
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

    handlePageChange(page) {
        this.setState({
            page: page.selected
        }, () => this.fetchHospitalRooms());
    }

    closeDeleteModal() {
        this.setState({ showModal: false });
    }

    showModal() {
        this.setState({ showModal: true });
    }

    toggleRoomToDelete(id) {
        this.setState({ roomIdToDelete: id });
    }

    deleteRoom() {
        HospitalRoomService.deleteRoom(this.state.roomIdToDelete).then(resp => {
            this.closeDeleteModal();
            this.fetchHospitalRooms();
            toast.success('Room successfully deleted!');
        });
    }

    render() {
        let { rooms, showModal } = this.state;

        return <div>
            <HospitalHeader label='Room List' />

            <div>
                <nav className="room-list-search mt-3 mb-4 navbar navbar-light rounded shadow">
                    <div className="container-fluid">
                        <a className="navbar-brand text-muted">Search Doctor</a>
                        <form onSubmit={this.searchRoom} className="room-list-search-form">
                            <input onChange={this.roomCodeFilterOnChange} className="form-control me-2" type="search" placeholder="Room Code" aria-label="Search" />
                            <input onChange={this.roomNameFilterOnChange} className="form-control me-2" type="search" placeholder="Room Name" aria-label="Search" />
                            <button className="search-btn btn btn-outline-success me-2" type="submit">Search</button>
                            <button className="btn btn-outline-success" onClick={this.clear} type="reset">Clear</button>
                        </form>
                    </div>
                </nav>
            </div>

            <button onClick={this.addRoom} className="m-3 btn btn-primary">Create</button>

            <div className="table-wrapper">
                <table className="table table-bordered">
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
            </div>

            <Modal
                show={showModal}
                onHide={this.closeDeleteModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>Delete Room</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    Are you sure you want to delete room?
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeDeleteModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={this.deleteRoom}>
                        Delete
                    </Button>
                </Modal.Footer>

            </Modal>

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

            <ToastContainer
                position="bottom-center"
                autoClose={3000}
                hideProgressBar={true}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnHover={false}
                draggable={false}
                theme="colored" />

        </div>;
    }
}

export default RoomList;
