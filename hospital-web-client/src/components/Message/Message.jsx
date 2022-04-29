import './styles/main.css';
import './styles/message.css';
import defaultImg from '../default.png'
import { Component, createRef } from 'react';
import MessagingService from '../../services/MessagingService';
import MessageBox from './MessageBox';
import AuthService from '../../services/AuthService';
import { buildProfileURL } from '../../utils/Utils';
import { ToastContainer, toast } from 'react-toastify';
import UserService from '../../services/UserService';

const messagesEndRef = createRef();
const messagesTopRef = createRef();

class Message extends Component {

    constructor(props) {
        super(props);

        this.state = {
            page: 0,
            size: 8,
            sort: 'id,desc',
            backReadPage: 0,
            isReverse: true,
            isSent: false,
            isAtTop: true,
            totalPages: 0,
            receiverId: this.props.params.id,
            threadId: -1,
            message: '',
            messageIds: [],
            messageContent: [],
            userNameDisplay: '',
            userProfile: defaultImg
        }

        this.sendMessage = this.sendMessage.bind(this);
        this.messageOnChange = this.messageOnChange.bind(this);
        this.displayMessageBox = this.displayMessageBox.bind(this);
        this.fetchMessages = this.fetchMessages.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
        this.backToMessages = this.backToMessages.bind(this);
        this.handleScrollTrack = this.handleScrollTrack.bind(this);
        this.insertToIds = this.insertToIds.bind(this);
        this.isDuplicate = this.isDuplicate.bind(this);
    }

    componentDidMount() {
        this.fetchMessages();
        this.scrollToBottom();
        messagesTopRef.current.addEventListener("scroll", this.handleScrollTrack);
        // messagesTopRef.current.addEventListener("scroll", this.handleScrollTrack);
    }

    componentDidUpdate() {
        if (this.state.page === 0) {
            this.scrollToBottom();
        }
    }


    handleScrollTrack() {
        let { scrollTop, scrollHeight, clientHeight } = messagesTopRef.current;
        let height = scrollHeight - clientHeight;
        let scrolled = scrollTop / height;
        if (messagesTopRef !== null && scrollTop === 0) {
            this.setState(state => {
                return { page: ++state.backReadPage }
            }, () => {
                this.fetchMessages();
            });
        }
    }

    backToMessages() {
        this.props.navigate('/messages');
    }

    fetchMessages() {
        console.log('receiverId', this.state.receiverId);
        let { scrollTop, scrollHeight, clientHeight, scrollTo } = messagesTopRef.current;
        let height = scrollHeight - clientHeight;
        let { page, size, sort } = this.state;
        MessagingService.getThreadMessagesByuserId(this.state.receiverId, {
            page: page,
            size: size,
            sort: sort
        }).then(resp => {
            let { page, isReverse, totalPages, backReadPage, isSent } = this.state;
            if (resp.data.content.length > 0) {
                // onyl get the first since, they are all thesame with with thread id
                messagesTopRef.current.scrollTo(0, height);
                let thread = resp.data.content[0].thread;
                let fetchedMessages = resp.data.content;
                let messagesToDisplay = [];
                let userNameToDisplay = null;
                let userProfileImg = null;

                let loggedUserId = AuthService.getUserId();

                if (loggedUserId === thread.senderId) {
                    userNameToDisplay = thread.receiverName;
                    userProfileImg = thread.receiverImage;
                } else {
                    userNameToDisplay = thread.senderName;
                    userProfileImg = thread.senderImage;
                }

                if (page === 0 && !isSent) {
                    fetchedMessages.forEach(message => {
                        if (this.insertToIds(message.id) !== -1) {
                            messagesToDisplay.push(message);
                        };
                    });
                }

                if (isReverse) {
                    messagesToDisplay.reverse();
                }

                // //if current page, is less than or equal to totalPages, then concat the succeding message fetch
                if (page > 0 && backReadPage <= totalPages) {
                    messagesToDisplay = this.state.messageContent;
                    fetchedMessages.forEach(message => {
                        if (this.insertToIds(message.id) !== -1) {
                            messagesToDisplay.unshift(message);
                        };
                    });
                }

                if (isSent) {
                    messagesToDisplay = this.state.messageContent;
                    fetchedMessages.forEach(message => {
                        if (this.insertToIds(message.id) !== -1) {
                            messagesToDisplay.push(message);
                        };
                    });
                    this.setState({ isSent: false, isReverse: true });
                }

                this.setState({
                    threadId: thread.id,
                    totalPages: resp.data.totalPages,
                    messageContent: messagesToDisplay,
                    userNameDisplay: userNameToDisplay,
                    userProfile: userProfileImg ? buildProfileURL(userProfileImg) : defaultImg
                });

                return null; // return null if there is an existing thread

            }

            return UserService.getUserById(this.state.receiverId);
        }).then(resp => {
            console.log('resp 2', resp);

            if (resp) {
                let { name, profileImg } = resp.data;
                this.setState({
                    userNameDisplay: name,
                    userProfile: profileImg ? buildProfileURL(profileImg) : defaultImg
                });
            }
        });
    }

