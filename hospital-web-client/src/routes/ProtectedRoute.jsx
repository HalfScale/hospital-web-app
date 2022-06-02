import { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/AuthService";


class ProtectedRoute extends Component {
    constructor(props) {
        super(props);
        console.log('ProtectedRoute interceptedData', props.location.state);
    }
    render() {
        let { location, role, hasAuth, hasState, hasRole } = this.props;

        // unauth routes
        // unauth routes but with states
        // auth routes
        // auth routes with roles
        // auth routes with roles and states

        if (!hasAuth && hasState) {

            if (location.state) {
                return { ...this.props.children };
            }
        }

        if (hasAuth && AuthService.isLoggedIn()) {

            console.log('hasAuth && isLoggedIn');
            
            if (!hasState) {
                console.log('No state');
                if (hasRole) {
                    if (role === AuthService.getUserRole()) {
                        return { ...this.props.children };
                    }

                    return <Navigate to={this.props.redirectTo} />;
                }
            }

            if (hasState && location.state) {
                console.log('hasState && State Object');
                if (hasRole) {

                    if (role === AuthService.getUserRole()) {
                        return { ...this.props.children };
                    }

                    console.log('returning to home');
                    return <Navigate to={this.props.redirectTo} />;
                }
            }else {
                return <Navigate to={this.props.redirectTo} />;
            }

            return { ...this.props.children };
        }

        return <Navigate to={this.props.redirectTo} />;
    }
}

export default ProtectedRoute;