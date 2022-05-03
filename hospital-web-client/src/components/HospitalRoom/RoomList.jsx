import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import ReactPaginate from 'react-paginate';

class RoomList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            filter: {
                roomCode: '',
                roomName: ''
            },
            page: 0,
            size: 8,
            sort: 'id,asc',
            totalPages: 0,
            rooms: [],
            roomIdToDelete: null
        }

        this.handlePageChange = this.handlePageChange.bind(this);
    }

    componentDidMount() {
        
    }

    handlePageChange() {

    }

    render() {
        return <div>
            <HospitalHeader label='Room List' />

            <div>
                <nav className="w-50 mx-auto mt-3 mb-4 navbar navbar-light rounded shadow">
                    <div className="container-fluid">
                        <a className="navbar-brand text-muted">Search Doctor</a>
                        <form onSubmit={this.searchDoctor} className="d-flex">
                            <input className="form-control me-2" type="search" placeholder="Room Code" aria-label="Search" />
                            <input className="form-control me-2" type="search" placeholder="Room Name" aria-label="Search" />
                            <button className="btn btn-outline-success me-2" type="submit">Search</button>
                            <button className="btn btn-outline-success" onClick={this.clear} type="reset">Clear</button>
                        </form>
                    </div>
                </nav>
            </div>

            <button className="m-3 btn btn-primary">Create</button>

            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">Room Code</th>
                        <th scope="col">Room Name</th>
                        <th scope="col">Created By</th>
                        <th scope="col">Updated By</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td>@mdo</td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>Jacob</td>
                        <td>Thornton</td>
                        <td>@fat</td>
                        <td>@mdo</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td colSpan="2">Larry the Bird</td>
                        <td>@twitter</td>
                        <td>@mdo</td>
                    </tr>
                </tbody>
            </table>

            <ReactPaginate
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
        </div>;
    }
}

export default RoomList;
