import { Component } from 'react';

class HospitalHeader extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>

            <header className="profile-header text-center mb-4">
                <h1 className="display-1">Hospital Name</h1>
                <h2 className="text-muted">{this.props.label}</h2>
            </header>

            <hr className="hr-text"></hr>

        </>;
    }
}

export default HospitalHeader;
