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
        AuthService.getLoggedInUser()
            .then(resp => {
                console.log('resp profile', resp);
                let {firstName, lastName, users: {email}, mobileNo, address, birthDate, profileImage} = resp.data;
                this.setState({ data: {
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    mobileNo: mobileNo,
                    address: address,
                    birthDate: birthDate,
                    profileImage: profileImage ? buildProfileURL(profileImage) : DEFAULT_PROFILE_IMG
                } })
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
            profileImage = DEFAULT_PROFILE_IMG } = this.state.data ? this.state.data : {};

        return (
            <div className="mt-3 m-auto w-50 p-3 shadow rounded">
                <header className="profile-header text-center mb-4">
                    <h1 className="display-1">Hospital Name</h1>
                    <h2 className="text-muted">User Information</h2>
                </header>

                <div className="profile-image text-center">
                    <img src={profileImage} alt="mdo" width="140" height="140" className="me-3 rounded-circle shadow" />
                    {/* <div class="mt-4 input-group mb-3 w-50 mx-auto">
                        <input type="file" class="form-control" id="inputGroupFile02" />
                    </div> */}
                </div>

                <hr class="hr-text"></hr>

                <div className="profile-details text-center">

                    <div className="row mb-3">
                        <div className="col">
                            <label className="lead me-2 fs-4">First Name:</label>
                            <label className="text-muted fs-5">{firstName}</label>
                            {/* <div class="form-floating mb-3">
                                <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" />
                                <label for="floatingInput">Email address</label>
                            </div> */}
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

                    <div className="mb-3 row">
                        <div className="col">
                            <button onClick={e => this.props.navigate('/')} type="button" className="me-3 btn btn-primary">
                                Back
                            </button>

                            <button onClick={e => this.props.navigate('/user/profile/edit')} type="button" className="me-3 btn btn-primary">
                                Edit
                            </button>
                        </div>
                    </div>
                </div>

            </div>
        );
    }
}

export default Profile;
