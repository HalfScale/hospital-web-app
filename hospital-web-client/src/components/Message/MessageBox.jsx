import { Component } from 'react';
import { buildProfileURL } from '../../utils/Utils';
import defaultImg from '../default.png'
class MessageBox extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        console.log('props', this.props.data);
        let { userProfile, message, sender } = this.props.data

        // console.log('user', user);

        let boxProps = {
            base: 'm-2 mb-3 shadow rounded message-box ',
            sender: 'message-box-sender text-end'
        }

        let headerProps = {
            base: 'm-2',
            decorated: ' text-muted'
        }
        return <>
            <div className={sender ? boxProps.base + boxProps.sender : boxProps.base}>
                {
                    sender && <>
                        <img src={userProfile ? buildProfileURL(userProfile) : defaultImg} className="m-2 profile-img-small shadow rounded-circle" alt="profile-img" />
                    </>
                }
                <div className={sender ? "ms-2 me-2 pb-2" : "m-2 p-2"}>{message}</div>
            </div>
        </>;
    }
}

export default MessageBox;
