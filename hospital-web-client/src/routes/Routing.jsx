import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import NavBar from '../components/NavBar';
import Home from '../components/Home'
import Registration from '../components/Registration';
import ConfirmRegistration from '../components/Registration/ConfirmRegistration';
import Login from '../components/Login';
import withNavigation from '../utils/WithNavigation';
import withLocationState from '../utils/WithLocationState';
import ProtectedRoute from './ProtectedRoute';

function Routing() {
    const RegistrationWithHooks = withNavigation(withLocationState(Registration));
    const ConfirmRegistrationWithHooks = withNavigation(withLocationState(ConfirmRegistration));
    const ProtectedRouteWithHooks = withLocationState(ProtectedRoute);
    return (
        <>
            <Router>
                <NavBar />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/registration" element={<RegistrationWithHooks />} />
                    <Route path="/registration/confirm" element={
                        <ProtectedRouteWithHooks redirectTo='/registration'>
                            <ConfirmRegistrationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/login" element={<Login />} />
                </Routes>
            </Router>
        </>
    );
}

export default Routing;