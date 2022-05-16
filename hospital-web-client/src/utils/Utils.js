import {BASE_API_SERVER_URL} from '../constants/GlobalConstants'

function buildProfileURL(imageHash) {
    return `${BASE_API_SERVER_URL}/api/file/img/profile/${imageHash}`
}

function buildRoomImageURL(imageHash) {
    return `${BASE_API_SERVER_URL}/api/file/img/hospital_room/${imageHash}`
}
export { buildProfileURL, buildRoomImageURL };