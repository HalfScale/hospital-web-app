import './styles/main.css';
import { Component } from 'react';
import MessagingService from '../../services/MessagingService';

class Message extends Component {
    constructor(props) {
        super(props);

        this.state = {
            receiverId: this.props.params.id,
            threadId: -1,
            message: ''
        }

        this.sendMessage = this.sendMessage.bind(this);
        this.messageOnChange = this.messageOnChange.bind(this);
    }

    componentDidMount() {
        console.log('receiverId', this.state.receiverId);
        MessagingService.getMessageThread(this.state.receiverId, {
            page: 0,
            size: 8,
            sort: 'id,asc'
        }).then(resp => {
            console.log('getMessageThread', resp);
            let { content } = resp.data;
            let threadId = content.length === 0 ? -1 : content[0].id;
            this.setState({
                threadId: threadId
            });
            return threadId;
        }).then(threadId => {
            console.log('threadId', threadId);
            return MessagingService.getThreadMessages(threadId, {
                page: 0,
                size: 8,
                sort: 'id,asc'
            });
        }).then(resp => {
            console.log('resp thread messages', resp);
        });
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
        }).then(resp => console.log('resp', resp));
    }

    messageOnChange(event) {
        this.setState({
            message: event.target.value
        });
    }

    render() {
        return (
            <div className="mt-3 m-auto w-50 shadow rounded">
                <header className="profile-header text-center mb-4">
                    <h2 className="text-muted">Message</h2>
                </header>

                <hr className="hr-text"></hr>

                <div className="message-container">
                    <div className="message-list-box"></div>
                    <div className="message-sender-box">
                        <textarea onChange={this.messageOnChange} className="form-control" rows="4" placeholder="send message..."></textarea>
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
