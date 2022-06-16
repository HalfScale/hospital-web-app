import { Component } from 'react';
import { Link } from 'react-router-dom';
import AuthService from '../services/AuthService';
import { DEFAULT_PROFILE_IMG } from '../constants/GlobalConstants';
import CustomAxios from '../services/CustomAxios';
import { ROLE_DOCTOR } from '../constants/GlobalConstants';
import NotificationsService from '../services/NotificationsService';

class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loggedUser: AuthService.getUserFullName(),
            profileIcon: DEFAULT_PROFILE_IMG,
            notificationsCount: 0,
            path: ''
        }
    }

    componentDidMount() {
        console.log('componentDidMount location', this.props.location);
        if (AuthService.isLoggedIn()) {

            AuthService.fetchUserFromAPI().then(res => {
                let { firstName, lastName } = res.data;
                this.setState({ loggedUser: `${firstName} ${lastName}` });
                return CustomAxios.get('/api/file/user', { responseType: 'blob' });
            }).then(resp => {
                // console.log('image data', resp)
                let reader = new window.FileReader();
                reader.readAsDataURL(resp.data);
                reader.onload = () => {
                    let imageDataUrl = reader.result;
                    this.setState({
                        profileIcon: imageDataUrl,
                    });
                }
            });
        }
    }

    componentDidUpdate() {
        let { path } = this.state;
        let currentPath = this.props.location.pathname;

        if (path != currentPath) {
            NotificationsService.getUnviewedNotifications()
                .then(resp => {
                    console.log('getUnviewedNotifications resp', resp);
                    this.setState({
                        notificationsCount: resp.data,
                        path: currentPath
                    });
                });
        }

    }

    render() {
        let { notificationsCount } = this.state;
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
                                (AuthService.isLoggedIn() && AuthService.getUserRole() === ROLE_DOCTOR) && <li className="nav-item">
                                    <Link to="/hospital_rooms" className="nav-link">Rooms</Link>
                                </li>
                            }
                            {
                                (AuthService.isLoggedIn() && AuthService.getUserRole() === ROLE_DOCTOR) && <li className="nav-item">
                                    <Link to="/reservations" className="nav-link">Reservations</Link>
                                </li>
                            }
                            {
                                AuthService.isLoggedIn() && <li className="nav-item">
                                    <Link to="/appointment" className="nav-link">Appointments</Link>
                                </li>
                            }
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
                                {
                                    notificationsCount > 0 && <span className="position-absolute top-0 start-0 translate-middle badge rounded-pill bg-danger">
                                        {notificationsCount}
                                    </span>
                                }
                                <span className="pe-1">{this.state.loggedUser}</span>
                            </a>
                            <ul className="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                                <li>
                                    <Link to="/user/profile" className="dropdown-item">Profile</Link>
                                </li>
                                <li>
                                    <Link to="/messages" className="dropdown-item">Messages</Link>
                                </li>
                                <li>
                                    <Link to="/notifications" className="dropdown-item">Notifications</Link>
                                </li>
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
