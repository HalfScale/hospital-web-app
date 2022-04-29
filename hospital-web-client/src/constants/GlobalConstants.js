import { buildProfileURL } from "../utils/Utils";

const AUTHENTICATED_USER = 'authenticatedUser';
//local
const BASE_API_SERVER_URL = 'http://192.168.254.102:8080';
//asw
// const BASE_API_SERVER_URL = 'http://todoserviceapp-dev.eba-pfu9kq5c.ap-southeast-1.elasticbeanstalk.com';
const DEFAULT_PROFILE_IMG = buildProfileURL('default.png');

const ROLE_PATIENT = 'PATIENT';
const ROLE_DOCTOR = 'DOCTOR';

export {
    AUTHENTICATED_USER, 
    BASE_API_SERVER_URL, 
    DEFAULT_PROFILE_IMG,
    ROLE_DOCTOR,
    ROLE_PATIENT
};