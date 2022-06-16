import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { ROLE_DOCTOR, ROLE_PATIENT } from "../constants/GlobalConstants";
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
import PreviewRoom from '../components/HospitalRoom/PreviewRoom';
import EditRoom from '../components/HospitalRoom/EditRoom';
import ReservationList from '../components/RoomReservation/ReservationList';
import ReservationDetails from '../components/RoomReservation/ReservationDetails';
import CreateReservation from '../components/RoomReservation/CreateReservation';
import ConfirmReservation from '../components/RoomReservation/ConfirmReservation';
import UpdateReservation from '../components/RoomReservation/UpdateReservation';
import CreateAppointment from '../components/Appointment/CreateAppointment';
import ConfirmAppointment from '../components/Appointment/ConfirmAppointment';
import EditAppointment from '../components/Appointment/EditAppointment';
import AppointmentDetails from '../components/Appointment/AppointmentDetails';
import AppointmentList from '../components/Appointment/AppointmentList';
import AppointmentComplete from '../components/Appointment/AppointmentComplete';
import UserInfo from '../components/Profile/UserInfo';
import Notifications from '../components/Notifications';

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
    const RoomListWithHooks = withNavigation(withLocationState(RoomList));
    const RoomDetailsWithHooks = withNavigation(withParams(RoomDetails));
    const CreateRoomWithHooks = withNavigation(withLocationState(CreateRoom));
    const PreviewRoomWithHooks = withNavigation(withLocationState(PreviewRoom));
    const EditRoomWithHooks = withParams(withNavigation(withLocationState(EditRoom)));
    const ReservationListWithHooks = withLocationState(withNavigation(ReservationList));
    const ReservationDetailsWithHooks = withLocationState(withParams(withNavigation(ReservationDetails)));
    const CreateReservationWithHooks = withLocationState(withNavigation(withParams(CreateReservation)));
    const ConfirmReservationWithHooks = withNavigation(withLocationState(ConfirmReservation));
    const UpdateReservationWithHooks = withParams(withNavigation(UpdateReservation));
    const CreateAppointmentWithHooks = withNavigation(withParams(withLocationState(CreateAppointment)));
    const ConfrimAppointmentWithHooks = withNavigation(withLocationState(ConfirmAppointment));
    const AppointmentListWithHooks = withNavigation(withLocationState(AppointmentList));
    const AppointmentCompleteWithHooks = withNavigation(withLocationState(AppointmentComplete))
    const EditAppointmentWithHooks = withNavigation(withLocationState(withParams(EditAppointment)));
    const AppointmentDetailsWithHooks = withNavigation(withParams(AppointmentDetails));
    const UserInfoWithHooks = withNavigation(withParams(UserInfo));
    const NotificationsWithHooks = withNavigation(withParams(Notifications));

    return (
        <>
            <Router>
                <NavBarWithHooks />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/user/info/:id" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <UserInfoWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/user/profile" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <ProfileWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/user/profile/edit" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <ProfileEditWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/doctors" element={<DoctorsWithHooks />} />
                    <Route path="/doctors/details/:id" element={<DoctorDetailsWithHooks />} />
                    <Route path="/registration" element={<RegistrationWithHooks />} />
                    <Route path="/registration/confirm" element={
                        <ProtectedRouteWithHooks hasState={true} redirectTo='/registration'>
                            <ConfirmRegistrationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/registration/success" element={
                        <ProtectedRouteWithHooks hasState={true} redirectTo='/registration'>
                            <SuccessfulRegistrationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/message/send/:id" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <MessageWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/messages" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <MessageListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/notifications" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/login'>
                            <NotificationsWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <RoomListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/preview" element={
                        <ProtectedRouteWithHooks hasState={true} hasAuth={true} hasRole={true}
                            role={ROLE_DOCTOR} redirectTo='/hospital_rooms/add'>
                            <PreviewRoomWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/add" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <CreateRoomWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/edit/:id" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <EditRoomWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/hospital_rooms/details/:id" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <RoomDetailsWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/reservations" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <ReservationListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/reservations/create/:roomId" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <CreateReservationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/reservations/create/confirm" element={
                        <ProtectedRouteWithHooks hasState={true} hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <ConfirmReservationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/reservations/edit/:reservationId" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <UpdateReservationWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/reservations/details/:id" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_DOCTOR} redirectTo='/'>
                            <ReservationDetailsWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/'>
                            <AppointmentListWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment/create/:doctorId" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasRole={true} role={ROLE_PATIENT} redirectTo='/'>
                            <CreateAppointmentWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment/edit/:appointmentId" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/'>
                            <EditAppointmentWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment/details/:appointmentId" element={
                        <ProtectedRouteWithHooks hasAuth={true} redirectTo='/'>
                            <AppointmentDetailsWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment/create/confirm" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasState={true} redirectTo='/'>
                            <ConfrimAppointmentWithHooks />
                        </ProtectedRouteWithHooks>
                    } />
                    <Route path="/appointment/create/complete" element={
                        <ProtectedRouteWithHooks hasAuth={true} hasState={true} redirectTo='/'>
                            <AppointmentCompleteWithHooks />
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