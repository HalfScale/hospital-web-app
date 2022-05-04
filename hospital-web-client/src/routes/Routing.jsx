import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { ROLE_DOCTOR } from "../constants/GlobalConstants";
import NavBar from '../components/NavBar';
import Home from '../components/Home'
import Registration from '../components/Registration';
import ConfirmRegistration from '../components/Registration/ConfirmRegistration';
import Login from '../components/Login';
import Logout from '../components/Logout';
import withNavigation from '../utils/WithNavigation';
import withLocationState from '../utils/WithLocationState';
import withParams from '../utils/WithParams';
import ProtectedRoute from './ProtectedRoute';
import SuccessfulRegistration from '../components/Registration/SuccessfulRegistration';
import Profile from '../components/Profile';
import ProfileEdit from '../components/Profile/ProfileEdit';
import Doctors from '../components/Doctors';
import DoctorDetails from '../components/Doctors/DoctorDetails';
import Message from '../components/Message/Message';
import MessageList from '../components/Message/MessageList';
import RoomList from '../components/HospitalRoom/RoomList';
import RoomDetails from '../components/HospitalRoom/RoomDetails';
import CreateRoom from '../components/HospitalRoom/CreateRoom';


function Routing() {
    const NavBarWithHooks = withLocationState(withNavigation(NavBar));
    const RegistrationWithHooks = withNavigation(withLocationState(Registration));
    const ConfirmRegistrationWithHooks = withNavigation(withLocationState(ConfirmRegistration));
    const ProtectedRouteWithHooks = withLocationState(ProtectedRoute);
    const SuccessfulRegistrationWithHooks = withNavigation(SuccessfulRegistration);
    const LoginWithHooks = withNavigation(Login);
    const ProfileWithHooks = withNavigation(Profile);
    const ProfileEditWithHooks = withNavigation(ProfileEdit);
    const DoctorsWithHooks = withNavigation(withLocationState(Doctors));
    const DoctorDetailsWithHooks = withParams(withNavigation(DoctorDetails));
    const MessageWithHooks = withParams(withNavigation(Message));
    const MessageListWithHooks = withNavigation(MessageList);
    const RoomListWithHooks = withNavigation(RoomList);
    const RoomDetailsWithHooks = withNavigation(withParams(RoomDetails));
    const CreateRoomWithHooks = withNavigation(CreateRoom);
    return (
        <>
            <Router>
                <NavBarWithHooks />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/user/profile" element={
                        <ProtectedRouteWithHooks redirectTo='/login'>
                            <ProfileWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/user/profile/edit" element={
                        <ProtectedRouteWithHooks redirectTo='/login'>
                            <ProfileEditWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/doctors" element={<DoctorsWithHooks />} />
                    <Route path="/doctors/details/:id" element={<DoctorDetailsWithHooks />} />
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
                    <Route path="/message/send/:id" element={
                        <ProtectedRouteWithHooks redirectTo='/login'>
                            <MessageWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/messages" element={
                        <ProtectedRouteWithHooks redirectTo='/login'>
                            <MessageListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms" element={
                        <ProtectedRouteWithHooks redirectTo='/' role={ROLE_DOCTOR}>
                            <RoomListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/add" element={
                        <ProtectedRouteWithHooks redirectTo='/' role={ROLE_DOCTOR}>
                            <CreateRoomWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/details/:id" element={
                        <ProtectedRouteWithHooks redirectTo='/' role={ROLE_DOCTOR}>
                            <RoomDetailsWithHooks />
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