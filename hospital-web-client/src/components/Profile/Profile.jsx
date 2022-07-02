import { Component } from 'react';
import AuthService from '../../services/AuthService';
import { buildProfileURL } from '../../utils/Utils';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: null,
            status: 200,
            profileImage: DEFAULT_PROFILE_IMG
        }
    }

    componentDidMount() {
        AuthService.fetchUserFromAPI()
            .then(resp => {

                let { firstName, lastName, users: { email }, mobileNo,
                    address, birthDate, profileImage, doctorCodeId, specialization,
                    noOfYearsExperience, education, schedule, expertise } = resp.data;

                this.setState({
                    data: {
                        firstName: firstName,
                        lastName: lastName,
                        email: email,
                        mobileNo: mobileNo,
                        address: address,
                        birthDate: birthDate,
                        profileImage: profileImage ? buildProfileURL(profileImage) : DEFAULT_PROFILE_IMG,
                        doctorCodeId: doctorCodeId,
                        specialization: specialization,
                        noOfYearsExperience: noOfYearsExperience,
                        education: education,
                        schedule: schedule,
                        expertise: expertise
                    }
                })
            })
            .catch(err => {
                if (err.response.status === 401) {
                    AuthService.logout();
                    this.props.navigate('/');
                }
            });
    }

    render() {
        let { firstName = '',
            lastName = '',
            email = '',
            mobileNo = '',
            gender = '',
            birthDate = '',
            address = '',
            profileImage = DEFAULT_PROFILE_IMG,
            doctorCodeId = null,
            specialization = '',
            noOfYearsExperience = '',
            education = '',
            schedule = '',
            expertise = '' } = this.state.data ? this.state.data : {};

        return (
            <div className="mt-3 common-container p-3 shadow rounded">

                <header className="profile-header text-center mb-4">
                    <h1 className="display-1">Hospital Name</h1>
                    <h2 className="text-muted">User Information</h2>
                </header>

                <div className="profile-image text-center">
                    <img src={profileImage} alt="mdo" width="140" height="140" className="me-3 rounded-circle shadow" />
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
                        <div className="col-sm-12">
                            <label className="lead me-2 fs-4">Address:</label>
                            <label className="text-muted fs-5">{address ? address : 'N/A'}</label>
                        </div>
                    </div>

                    {
                        doctorCodeId && <>
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


                    <section className="button-section pb-2 text-center">
                        <button onClick={e => this.props.navigate('/user/profile/edit')} type="button" className="me-3 btn btn-primary">
                            Edit
                        </button>

                        <button onClick={e => this.props.navigate('/')} type="button" className="me-3 btn btn-primary">
                            Back
                        </button>
                    </section>
                </div>

            </div>
        );
    }
}

export default Profile;
