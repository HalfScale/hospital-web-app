import './styles/message-list.css'
import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import MessagingService from '../../services/MessagingService';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGear, faXmark, faTrash } from "@fortawesome/free-solid-svg-icons";
import ThreadContainer from './ThreadContainer';
import AuthService from '../../services/AuthService';
import { Modal, Button } from 'react-bootstrap'
import { ToastContainer, toast } from 'react-toastify';
import withNavigation from '../../utils/WithNavigation';
import 'react-toastify/dist/ReactToastify.css';
import './styles/message-list.css'

class MessageList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            filter: '',
            deleteMode: false,
            showModal: false,
            threads: [],
            threadsToBeDeleted: [],
            sortAscFlag: true,
            page: 0,
            size: 8,
            sort: 'id,asc',
            totalPages: 1
        }

        this.handlePageChange = this.handlePageChange.bind(this);
        this.displayThreads = this.displayThreads.bind(this);
        this.toggleDeleteMode = this.toggleDeleteMode.bind(this);
        this.checkSelectedThreads = this.checkSelectedThreads.bind(this);
        this.toggleThread = this.toggleThread.bind(this);
        this.closeDeleteModal = this.closeDeleteModal.bind(this);
        this.showDeleteModal = this.showDeleteModal.bind(this);
        this.deleteSelectedThreads = this.deleteSelectedThreads.bind(this);
        this.getThreads = this.getThreads.bind(this);
        this.toggleSortAsc = this.toggleSortAsc.bind(this);
        this.toggleSortDesc = this.toggleSortDesc.bind(this);
        this.navigateToMessage = this.navigateToMessage.bind(this);
    }

    componentDidMount() {
        this.getThreads();
    }

    getThreads() {
        let { page, size, sort } = this.state;
        console.log('getThreads sort', sort);
        console.log('page', page);
        MessagingService.getThreadByLoggedUser({
            page: page,
            size: size,
            sort: sort
        }).then(resp => {
            // console.log('getThreadByLoggedUser', resp);
            this.setState({
                threads: resp.data.content,
                totalPages: resp.data.totalPages
            });
        });
    }

    navigateToMessage(data) {
        console.log('navigateToMessage');
        let { receiverId, senderId } = data;
        let loggedUserId = AuthService.getUserId();
        let user = null;

        if(loggedUserId !== receiverId) {
            user = receiverId;
        }else {
            user = senderId;
        }

        if(!this.state.deleteMode) {
            this.props.navigate(`/message/send/${user}`);
        }
    }

    displayThreads(deleteMode, threads) {

        if (threads.length > 0) {
            return threads.map(thread => {
                // console.log('thread', thread);
                let loggedUserId = AuthService.getUserId();

                let data = {
                    threadId: thread.id,
                    userProfile: null,
                    user: null,
                    message: thread.latestMessage,
                    receiverId: thread.receiverId,
                    senderId: thread.senderId,
                    redirectTo: this.navigateToMessage
                }

                if (loggedUserId !== thread.receiverId) {
                    data.user = thread.receiverName;
                    data.userProfile = thread.receiverImage;
                } else {
                    data.user = thread.senderName;
                    data.userProfile = thread.senderImage;
                }

                

                return <ThreadContainer key={thread.id} data={data} isDelete={deleteMode} toggle={this.toggleThread} />;
            });
        }

        return <h2 className="lead m-4 text-center">No threads found.</h2>
    }

    checkSelectedThreads() {
        let threadToBeDeleted = this.state.threadsToBeDeleted;
        console.log('threads to be deleted', threadToBeDeleted);

        if (threadToBeDeleted.length > 0) {
            this.showDeleteModal();

        } else {
            toast.warn('Please select a thread to delete');
        }

    }

    deleteSelectedThreads() {
        MessagingService.deleteThread({
            threads: this.state.threadsToBeDeleted
        }).then(resp => {
            
            this.setState({deleteMode: false}, () => {
                this.closeDeleteModal();
                toast.success('Thread successfully deleted.');
                this.getThreads();
            });
            
        });
    }

    toggleThread(event) {
        event.stopPropagation();
        let threadId = event.target.value;
        console.log('value toggled', threadId);

        let threadsToBeDeleted = this.state.threadsToBeDeleted;
        let idIndex = threadsToBeDeleted.indexOf(threadId);

        if (idIndex !== -1) {
            threadsToBeDeleted.splice(idIndex, 1);
        } else {

            threadsToBeDeleted.push(threadId);
        }

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

    showDeleteModal() {
        this.setState({
            showModal: true
        });
    }

    closeDeleteModal() {
        this.setState({
            showModal: false
        });
    }

    displaySort() {

        let isAsc = this.state.sortAscFlag
        let btnClass = {
            base: 'btn btn-outline-success',
            active: 'btn btn-outline-success active'
        }

        return <>
            <button onClick={this.toggleSortAsc} className={isAsc ? `${btnClass.active} me-3` : `${btnClass.base} me-3`}>Oldest</button>
            <button onClick={this.toggleSortDesc} className={!isAsc ? btnClass.active : btnClass.base}>Latest</button>
        </>
    }

    toggleSortAsc() {
        this.setState({sortAscFlag: true,sort: 'id,asc'}, () => { 
            this.getThreads()
        });
    }

    toggleSortDesc() {
        this.setState({sortAscFlag: false, sort: 'id,desc'}, () => {
            this.getThreads();
        });
    }

    handlePageChange(page) {
        this.setState({page: page.selected}, () => {
            this.getThreads();
        });
    }

    render() {
        let { showModal, deleteMode, threads, totalPages } = this.state;
        return <>

            <Modal
                show={showModal}
                onHide={this.closeDeleteModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>Delete Thread</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    Are you sure you want to delete all this thread? All messages in a thread will be deleted!
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeDeleteModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={this.deleteSelectedThreads}>
                        Delete
                    </Button>
                </Modal.Footer>

            </Modal>

            <div className="message-list-container mt-3 pb-2 m-auto shadow rounded">
                <header className="pt-3 profile-header text-center mb-4">
                    <h2 className="text-muted">Message List</h2>
                </header>

                <hr className="hr-text"></hr>

                {
                    threads.length > 0 && <div className="message-menu d-flex flex-row justify-content-center">
                        {
                            deleteMode ?
                                <FontAwesomeIcon onClick={this.toggleDeleteMode} icon={faXmark} size="2xl" className="me-3" /> :
                                <FontAwesomeIcon onClick={this.toggleDeleteMode} icon={faGear} size="2xl" className="me-3" />
                        }
                        {
                            deleteMode &&
                            <FontAwesomeIcon onClick={this.checkSelectedThreads} icon={faTrash} size="2xl" className="ms-3" />
                        }
                        {
                            !deleteMode && this.displaySort()
                        }

                    </div>
                }

                <div className="m-3 message-list">
                    {this.displayThreads(deleteMode, threads)}
                </div>

                {
                    (!deleteMode && totalPages > 1) && <ReactPaginate
                        className="mt-4 pb-3 pagination justify-content-center"
                        nextLabel=">"
                        onPageChange={this.handlePageChange}
                        pageRangeDisplayed={2}
                        marginPagesDisplayed={1}
                        pageCount={totalPages}
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
            <ToastContainer
                position="bottom-center"
                autoClose={3000}
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

export default MessageList;
