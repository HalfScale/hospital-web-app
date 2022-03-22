import { Component } from 'react';
import AuthService from '../../services/AuthService';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: null,
            status: 200
        }
    }

    componentDidMount() {
        AuthService.getLoggedInUser()
            .then(resp => {
                console.log('resp', resp);
                this.setState({ data: resp.data })
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
            users: { email } = {},
            mobileNo = '',
            gender = '',
            birthDate = '',
            address = '' } = this.state.data ? this.state.data : {};

        return (
            <div className="mt-3 m-auto w-50 p-3 shadow rounded">
                <header className="profile-header text-center mb-4">
                    <h1 className="display-1">Hospital Name</h1>
                    <h2 className="text-muted">User Information</h2>
                </header>

                <div className="profile-image text-center">
                    <img src="https://github.com/mdo.png" alt="mdo" width="140" height="140" className="me-3 rounded-circle shadow" />
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
                            <button type="button" className="me-3 btn btn-primary">
                                Back
                            </button>

                            <button onClick={() => {
                                AuthService.getLoggedInUser().then(resp => console.log('resp', resp))
                                    .catch(error => console.log('error', error));
                            }
                            } type="button" className="me-3 btn btn-primary">
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
