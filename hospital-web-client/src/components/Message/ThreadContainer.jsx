import { Component } from 'react';
import defaultImg from '../default.png'
import { buildProfileURL } from '../../utils/Utils';

class ThreadContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let isDeleteMode = this.props.isDelete;
        let { threadId, userProfile, user, message } = this.props.data;

        const messageThreadClass = {
            base: 'mb-3 message-thread rounded shadow p-2',
            decorated: 'mb-3 message-thread message-thread-decorator rounded shadow p-2'
        }

        return <>
            <div className={isDeleteMode ? messageThreadClass.base : messageThreadClass.decorated}>

                <div className="content-wrapper d-flex flex-row">

                    <div className="d-flex flex-row">
                        {isDeleteMode && <div className="form-check align-self-center">
                            <input onClick={this.props.toggle} className="form-check-input" type="checkbox" value={threadId} />
                            <label className="form-check-label me-3"></label>
                        </div>}
                        <img src={userProfile ? buildProfileURL(userProfile) : defaultImg} className="shadow thread-user-profile rounded-circle" alt="profile-img" />
                    </div>

                    <div className="ms-3 d-inline-block">
                        <div className="d-flex flex-column mt-3">
                            <h5>{user}</h5>
                            <span className="text-muted mb-2">{message}</span>
                        </div>
                    </div>
                </div>

            </div>
        </>;
    }
}

export default ThreadContainer;
