import { Component } from 'react';

class MessageBox extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        console.log('props', this.props.data);
        let {user, message, sender} = this.props.data

        // console.log('user', user);

        let boxProps = {
            base: 'm-2 mb-3 shadow rounded message-box ',
            sender: 'text-end'
        }

        let headerProps = {
            base: 'm-2',
            decorated: ' text-muted'
        }
        return <>
            <div className={sender ? boxProps.base + boxProps.sender : boxProps.base}>
                <h3 className={sender ? headerProps.base : headerProps.base + headerProps.decorated}>{user}</h3>
                <hr />
                <div className="m-2 pb-2">{message}</div>
            </div>
        </>;
    }
}

export default MessageBox;
