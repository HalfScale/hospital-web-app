import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import MessagingService from '../../services/MessagingService';
import defaultImg from '../default.png'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGear, faXmark, faTrash } from "@fortawesome/free-solid-svg-icons";
import ThreadContainer from './ThreadContainer';
import AuthService from '../../services/AuthService';

class MessageList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            filter: '',
            deleteMode: false,
            threads: [],
            threadsToBeDeleted: [],
            totalPages: 10
        }

        this.handlePageChange = this.handlePageChange.bind(this);
        this.displayThreads = this.displayThreads.bind(this);
        this.toggleDeleteMode = this.toggleDeleteMode.bind(this);
        this.deleteThreads = this.deleteThreads.bind(this);
        this.toggleThread = this.toggleThread.bind(this);
    }

    componentDidMount() {
        MessagingService.getThreadByLoggedUser({
            page: 0,
            size: 8,
            sort: 'id,asc'
        }).then(resp => {
            console.log('getThreadByLoggedUser', resp);
            this.setState({
                threads: resp.data.content
            });
        });
    }

    displayThreads(deleteMode, threads) {
        return threads.map(thread => {
            console.log('thread', thread);
            let loggedUserId = AuthService.getUserId();

            let data = {
                threadId: thread.id,
                userProfile: null,
                user: null,
                message: thread.latestMessage
            }

            if (loggedUserId !== thread.receiverId) {
                data.user = thread.receiverName;
                data.userProfile = thread.receiverImage;
            } else {
                data.user = thread.senderName;
                data.userProfile = thread.senderImage;
            }

            return <ThreadContainer key={thread.id} data={data} isDelete={deleteMode} toggle={this.toggleThread} />
        });
    }

    deleteThreads() {
        console.log('threads to be deleted', this.state.threadsToBeDeleted);
    }

    toggleThread(event) {
        let threadId = event.target.value;
        console.log('value toggled', threadId);

        let threadsToBeDeleted = this.state.threadsToBeDeleted;
        threadsToBeDeleted.push(threadId);
        this.setState({
            threadsToBeDeleted: threadsToBeDeleted
        });
    }

    toggleDeleteMode() {
        this.state.threadsToBeDeleted = [];
        this.setState({
            deleteMode: !this.state.deleteMode
        });
    }

    handlePageChange() {

    }

    render() {
        let data = {
            threadId: 1,
            userProfile: null,
            user: 'Marwin Buenaventura',
            message: 'Booking for consultation'
        };
        return <>
            <div className="mt-3 pb-2 m-auto w-50 shadow rounded">
                <header className="pt-3 profile-header text-center mb-4">
                    <h2 className="text-muted">Message List</h2>
                </header>

                <hr className="hr-text"></hr>


                <div className="message-menu d-flex flex-row justify-content-center">
                    {
                        this.state.deleteMode ?
                            <FontAwesomeIcon onClick={this.toggleDeleteMode} icon={faXmark} size="2xl" className="me-3" /> :
                            <FontAwesomeIcon onClick={this.toggleDeleteMode} icon={faGear} size="2xl" className="me-3" />
                    }
                    {
                        this.state.deleteMode &&
                        <FontAwesomeIcon onClick={this.deleteThreads} icon={faTrash} size="2xl" className="ms-3" />
                    }
                    {
                        !this.state.deleteMode && <>
                            <button className="btn btn-outline-success me-3">Oldest</button>
                            <button className="btn btn-outline-success">Latest</button>
                        </>
                    }

                </div>

                <div className="m-3 message-list">
                    {this.displayThreads(this.state.deleteMode, this.state.threads)}
                </div>

                {
                    !this.state.deleteMode && <ReactPaginate
                        className="mt-4 pb-3 pagination justify-content-center"
                        nextLabel=">"
                        onPageChange={this.handlePageChange}
                        pageRangeDisplayed={2}
                        marginPagesDisplayed={1}
                        pageCount={this.state.totalPages}
                        previousLabel="<"
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

export default MessageList;
