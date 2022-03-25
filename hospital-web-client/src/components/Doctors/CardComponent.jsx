import { Component } from 'react';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';

class CardComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        return (
            <div className="col mb-4">
                {/* <div className="card rounded shadow mx-auto doctor-cards">
                    <img src={DEFAULT_PROFILE_IMG} className="card-img-top" alt="..." />
                    <div className="card-body">
                        <h5 className="card-title">Card title</h5>
                        <p className="card-text">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                    </div>
                </div> */}

                <div className="card mx-auto doctor-cards">
                    <img src={DEFAULT_PROFILE_IMG} className="card-img-top" alt="..." />
                    <div className="card-body">
                        <h5 className="card-title placeholder-glow">
                            <span className="placeholder col-6"></span>
                        </h5>
                        <p className="card-text placeholder-glow">
                            <span className="placeholder col-7"></span>
                            <span className="placeholder col-4"></span>
                            <span className="placeholder col-4"></span>
                            <span className="placeholder col-6"></span>
                            <span className="placeholder col-8"></span>
                        </p>
                        <a href="#" tabindex="-1" className="btn btn-primary disabled placeholder col-6"></a>
                    </div>
                </div>
            </div>

        );
    }
}

export default CardComponent;
