import './styles/main.css';
import { Component, createRef} from 'react';
import MessagingService from '../../services/MessagingService';
import MessageBox from './MessageBox';
import AuthService from '../../services/AuthService';

const messagesEndRef = createRef();

class Message extends Component {

    constructor(props) {
        super(props);

        this.state = {
            receiverId: this.props.params.id,
            threadId: -1,
            message: '',
            messageContent: []
        }

        this.sendMessage = this.sendMessage.bind(this);
        this.messageOnChange = this.messageOnChange.bind(this);
        this.displayMessageBox = this.displayMessageBox.bind(this);
        this.fetchMessages = this.fetchMessages.bind(this);
        this.scrollToBottom = this.scrollToBottom.bind(this);
    }

    componentDidMount() {
        this.fetchMessages();
        this.scrollToBottom();
    }

    componentDidUpdate() {
        this.scrollToBottom();
    }

    fetchMessages() {
        console.log('receiverId', this.state.receiverId);
        MessagingService.getThreadMessagesByuserId(this.state.receiverId, {
            page: 0,
            size: 8,
            sort: 'id,asc'
        }).then(resp => {
            console.log('getMessageThread', resp);
            // onyl get the first since, they are all thesame with with thread id
            if(resp.data.content.length > 0) {
                let threadId = resp.data.content[0].thread.id;
                this.setState({
                    threadId: threadId,
                    messageContent: resp.data.content
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
            this.setState({
                message: ''
            })
            this.fetchMessages();
        });

        console.log("asdasd");
    }

    messageOnChange(event) {
        this.setState({
            message: event.target.value
        });
    }

    displayMessageBox(content) {
        if(content.length > 0) {
            return content.map(data => {
                console.log('data', data);
                let loggedUserId = AuthService.getUserId();
                let messageData = null;
                

                // sender or receiver
                let senderId = data.thread.senderId;

                if(loggedUserId === senderId) {
                    messageData = {
                        user: 'Me',
                        message: data.message,
                        sender: true
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
        return (
            <div className="mt-3 m-auto w-50 shadow rounded">
                <header className="profile-header text-center mb-4">
                    <h2 className="text-muted">Message</h2>
                </header>

                <hr className="hr-text"></hr>

                <div className="message-container">
                    <div className="message-list-box overflow-auto">
                        {this.displayMessageBox(this.state.messageContent)}
                        <div ref={messagesEndRef} />
                    </div>
                    <div className="message-sender-box">
                        <textarea value={this.state.message} onChange={this.messageOnChange} className="form-control" rows="4" placeholder="send message..."></textarea>
                    </div>
                    <div className="buttons">
                        <button onClick={this.sendMessage} className="btn btn-primary">Send</button>
                    </div>
                </div>
            </div>
        );
    }
}

export default Message;
