import { Component } from 'react';
import DoctorsService from '../../services/DoctorsService';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';
import { buildProfileURL } from '../../utils/Utils';
import AuthService from '../../services/AuthService';

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
    }

    createAppointment() {
        console.log('create appointment');
        if (!AuthService.isLoggedIn()) {
            this.props.navigate('/login');
        }
    }

    componentDidMount() {
        DoctorsService.getDoctorDetails(this.props.params.id)
            .then(resp => {
                console.log('resp', resp)
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
                    profileImage: resp.data.profileImage ? buildProfileURL(resp.data.profileImage): DEFAULT_PROFILE_IMG
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
            <div className="mt-3 m-auto w-50 p-3 shadow rounded">

                <header className="profile-header text-center mb-4">
                    <h1 className="display-1">Hospital Name</h1>
                    <h2 className="text-muted">Doctor Details</h2>
                </header>

                <div className="profile-image text-center">
                    <img src={profileImage} alt="profile-img" width="140" height="140" className="me-3 rounded-circle shadow" />
                </div>

                <hr className="hr-text"></hr>

                <div className="profile-details text-center">

                    <div className="row mb-3">
                        <div className="col">
                            <label className="lead me-2 fs-4">First Name:</label>
                            <label className="text-muted fs-5">{firstName}</label>
                        </div>
                        <div className="col">
                            <label className="lead me-2 fs-4">Last Name:</label>
                            <label className="text-muted fs-5">{lastName}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col">
                            <label className="lead me-2 fs-4">Email:</label>
                            <label className="text-muted fs-5">{email}</label>
                        </div>
                        <div className="col">
                            <label className="lead me-2 fs-4">Mobile:</label>
                            <label className="text-muted fs-5">{mobileNo}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col">
                            <label className="lead me-2 fs-4">Gender:</label>
                            <label className="text-muted fs-5">{gender == 1 ? 'Male' : 'Female'}</label>
                        </div>
                        <div className="col">
                            <label className="lead me-2 fs-4">Birthdate:</label>
                            <label className="text-muted fs-5">{birthDate}</label>
                        </div>
                    </div>

                    <div className="row mb-3">
                        <div className="col">
                            <label className="lead me-2 fs-4">Address:</label>
                            <label className="text-muted fs-5">{address}</label>
                        </div>
                    </div>

                    {
                        doctorCodeId &&
                        <>
                            <hr className="hr-text"></hr>

                            <div className="row mb-3">
                                <div className="col">
                                    <label className="lead me-2 fs-4">Specialization:</label>
                                    <label className="text-muted fs-5">{specialization}</label>
                                </div>

                                <div className="col">
                                    <label className="lead me-2 fs-4">Years of experience:</label>
                                    <label className="text-muted fs-5">{noOfYearsExperience}</label>
                                </div>
                            </div>

                            <div className="row mb-3">
                                <div className="col">
                                    <label className="lead me-2 fs-4">Education:</label>
                                    <label className="text-muted fs-5">{education}</label>
                                </div>

                                <div className="col">
                                    <label className="lead me-2 fs-4">Schedule:</label>
                                    <label className="text-muted fs-5">{schedule}</label>
                                </div>
                            </div>

                            <div className="row mb-3">
                                <div className="col">
                                    <label className="lead me-2 fs-4">Expertise:</label>
                                    <label className="text-muted fs-5">{expertise}</label>
                                </div>
                            </div>
                        </>
                    }


                    <div className="mb-3 row">
                        <div className="col">
                            <button onClick={e => this.props.navigate('/doctors')} type="button" className="me-3 btn btn-primary">
                                Back
                            </button>

                            <button onClick={this.createAppointment} type="button" className="me-3 btn btn-primary">
                                Create Appointment
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default DoctorDetails;
