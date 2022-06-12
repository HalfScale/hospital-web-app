import { buildProfileURL } from "../utils/Utils";

const AUTHENTICATED_USER = 'authenticatedUser';
//local
const BASE_API_SERVER_URL = 'http://localhost:8080';
//asw
// const BASE_API_SERVER_URL = 'http://todoserviceapp-dev.eba-pfu9kq5c.ap-southeast-1.elasticbeanstalk.com';
const DEFAULT_PROFILE_IMG = buildProfileURL('default.png');

const ROLE_PATIENT = 'PATIENT';
const ROLE_DOCTOR = 'DOCTOR';

const RESERVATION_STATUS_CODE = {
    CREATED: '0',
    DONE: '2',
    CANCELLED: '1',
    ALL: '3'
}

const APPOINTMENT_STATUS = {
    PENDING: 1,
    APPROVED: 2,
    CANCELLED: 3,
    REJECT: 4
}

export {
    AUTHENTICATED_USER, 
    BASE_API_SERVER_URL, 
    DEFAULT_PROFILE_IMG,
    ROLE_DOCTOR,
    ROLE_PATIENT,
    RESERVATION_STATUS_CODE,
    APPOINTMENT_STATUS
};