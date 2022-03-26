import { Component } from 'react';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';
import { buildProfileURL } from '../../utils/Utils';

class CardComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {

        }
    }

    render() {
        let { id, name, image, specialization, description } = this.props.data ? this.props.data : {};

        return (
            <div className="col mb-4">
                {
                    this.props.data && <div className="card rounded shadow mx-auto doctor-cards">
                        <img src={buildProfileURL(image)} className="card-img-top" alt="..." />
                        <div className="card-body">
                            <h5 className="card-title">{name}</h5>
                            <h5 class="text-muted">{specialization}</h5>
                            <hr className="hr-text"></hr>
                            <p className="card-text text-truncate">{description}</p>
                            <div className="d-flex"><button className="w-100 btn btn-primary">Details</button></div>
                        </div>
                    </div>
                }

                {
                    !this.props.data && <div className="card mx-auto doctor-cards">
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
                            <a href="#" className="btn btn-primary disabled placeholder col-6"></a>
                        </div>
                    </div>
                }

            </div>

        );
    }
}

export default CardComponent;
