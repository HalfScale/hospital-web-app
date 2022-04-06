import './styles/index.css'
import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import HospitalHeader from '../HospitalHeader';
import CardComponent from './CardComponent';
import DoctorsService from '../../services/DoctorsService';

class Doctors extends Component {
    constructor(props) {
        super(props);
        this.state = {
            preloading: true,
            doctors: [],
            page: 0,
            size: 8,
            sort: 'id,asc',
            totalDoctors: 0,
            totalPages: 20,
            doctorCodeId: '',
            doctorName: '',
            doctorCodeFilter: '',
            doctorNameFilter: ''
        }

        this.handlePageChange = this.handlePageChange.bind(this);
        this.displayDoctors = this.displayDoctors.bind(this);
        this.searchDoctor = this.searchDoctor.bind(this);
        this.doctorCodeOnChange = this.doctorCodeOnChange.bind(this);
        this.doctorNameOnChange = this.doctorNameOnChange.bind(this);
        this.clear = this.clear.bind(this);
    }

    componentDidMount() {

        DoctorsService.getDoctors({
            page: this.state.page,
            size: this.state.size,
            sort: this.state.sort
        }).then(resp => {
            console.log('resp', resp)
            this.setState({
                doctors: resp.data.content,
                totalPages: resp.data.totalPages,
                totalDoctors: resp.data.totalElements
            });
        }).catch(err => {
            console.log('err', err.response)
        }).finally(() => {
            this.setState({
                preloading: false
            });
        });
        
    }

    displayDoctors(doctors) {
        if (doctors.length === 0) {
            return (
                <div className="text-center w-100 m-3">
                    <span className="text-muted fs-1">No Results.</span>
                </div>
            );
        }

        return doctors.map(doctor => <CardComponent key={doctor.id} data={doctor} />);
    }

    handlePageChange(page) {
        console.log('handle page click', page.selected);

        DoctorsService.getDoctors({
            page: page.selected,
            size: this.state.size,
            sort: this.state.sort
        }).then(resp => {
            console.log('resp', resp);
            this.setState({
                doctors: resp.data.content,
                totalPages: resp.data.totalPages,
                totalDoctors: resp.data.totalElements
            });
        }).catch(err => console.log('err', err.response));
    }

    searchDoctor(event) {
        event.preventDefault();

        DoctorsService.getDoctors({
            page: this.state.page,
            size: this.state.size,
            sort: this.state.sort,
            name: this.state.doctorNameFilter,
            doctorCode: this.state.doctorCodeFilter
        }).then(resp => {
            console.log('resp', resp)
            this.setState({
                doctors: resp.data.content,
                totalPages: resp.data.totalPages,
                totalDoctors: resp.data.totalElements
            });
        }).catch(err => {
            console.log('err', err.response)
        });

    }

    doctorCodeOnChange(event) {
        this.setState({
            doctorCodeFilter: event.target.value
        });
    }

    doctorNameOnChange(event) {
        this.setState({
            doctorNameFilter: event.target.value
        });
    }

    clear() {
        this.setState({
            doctorCodeFilter: '',
            doctorNameFilter: ''
        });
    }

    render() {
        return <>
            <div className="mt-3 m-auto">

                <HospitalHeader label='Doctors'/>

                <div>
                    <nav className="w-50 mx-auto mt-3 mb-4 navbar navbar-light rounded shadow">
                        <div className="container-fluid">
                            <a className="navbar-brand text-muted">Search Doctor</a>
                            <form onSubmit={this.searchDoctor} className="d-flex">
                                <select onChange={this.doctorCodeOnChange} className="form-select me-2">
                                    <option value="">All</option>
                                    <option value="0001IM">Internal Medicine</option>
                                    <option value="0002PD">GrePediatricianen</option>
                                    <option value="0003SG">Surgeon</option>
                                    <option value="0004OB">Obstetrician/Gynecologist</option>
                                    <option value="0005CD">Cardiologist</option>
                                    <option value="0006GSG">Gastroenterologist</option>
                                    <option value="0007NG">Neurologist</option>
                                </select>
                                <input onChange={this.doctorNameOnChange} className="form-control me-2" type="search" placeholder="Doctor name" aria-label="Search" />
                                <button className="btn btn-outline-success me-2" type="submit">Search</button>
                                <button className="btn btn-outline-success" onClick={this.clear} type="reset">Clear</button>
                            </form>
                        </div>
                    </nav>
                </div>

                <div className="row row-cols-1 row-cols-md-4">
                    {
                        this.state.preloading && <>
                            <CardComponent />
                            <CardComponent />
                            <CardComponent />
                            <CardComponent />
                        </>
                    }
                    {
                        this.displayDoctors(this.state.doctors)
                    }
                </div>

                {
                    this.state.totalDoctors > 8 && <ReactPaginate
                        className="pagination justify-content-center"
                        nextLabel="next >"
                        onPageChange={this.handlePageChange}
                        pageRangeDisplayed={3}
                        marginPagesDisplayed={2}
                        pageCount={this.state.totalPages}
                        previousLabel="< previous"
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
            </div>
        </>;
    }
}

export default Doctors;
