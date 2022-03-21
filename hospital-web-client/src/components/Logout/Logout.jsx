import { Component } from 'react';

class Logout extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <main className="cover-main d-flex w-100 h-100 p-3 mx-auto flex-column">
                <h1>Thank you!</h1>
                <p className="lead">You are now logged out.</p>
                <p className="lead">
                    <a href="#" className="btn btn-lg btn-secondary fw-bold border-white bg-white">Learn more</a>
                </p>
            </main>
        </>;
    }
}

export default Logout;
