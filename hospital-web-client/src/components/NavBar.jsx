import { Component } from 'react';
import { Link } from 'react-router-dom';
import AuthService from '../services/AuthService';
import { DEFAULT_PROFILE_IMG } from '../constants/GlobalConstants';
import CustomAxios from '../services/CustomAxios';

class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loggedUser: AuthService.getUserFullName(),
            profileIcon: DEFAULT_PROFILE_IMG
        }
    }

    componentDidMount() {
        if (AuthService.isLoggedIn()) {

            AuthService.fetchUserFromAPI().then(res => {
                let { firstName, lastName } = res.data;
                this.setState({ loggedUser: `${firstName} ${lastName}` });
                return CustomAxios.get('/api/file/user', { responseType: 'blob' });
            }).then(resp => {
                console.log('image data', resp)
                let reader = new window.FileReader();
                reader.readAsDataURL(resp.data);
                reader.onload = () => {
                    let imageDataUrl = reader.result;
                    this.setState({
                        profileIcon: imageDataUrl
                    });
                }
            });
        }
    }

    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container-fluid">

                    <a className="navbar-brand" href="#">Hospital App</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>

                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <Link to="/" className="nav-link">Home</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="/doctors" className="nav-link">Doctors</Link>
                            </li>
                            {
                                !AuthService.isLoggedIn() && <li className="nav-item">
                                    <Link to="/registration" className="nav-link">Register</Link>
                                </li>
                            }

                            {
                                !AuthService.isLoggedIn() && <li className="nav-item">
                                    <Link to="/login" className="nav-link">Login</Link>
                                </li>
                            }
                        </ul>
                    </div>
                    {
                        AuthService.isLoggedIn() && <div className="dropdown text-end">
                            <a href="#" className="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src={this.state.profileIcon} alt="mdo" width="32" height="32" className="me-3 rounded-circle" />
                                <span className="pe-1">{this.state.loggedUser}</span>
                            </a>
                            <ul className="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                                <li>
                                    <Link to="/user/profile" className="dropdown-item">Profile</Link>
                                </li>
                                <li><a className="dropdown-item" href="#">Messages</a></li>
                                <li><a className="dropdown-item" href="#">Notifications</a></li>
                                <li><hr className="dropdown-divider" /></li>
                                <li>
                                    <Link onClick={AuthService.logout} to="/logout" className="dropdown-item">Sign out</Link>
                                </li>
                            </ul>
                        </div>
                    }

                </div>
            </nav>
        );
    }
}

export default NavBar;
