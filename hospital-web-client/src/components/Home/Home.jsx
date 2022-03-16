import './Home.css';
import { Component } from 'react';

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return (
            <>
                <main className="cover-main d-flex w-100 h-100 p-3 mx-auto flex-column">
                    <h1>Landing page.</h1>
                    <p className="lead">Cover is a one-page template for building simple and beautiful home pages. Download, edit the text, and add your own fullscreen background photo to make it your own.</p>
                    <p className="lead">
                        <a href="#" className="btn btn-lg btn-secondary fw-bold border-white bg-white">Learn more</a>
                    </p>
                </main>
            </>
        );
    }
}

export default Home;