    insertToIds(id) {
        let { messageIds } = this.state;
        if (!this.isDuplicate(id)) {
            messageIds.push(id);
            this.setState({
                messageIds: messageIds
            });
            return id;
        }
        return -1;
    }

    isDuplicate(id) {
        let { messageIds } = this.state;
        return messageIds.indexOf(id) > -1;
    }

    scrollToBottom() {
        messagesEndRef.current.scrollIntoView({ behavior: 'smooth' })
    }

    sendMessage() {
        let { receiverId, threadId, message } = this.state;
        let basePage = 0;

        console.log('send message data', {
            receiverId: receiverId,
            threadId: threadId,
            message: message
        });
        MessagingService.sendMessage({
            receiverId: receiverId,
            threadId: threadId,
            message: message
        }).then(resp => {
            console.log('resp', resp);
            this.setState({ message: '', page: basePage, isSent: true, isReverse: false }, () => {
                toast.success('Message sent successfully!')
                this.fetchMessages();
            })

        });

        console.log("asdasd");
    }

    messageOnChange(event) {
        this.setState({
            message: event.target.value
        });
    }

    displayMessageBox(content) {
        if (content.length > 0) {
            return content.map(data => {
                // console.log('data', data);
                let loggedUserId = AuthService.getUserId();
                let messageData = null;


                // sender or receiver
                let senderId = data.thread.senderId;

                if (loggedUserId === senderId) {
                    messageData = {
                        user: 'Me',
                        message: data.message,
                        sender: true,
                        userProfile: data.thread.senderImage
                    }
                    // console.log('messageData', messageData);
                    return <MessageBox data={messageData} />
                }

                messageData = {
                    user: data.thread.senderName,
                    message: data.message,
                    sender: false
                }

                return <MessageBox data={messageData} />
            });
        }
    }

    render() {
        let { userNameDisplay, userProfile, messageContent } = this.state;
        return (
            <div className="mt-3 m-auto message-container shadow rounded">
                <header className="profile-header text-center mb-4">
                    <h2 className="pt-2 text-muted">Message</h2>
                    <div className="d-flex flex-row justify-content-center">
                        <img src={userProfile} className="profile-img shadow rounded-circle" alt="profile-img" />
                        <div className="ms-2 d-flex flex-column justify-content-center">
                            <span className="fs-4">{userNameDisplay}</span>
                        </div>
                    </div>

                </header>

                <hr className="hr-text"></hr>

                <div className="mx-auto message-view">

                    <div ref={messagesTopRef} className="mb-3 message-list-box shadow rounded overflow-auto">
                        {
                            messageContent.length <= 0 && <div className="fst-italic shadow rounded p-2 text-center text-muted fs-6">
                                Thread is empty. Send a new message</div>
                        }
                        {this.displayMessageBox(messageContent)}
                        <div ref={messagesEndRef} />
                    </div>

                    <div className="message-sender-box">
                        <textarea style={{ resize: 'none' }} value={this.state.message} onChange={this.messageOnChange} className="form-control" rows="4" placeholder="send message..."></textarea>
                    </div>
                    <div className="d-grid gap-2 buttons mt-3">
                        <button onClick={this.sendMessage} className="btn btn-primary">Send</button>
                        <button onClick={this.backToMessages} className="btn btn-primary">Back</button>
                    </div>
                </div>

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
            </div>
        );
    }
}

export default Message;
