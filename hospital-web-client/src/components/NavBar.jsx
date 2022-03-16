import { Component } from 'react';
import { Link } from 'react-router-dom';

class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            
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
                                <Link to="/registration" className="nav-link">Register</Link>
                            </li>
                            <li className="nav-item">
                                <Link to="/login" className="nav-link">Login</Link>
                            </li>
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
                </div>
            </nav>
        );
    }
}

export default NavBar;
