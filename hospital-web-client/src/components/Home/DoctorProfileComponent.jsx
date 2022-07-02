import { Component } from 'react';
import defaultImg from '../default.png'
import { Link } from 'react-router-dom';
import { buildProfileURL } from '../../utils/Utils';

class DoctorProfileComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            
        }
    }

    render() {
        let { id, name, image, specialization, description } = this.props.data ? this.props.data : {};

        return <>
            {
                this.props.data && <div className="col-lg-4">
                    <img src={image ? buildProfileURL(image): defaultImg} alt='profile_img' className="shadow doctor-profile-img rounded-circle" />

                    <h2>{name}</h2>
                    <p>{specialization}</p>
                    
                    <p><Link to={`/doctors/details/${id}`} className="btn btn-secondary">View details &raquo;</Link></p>
                </div>
            }

            {
                !this.props.data && <div className="col-lg-4">
                    <img src={defaultImg} alt='profile_img' className="shadow doctor-profile-img rounded-circle" />

                    <h2 className="card-title placeholder-glow">
                        <span className="placeholder col-6"></span>
                    </h2>
                    <p className="card-text placeholder-glow">
                        <span className="placeholder col-7"></span>
                        <span className="placeholder col-4"></span>
                        <span className="placeholder col-4"></span>
                        <span className="placeholder col-6"></span>
                        <span className="placeholder col-8"></span>
                    </p>
                    <a href="#" className="btn btn-secondary disabled placeholder col-6"></a>
                </div>
            }
        </>;
    }
}

export default DoctorProfileComponent;
