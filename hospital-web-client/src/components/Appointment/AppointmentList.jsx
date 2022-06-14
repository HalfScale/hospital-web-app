import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import AuthService from '../../services/AuthService';
import { ROLE_DOCTOR, ROLE_PATIENT } from '../../constants/GlobalConstants';
import AppointmentListTableRow from './AppointmentListTableRow';
import ReactPaginate from 'react-paginate';
import AppointmentService from '../../services/AppointmentService'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class AppointmentList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appointmentIdFilter: '',
            nameFilter: '',
            nameFilterDisplay: '',
            page: 0,
            size: 8,
            sort: 'id,desc',
            totalPages: 0,
            appointments: [],
            currentUserRole: AuthService.getUserRole()
        }

        this.search = this.search.bind(this);
        this.appointmentIdOnChange = this.appointmentIdOnChange.bind(this);
        this.nameOnChange = this.nameOnChange.bind(this);
        this.search = this.search.bind(this);
        this.displayAppointmentRows = this.displayAppointmentRows.bind(this);
        this.fetchAppointments = this.fetchAppointments.bind(this);
        this.clear = this.clear.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.back = this.clear.bind(this);
        this.viewAppointment = this.viewAppointment.bind(this);
        this.editAppointment = this.editAppointment.bind(this);
    }

    fetchAppointments() {
        let { appointmentIdFilter, nameFilter, page, size, sort } = this.state;
        return AppointmentService.findAll({
            appointmentId: appointmentIdFilter,
            username: nameFilter,
            page: page,
            size: size,
            sort: sort
        });
    }

    componentDidMount() {
        this.fetchAppointments().then(resp => {
            console.log('resp', resp);
            this.setState({
                appointments: resp.data.content,
                totalPages: resp.data.totalPages
            });
        });

        if (this.props.location.state) {
            let { message, type } = this.props.location.state;

            setTimeout(() => {
                if (type == 'success') {
                    toast.success(message);
                }else {
                    toast.error(message);
                }
            }, 500);
            
        }
    }

    search(event) {
        event.preventDefault();
        this.fetchAppointments().then(resp => {
            console.log('findAll resp', resp);
            this.setState({
                totalPages: resp.data.totalPages,
                appointments: resp.data.content
            });
        });
    }

    viewAppointment(id) {
        this.props.navigate(`/appointment/details/${id}`);
    }

    editAppointment(id) {
        this.props.navigate(`/appointment/edit/${id}`);
    }

    displayAppointmentRows() {
        let { appointments } = this.state;
        if (appointments.length > 0) {
            return appointments.map(appointment => {
                return <AppointmentListTableRow
                    viewAppointment={this.viewAppointment}
                    editAppointment={this.editAppointment}
                    key={appointment.id}
                    data={appointment} />
            });
        }

        return <tr className="text-center ">
            <td colSpan={7} className="lead">No results found</td>
        </tr>
    }

    appointmentIdOnChange(event) {
        this.setState({
            appointmentIdFilter: event.target.value
        });
    }

    nameOnChange(event) {
        this.setState({
            nameFilter: event.target.value
        });
    }

    clear() {
        this.setState({
            appointmentIdFilter: '',
            nameFilter: ''
        });
    }

    back() {
        this.props.navigate('/');
    }

    handlePageChange(page) {
        this.setState({
            page: page.selected
        }, () => {
            this.fetchAppointments().then(resp => {
                this.setState({
                    appointments: resp.data.content,
                    totalPages: resp.data.totalPages
                });
            });
        });
    }

    render() {
        let { currentUserRole, totalPages, appointmentIdFilter, nameFilter } = this.state;

        return <>
            <div className="mt-3">
                <HospitalHeader label="Appointment List" />

                <div>
                    <nav className="reservation-list-search mt-3 mb-4 navbar navbar-light rounded shadow">
                        <div className="container-fluid">
                            <a className="navbar-brand text-muted">Search Appointment</a>
                            <form onSubmit={this.search} className="reservation-list-search-form">
                                <input onChange={this.appointmentIdOnChange} value={appointmentIdFilter} className="form-control me-2" type="search" placeholder="Appointment ID" aria-label="Search" />
                                <input onChange={this.nameOnChange} value={nameFilter} className="form-control me-2" type="search" placeholder={currentUserRole == ROLE_DOCTOR ? 'Patient Name' : 'Doctor Name'} aria-label="Search" />
                                <button className="search-btn btn btn-outline-success me-2" type="submit">Search</button>
                                <button onClick={this.clear} className="btn btn-outline-success" type="reset">Clear</button>
                            </form>
                        </div>
                    </nav>
                </div>

                <table className="table">
                    <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">{currentUserRole == ROLE_DOCTOR ? 'Patient Name' : 'Doctor Name'}</th>
                            <th scope="col">Start Date</th>
                            <th scope="col">End Date</th>
                            <th scope="col">Email</th>
                            <th scope="col">Status</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.displayAppointmentRows()
                        }
                    </tbody>
                </table>
            </div>

            <ReactPaginate
                className="pagination justify-content-center"
                nextLabel="next >"
                onPageChange={this.handlePageChange}
                pageRangeDisplayed={3}
                marginPagesDisplayed={2}
                pageCount={totalPages}
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

            <ToastContainer className="text-center"
                position="bottom-center"
                autoClose={2500}
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

export default AppointmentList;
