import './styles/main.css';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';

class Error extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return <>
            <section className="page_404">
                <div className="four_zero_four_bg">

                </div>

                <div className="content_box_404">
                    <h1 className="text-center ">404</h1>
                    <h3 className="text-center">
                        Looks like you're lost
                    </h3>

                    <p className="text-center">The page you are looking for is not available!</p>

                </div>


            </section>
        </>;
    }
}

export default Error;
