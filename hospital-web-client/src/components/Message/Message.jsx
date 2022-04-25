import './styles/main.css';
import './styles/message.css';
import defaultImg from '../default.png'
import { Component, createRef } from 'react';
import MessagingService from '../../services/MessagingService';
import MessageBox from './MessageBox';
import AuthService from '../../services/AuthService';
import { buildProfileURL } from '../../utils/Utils';
import { ToastContainer, toast } from 'react-toastify';

const messagesEndRef = createRef();

class Message extends Component {

    constructor(props) {
        super(props);

        this.state = {
            receiverId: this.props.params.id,
            threadId: -1,
            message: '',
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
    }

    componentDidMount() {
        this.fetchMessages();
        this.scrollToBottom();
    }

    componentDidUpdate() {
        this.scrollToBottom();
    }

    backToMessages() {
        this.props.navigate('/messages');
    }

    fetchMessages() {
        console.log('receiverId', this.state.receiverId);
        MessagingService.getThreadMessagesByuserId(this.state.receiverId, {
            page: 0,
            size: 8,
            sort: 'id,asc'
        }).then(resp => {
            console.log('getMessageThread', resp);

            if (resp.data.content.length > 0) {
                // onyl get the first since, they are all thesame with with thread id
                let thread = resp.data.content[0].thread;
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

                this.setState({
                    threadId: thread.id,
                    messageContent: resp.data.content,
                    userNameDisplay: userNameToDisplay,
                    userProfile: userProfileImg ? buildProfileURL(userProfileImg) : defaultImg
                });
            }
        });
    }

    scrollToBottom() {
        messagesEndRef.current.scrollIntoView({ behavior: 'smooth' })
    }

    sendMessage() {
        let { receiverId, threadId, message } = this.state;

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
            this.setState({ message: '' }, () => {
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
                console.log('data', data);
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
        let { userNameDisplay, userProfile } = this.state;
        return (
            <div className="mt-3 m-auto w-50 shadow rounded">
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

                <div className="mx-auto message-container">
                    <div className="mb-3 message-list-box shadow rounded overflow-auto">
                        {this.displayMessageBox(this.state.messageContent)}
                        <div className="fst-italic shadow rounded p-2 text-center text-muted fs-6">Thread is empty. Send a new message</div>
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
                    pauseOnFocusLoss
                    pauseOnHover={false}
                    draggable={false}
                    theme="colored" />
            </div>
        );
    }
}

export default Message;
