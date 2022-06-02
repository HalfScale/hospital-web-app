import { Component } from 'react';
import { RESERVATION_STATUS_CODE } from '../../constants/GlobalConstants';
import HospitalHeader from '../HospitalHeader';
import RoomReservationService from '../../services/RoomReservationService';
import ReservationTableRow from './ReservationTableRow';
import ReactPaginate from 'react-paginate';
import { ToastContainer, toast } from 'react-toastify';
import './styles/main.css';

class ReservationList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomCodeFilter: '',
            roomNameFilter: '',
            statusFilter: '3',
            page: 0,
            size: 8,
            sort: 'id,desc',
            totalPages: 0,
            reservations: [],
            reservationToDelete: null,
            showModal: false
        }

        this.displayReservationTableRows = this.displayReservationTableRows.bind(this);
        this.viewAppointment = this.viewAppointment.bind(this);
        this.updateAppointment = this.updateAppointment.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.searchReservation = this.searchReservation.bind(this);
        this.roomCodeFilterOnChange = this.roomCodeFilterOnChange.bind(this);
        this.roomNameFilterOnChange = this.roomNameFilterOnChange.bind(this);
        this.statusFilterOnChange = this.statusFilterOnChange.bind(this);
        this.clearSearch = this.clearSearch.bind(this);
        this.fetchRoomReservations = this.fetchRoomReservations.bind(this);
    }

    componentDidMount() {
        this.fetchRoomReservations();
        if (this.props.location.state) {
            console.log('state', this.props.location.state);

            setTimeout(() => {
                toast.success(this.props.location.state);
            }, 100);

        }
    }

    displayReservationTableRows() {
        let { reservations } = this.state;

        return reservations.map(reservation => {

            return <ReservationTableRow key={reservation.id}
                viewReservation={this.viewAppointment}
                editReservation={this.updateAppointment}
                data={reservation}
            />
        });
    }

    fetchRoomReservations() {
        let { roomCodeFilter, roomNameFilter, statusFilter, page, size, sort } = this.state;
        RoomReservationService.findAll({
            page: page,
            size: size,
            sort: sort,
            roomCode: roomCodeFilter,
            roomName: roomNameFilter,
            status: statusFilter
        }).then(resp => {
            console.log('reservation findAll', resp);
            this.setState({
                reservations: resp.data.content,
                totalPages: resp.data.totalPages
            });
        });
    }

    searchReservation(event) {
        event.preventDefault();
        this.setState({ page: 0 }, () => this.fetchRoomReservations());
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

    statusFilterOnChange(event) {
        this.setState({
            statusFilter: event.target.value
        });
    }

    clearSearch() {
        this.setState({
            roomCodeFilter: '',
            roomNameFilter: '',
            statusFilter: '3',
        });
    }

    viewAppointment(id) {
        this.props.navigate(`/reservation/details/${id}`);
    }

    updateAppointment(id) {
        this.props.navigate(`/reservations/edit/${id}`);
    }

    handlePageChange(page) {
        this.setState({ page: page.selected }, () => this.fetchRoomReservations());
    }

    render() {
        let { reservations } = this.state;
        return <>
            <HospitalHeader label="Reservation List" />

            <div>
                <nav className="reservation-list-search mt-3 mb-4 navbar navbar-light rounded shadow">
                    <div className="container-fluid">
                        <a className="navbar-brand text-muted">Search Doctor</a>
                        <form onSubmit={this.searchReservation} className="reservation-list-search-form">
                            <input onChange={this.roomCodeFilterOnChange} className="form-control me-2" type="search" placeholder="Room Code" aria-label="Search" />
                            <input onChange={this.roomNameFilterOnChange} className="form-control me-2" type="search" placeholder="Room Name" aria-label="Search" />
                            <select onChange={this.statusFilterOnChange} className="form-select me-2">
                                <option value={RESERVATION_STATUS_CODE.ALL}>All</option>
                                <option value={RESERVATION_STATUS_CODE.CREATED}>Created</option>
                                <option value={RESERVATION_STATUS_CODE.DONE}>Done</option>
                                <option value={RESERVATION_STATUS_CODE.CANCELLED}>Cancelled</option>
                            </select>
                            <button className="search-btn btn btn-outline-success me-2" type="submit">Search</button>
                            <button onClick={this.clearSearch} className="btn btn-outline-success" type="reset">Clear</button>
                        </form>
                    </div>
                </nav>
            </div>

            <div className="table-wrapper">
                <table className="table table-bordered">
                    <thead>
                        <tr>
                            <th scope="col">Room Code</th>
                            <th scope="col">Room Name</th>
                            <th scope="col">Created By</th>
                            <th scope="col">Start Date</th>
                            <th scope="col">End Date</th>
                            <th className="text-center" scope="col">Status</th>
                            <th className="text-center" scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.displayReservationTableRows()}
                    </tbody>
                </table>
            </div>

            {
                reservations.length !== 0 && <ReactPaginate
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

            <ToastContainer className="text-center"
                position="bottom-center"
                autoClose={2000}
                hideProgressBar={true}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                pauseOnHover={false}
                draggable={false}
                theme="colored" />
        </>;
    }
}

export default ReservationList;
