import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import NavBar from '../components/NavBar';
import Home from '../components/Home'
import Registration from '../components/Registration';
import ConfirmRegistration from '../components/Registration/ConfirmRegistration';
import Login from '../components/Login';
import Logout from '../components/Logout';
import withNavigation from '../utils/WithNavigation';
import withLocationState from '../utils/WithLocationState';
import ProtectedRoute from './ProtectedRoute';
import SuccessfulRegistration from '../components/Registration/SuccessfulRegistration';

function Routing() {
    const NavBarWithHooks = withNavigation(NavBar);
    const RegistrationWithHooks = withNavigation(withLocationState(Registration));
    const ConfirmRegistrationWithHooks = withNavigation(withLocationState(ConfirmRegistration));
    const ProtectedRouteWithHooks = withLocationState(ProtectedRoute);
    const SuccessfulRegistrationWithHooks = withNavigation(SuccessfulRegistration);
    const LoginWithHooks = withNavigation(Login);
    return (
        <>
            <Router>
                <NavBarWithHooks />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/registration" element={<RegistrationWithHooks />} />
                    <Route path="/registration/confirm" element={
                        <ProtectedRouteWithHooks redirectTo='/registration'>
                            <ConfirmRegistrationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/registration/success" element={
                        <ProtectedRouteWithHooks redirectTo='/registration'>
                            <SuccessfulRegistrationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/login" element={<LoginWithHooks />} />
                    <Route path="/logout" element={<Logout />} />
                </Routes>
            </Router>
        </>
    );
}

export default Routing;