import { Component } from 'react';
import UserService from '../../services/UserService';
import HospitalHeader from '../HospitalHeader';
import defaultImage from '../default.png';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { ROLE_DOCTOR } from '../../constants/GlobalConstants';
import AuthService from '../../services/AuthService';
import { buildProfileURL } from '../../utils/Utils';

class UserInfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: props.params.id,
            firstName: '',
            lastName: '',
            email: '',
            mobileNo: '',
            gender: '',
            address: '',
            birthDate: '',
            profileImage: defaultImage
        }

        this.message = this.message.bind(this);
        this.back = this.back.bind(this);
    }

    componentDidMount() {
        let { userId } = this.state;
        UserService.getUserById(userId)
            .then(resp => {
                let { firstName, lastName, email, gender, mobileNo,
                    address, birthDate, profileImg } = resp.data;
                this.setState({
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    gender: gender,
                    mobileNo: mobileNo,
                    address: address,
                    birthDate: birthDate,
                    profileImage: profileImg ? buildProfileURL(profileImg) : defaultImage
                });
            }).catch(error => this.props.navigate('/'));
    }

    message() {
        this.props.navigate(`/message/send/${this.state.userId}`);
    }

    back() {
        this.props.navigate('/appointment');
    }

    render() {
        let { userId, firstName, lastName, email, mobileNo,
            gender, address, birthDate, profileImage } = this.state;
        return <>
            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Patient Details" />

                <div className="text-center">
                    <img src={profileImage} alt="profile-img" width="140" height="140" className="me-3 rounded-circle shadow" />
                    {
                        AuthService.getUserRole() === ROLE_DOCTOR && <div className="mt-3">
                            <FontAwesomeIcon icon={faEnvelope} size="2xl" />
                            <span className="text-muted ms-2 fs-5">
                                <a role="button" onClick={this.message} className="text-decoration-none">Message</a>
                            </span>
                        </div>
                    }
                </div>

                <hr className="hr-text"></hr>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">First Name:</label>
                        <label className="text-muted fs-5">{firstName}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Last Name:</label>
                        <label className="text-muted fs-5">{lastName}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Email Address:</label>
                        <label className="text-muted fs-5">{email}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Gender:</label>
                        <label className="text-muted fs-5">{gender == 1 ? 'Male' : 'Female'}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Mobile No:</label>
                        <label className="text-muted fs-5">{mobileNo}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Address:</label>
                        <label className="text-muted fs-5">{address}</label>
                    </div>
                </div>

                <div className="text-center row mb-3">
                    <div className="col">
                        <label className="lead me-2 fs-4">Birthdate:</label>
                        <label className="text-muted fs-5">{birthDate}</label>
                    </div>
                </div>

                <section className="button-section pb-2 text-center">
                    <button onClick={this.back} type="button" className="btn btn-primary">Back</button>
                </section>

            </div>
        </>;
    }
}

export default UserInfo;
