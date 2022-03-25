import './styles/index.css'
import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';
import CardComponent from './CardComponent';

class Doctors extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <div className="mt-3 m-auto">

                <h1 className="text-center">Doctors List</h1>

                <div className="">
                    <nav className="w-50 mx-auto mt-3 mb-3 navbar navbar-light rounded shadow">
                        <div className="container-fluid">
                            <a className="navbar-brand">Search Doctor</a>
                            <form className="d-flex">
                                <select className="form-select me-2">
                                    <option value="0001IM">Internal Medicine</option>
                                    <option value="0002PD">GrePediatricianen</option>
                                    <option value="0003SG">Surgeon</option>
                                    <option value="0004OB">Obstetrician/Gynecologist</option>
                                    <option value="0005CD">Cardiologist</option>
                                    <option value="0006GSG">Gastroenterologist</option>
                                    <option value="0007NG">Neurologist</option>
                                </select>
                                <input className="form-control me-2" type="search" placeholder="Doctor name" aria-label="Search" />
                                <button className="btn btn-outline-success me-2" type="submit">Search</button>
                                <button className="btn btn-outline-success" type="button">Clear</button>
                            </form>
                        </div>
                    </nav>
                </div>

                <hr className="hr-text"></hr>

                <div className="row row-cols-1 row-cols-md-4">
                    <CardComponent />
                    <CardComponent />
                    <CardComponent />
                    <CardComponent />
                </div>

                <ReactPaginate
                    className="pagination justify-content-center"
                    nextLabel="next >"
                    // onPageChange={this.handlePageClick}
                    pageRangeDisplayed={3}
                    marginPagesDisplayed={2}
                    pageCount={20}
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

            </div>
        </>;
    }
}

export default Doctors;
