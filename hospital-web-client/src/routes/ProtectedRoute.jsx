import { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/AuthService";


class ProtectedRoute extends Component {
    constructor(props) {
        super(props);
        console.log('ProtectedRoute interceptedData', props.location.state);
    }
    render() {
        let { location, role } = this.props;

        if (location.state || AuthService.isLoggedIn()) {

            if(role && role !== AuthService.getUserRole()) {

                return <Navigate to={this.props.redirectTo} />;

            }

            return { ...this.props.children };
        }

        return  <Navigate to={this.props.redirectTo} />;
    }
}

export default ProtectedRoute;