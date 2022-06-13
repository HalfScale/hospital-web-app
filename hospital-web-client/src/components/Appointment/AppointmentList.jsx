import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import AuthService from '../../services/AuthService';
import { ROLE_DOCTOR, ROLE_PATIENT } from '../../constants/GlobalConstants';
import AppointmentListTableRow from './AppointmentListTableRow';
import ReactPaginate from 'react-paginate';

class AppointmentList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appointmentIdFilter: '',
            patientNameFilter: '',
            doctorNameFilter: '',
            nameFilterDisplay: '',
            page: 0,
            size: 8,
            sort: 'id,desc',
            totalPages: 0,
            appointment: [],
            currentUserRole: AuthService.getUserRole()
        }

        this.search = this.search.bind(this);
        this.clear = this.clear.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.back = this.clear.bind(this);
    }

    componentDidMount() {
    }

    search() {

    }

    clear() {

    }

    back() {

    }

    handlePageChange() {

    }

    render() {
        let { currentUserRole, totalPages } = this.state;

        return <>
            <div className="mt-3">
                <HospitalHeader label="Appointment List" />

                <div>
                    <nav className="reservation-list-search mt-3 mb-4 navbar navbar-light rounded shadow">
                        <div className="container-fluid">
                            <a className="navbar-brand text-muted">Search Appointment</a>
                            <form onSubmit={this.searchReservation} className="reservation-list-search-form">
                                <input className="form-control me-2" type="search" placeholder="Appointment ID" aria-label="Search" />
                                <input className="form-control me-2" type="search" placeholder={currentUserRole == ROLE_DOCTOR ? 'Patient Name' : 'Doctor Name'} aria-label="Search" />
                                <button className="search-btn btn btn-outline-success me-2" type="submit">Search</button>
                                <button onClick={this.clearSearch} className="btn btn-outline-success" type="reset">Clear</button>
                            </form>
                        </div>
                    </nav>
                </div>

                <table className="table">
                    <thead>
                        <tr>
                            <th className="text-center" scope="col">ID</th>
                            <th className="text-center" scope="col">{currentUserRole == ROLE_DOCTOR ? 'Patient Name' : 'Doctor Name'}</th>
                            <th className="text-center" scope="col">Start Date</th>
                            <th className="text-center" scope="col">End Date</th>
                            <th className="text-center" scope="col">Email</th>
                            <th className="text-center" scope="col">Status</th>
                            <th className="text-center" scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
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
        </>;
    }
}

export default AppointmentList;
