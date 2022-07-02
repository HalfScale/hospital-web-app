import './styles/index.css';
import { Component } from 'react';
import DoctorsService from '../../services/DoctorsService';
import { DEFAULT_PROFILE_IMG, ROLE_DOCTOR } from '../../constants/GlobalConstants';
import { buildProfileURL } from '../../utils/Utils';
import AuthService from '../../services/AuthService';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope } from "@fortawesome/free-solid-svg-icons";

class DoctorDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.params.id,
            firstName: '',
            lastName: '',
            email: '',
            mobileNo: '',
            gender: '',
            birthDate: '',
            address: '',
            specialization: '',
            noOfYearsExperience: '',
            education: '',
            schedule: '',
            expertise: '',
            doctorCodeId: null,
            profileImage: DEFAULT_PROFILE_IMG
        }

        this.createAppointment = this.createAppointment.bind(this);
        this.messageDoctor = this.messageDoctor.bind(this);
    }

    createAppointment() {
        console.log('create appointment');
        if (!AuthService.isLoggedIn()) {
            this.props.navigate('/login');
        }

        this.props.navigate(`/appointment/create/${this.state.id}`);
    }

    messageDoctor() {
        if (!AuthService.isLoggedIn()) {
            this.props.navigate('/login');
        }

        this.props.navigate(`/message/send/${this.props.params.id}`);
    }

    componentDidMount() {
        DoctorsService.getDoctorDetails(this.props.params.id)
            .then(resp => {
                this.setState({
                    firstName: resp.data.firstName,
                    lastName: resp.data.lastName,
                    email: resp.data.users.email,
                    mobileNo: resp.data.mobileNo,
                    gender: resp.data.gender,
                    birthDate: resp.data.birthDate,
                    address: resp.data.address,
                    specialization: resp.data.specialization,
                    noOfYearsExperience: resp.data.noOfYearsExperience,
                    education: resp.data.education,
                    schedule: resp.data.schedule,
                    expertise: resp.data.expertise,
                    doctorCodeId: resp.data.doctorCodeId,
                    profileImage: resp.data.profileImage ? buildProfileURL(resp.data.profileImage) : DEFAULT_PROFILE_IMG
                });
            })
            .catch(error => console.log('error', error.response));
    }

    render() {
        let {
            firstName,
            lastName,
            email,
            mobileNo,
            gender,
            birthDate,
            address,
            specialization,
            noOfYearsExperience,
            education,
            schedule,
            expertise,
            doctorCodeId,
            profileImage
        } = this.state;

        return (
            <div className="mt-3 doctor-details-container p-3 shadow rounded">

                <header className="profile-header text-center mb-4">
                    <h1 className="display-1">Hospital Name</h1>
                    <h2 className="text-muted">Doctor Details</h2>
                </header>

                <div className="profile-image text-center">
                    <img src={profileImage} alt="profile-img" width="140" height="140" className="me-3 rounded-circle shadow" />
                    {
                        AuthService.getUserRole() !== ROLE_DOCTOR && <div className="mt-3">
                            <FontAwesomeIcon icon={faEnvelope} size="2xl" />
                            <span className="text-muted ms-2 fs-5">
                                <a role="button" onClick={this.messageDoctor} className="text-decoration-none">Message</a>
                            </span>
                        </div>
                    }
                </div>

                <hr className="hr-text"></hr>

                <div className="profile-details text-center">

                    <div className="row mb-3">
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">First Name:</label>
                            <label className="text-muted fs-5">{firstName}</label>
                        </div>
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Last Name:</label>
                            <label className="text-muted fs-5">{lastName}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Email:</label>
                            <label className="text-muted fs-5">{email}</label>
                        </div>
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Mobile:</label>
                            <label className="text-muted fs-5">{mobileNo}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Gender:</label>
                            <label className="text-muted fs-5">{gender == 1 ? 'Male' : 'Female'}</label>
                        </div>
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Birthdate:</label>
                            <label className="text-muted fs-5">{birthDate ? birthDate : 'N/A'}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col-sm-6">
                            <label className="lead me-2 fs-4">Address:</label>
                            <label className="text-muted fs-5">{address ? address : 'N/A'}</label>
                        </div>
                    </div>

                    {
                        doctorCodeId &&
                        <>
                            <hr className="hr-text"></hr>

                            <div className="row mb-3">
                                <div className="col-sm-6">
                                    <label className="lead me-2 fs-4">Specialization:</label>
                                    <label className="text-muted fs-5">{specialization}</label>
                                </div>
                                {
                                    noOfYearsExperience && <div className="col">
                                        <label className="lead me-2 fs-4">Years of experience:</label>
                                        <label className="text-muted fs-5">{noOfYearsExperience}</label>
                                    </div>
                                }
                            </div>

                            <div className="row mb-3">
                                {
                                    education && <div className="col-sm-6">
                                        <label className="lead me-2 fs-4">Education:</label>
                                        <label className="text-muted fs-5">{education}</label>
                                    </div>
                                }

                                {
                                    schedule && <div className="col-sm-6">
                                        <label className="lead me-2 fs-4">Schedule:</label>
                                        <label className="text-muted fs-5">{schedule}</label>
                                    </div>
                                }

                            </div>

                            {
                                expertise && <div className="row mb-3">
                                    <div className="col-sm-12">
                                        <label className="lead me-2 fs-4">Expertise:</label>
                                        <label className="text-muted fs-5">{expertise}</label>
                                    </div>
                                </div>
                            }

                        </>
                    }


                    <section className="button-section pb-2 text-center">
                        {
                            AuthService.getUserRole() !== ROLE_DOCTOR && <button onClick={this.createAppointment} type="button" className="me-3 btn btn-primary">
                                Create Appointment
                            </button>
                        }
                        <button onClick={e => this.props.navigate('/doctors')} type="button" className="me-3 btn btn-primary">
                            Back
                        </button>
                    </section>
                </div>
            </div>
        );
    }
}

export default DoctorDetails;
