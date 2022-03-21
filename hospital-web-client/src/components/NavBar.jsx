import { Component } from 'react';
import { Link } from 'react-router-dom';
import AuthService from '../services/AuthService';

class NavBar extends Component {
    constructor(props) {
        super(props);
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
                            {/* {
                                (isLoggedIn && decodedJWT.roles[0].authority === 'ROLES_ADMIN') && <li className="nav-item">
                                    <Link to="/dashboard" className="nav-link">Dashboard</Link>
                                </li>
                            }
                            {
                                isLoggedIn && <li className="nav-item">
                                    <Link to="/todos" className="nav-link">Todos</Link>
                                </li>
                            }
                            {
                                !isLoggedIn && <li className="nav-item">
                                    <Link to="/" className="nav-link">Login</Link>
                                </li>
                            }
                            {
                                isLoggedIn && <li className="nav-item">
                                    <Link to="/logout" className="nav-link" onClick={AuthenticationService.logout}>Logout</Link>
                                </li>
                            } */}

                        </ul>
                    </div>
                    {
                        AuthService.isLoggedIn() && <div class="dropdown text-end">
                            <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" class="me-3 rounded-circle" />
                                <span class="pe-1">{AuthService.getUserFullName()}</span>
                            </a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                                <li><a class="dropdown-item" href="#">Profile</a></li>
                                <li><a class="dropdown-item" href="#">Messages</a></li>
                                <li><a class="dropdown-item" href="#">Notifications</a></li>
                                <li><hr class="dropdown-divider" /></li>
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
