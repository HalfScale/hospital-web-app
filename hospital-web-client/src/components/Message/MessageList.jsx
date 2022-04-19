import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import MessagingService from '../../services/MessagingService';
import defaultImg from '../default.png'

class MessageList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            totalPages: 20
        }

        this.handlePageChange = this.handlePageChange.bind(this);
    }

    componentDidMount() {
        MessagingService.getThreadByLoggedUser({
            page: 0,
            size: 8,
            sort: 'id,asc'
        })
            .then(resp => console.log('getThreadByLoggedUser', resp));
    }

    handlePageChange() {

    }

    render() {
        return <>
            <div className="mt-3 m-auto w-50 shadow rounded">
                <header className="pt-3 profile-header text-center mb-4">
                    <h2 className="text-muted">Message List</h2>
                </header>

                <hr className="hr-text"></hr>

                <div className="message-menu">
                    <div className="message-thread">
                        <img src={defaultImg} className="mx-auto mt-2 shadow thread-user-profile rounded-circle" alt="profile-img" />
                        <div className="d-inline-block">
                            <div className="d-flex flex-column">
                                <span>Doctor</span>
                                <span>Doctor</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="message-list"></div>

                <ReactPaginate
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

            </div>
        </>;
    }
}

export default MessageList;
