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
            <section class="page_404">
                <div class="four_zero_four_bg">

                </div>

                <div class="content_box_404">
                    <h1 class="text-center ">404</h1>
                    <h3 class="text-center">
                        Looks like you're lost
                    </h3>

                    <p class="text-center">The page you are looking for is not available!</p>

                </div>


            </section>
        </>;
    }
}

export default Error;
