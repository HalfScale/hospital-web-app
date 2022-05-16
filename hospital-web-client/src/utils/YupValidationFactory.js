import * as Yup from 'yup';
import AuthService from '../services/AuthService';
import HospitalRoomService from '../services/HospitalRoomService';

export default function getYupValidation(schemaType) {
    if (schemaType === 'profileEdit') {

        return Yup.object().shape({
            firstName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            lastName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            mobileNo: Yup.string().min(11, 'Enter a correct mobile number')
                .max(11, 'Enter a correct mobile number').required('Required!')
                .matches(/^09[0-9]+$/, 'Mobile number format is incorrect'),
            birthDate: Yup.string().notRequired().nullable(),
            address: Yup.string().notRequired().nullable(),
            image: Yup.mixed().nullable()
                .test('is-correct-file', 'File too big', (file) => {
                    console.log('file', file);
                    let valid = true;
                    if (file) {
                        const size = file.size / 1024 / 1024
                        if (size > 10) {
                            valid = false
                        }

                    }
                    return valid
                })
        });
    }

    if (schemaType === 'registration') {

        return Yup.object().shape({
            firstName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            lastName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            email: Yup.string().email('Invalid email format').required('Required!')
                .test(
                    'email-backend-validation',
                    'Email is already used',
                    async(email) => {
                        return await AuthService.checkIfEmailIsValid(email)
                            .then(res => res.status !== 400)
                            .catch(err => { console.log('err', err) });
                    }
                ),
            gender: Yup.string().required('Required!'),
            mobileNo: Yup.string().matches(/^09[0-9]+$/, 'Invalid mobile no.')
                .min(11, 'Mobile no. should be 11-13 digits').max(13, 'Mobile no. should be 11-13 digits')
                .required('Required!'),
            hospitalCode: Yup.string()
                .notRequired()
                .nullable()
                .test(
                    'hospital-code-validation',
                    'Invalid doctor code',
                    async(code) => {
                        console.log('code', code);
                        if (code && code.trim().length > 0) {
                            return await AuthService.checkIfDoctorCodeIsValid(code.trim())
                                .then(res => res.status !== 400)
                                .catch(err => { console.log('err', err) });
                        }
                        return true;
                    }
                ),
            password: Yup.string().min(6, 'Password should be 6-15 characters!').max(15, 'Password should be 6-15 characters!').required('Required'),
            confirmPassword: Yup.string().min(6, 'Password should be 6-15 characters!').max(15, 'Password should be 6-15 characters!').required('Required')
                .oneOf([Yup.ref('password'), null], "Password dont match!"),
            termsOfAgreement: Yup.boolean().oneOf([true], 'Must Accept Terms of Agreement')
        });
    }

    if (schemaType === 'login') {
        return Yup.object().shape({
            email: Yup.string()
                .email('Must be a valid email!')
                .required('Required!'),
            password: Yup.string()
                .required('Required!')
        });
    }

    if (schemaType === 'room') {
        return Yup.object().shape({
            roomCode: Yup.string()
                .required('Required!')
                .test(
                    'room-code-validation',
                    'Invalid room code',
                    async(roomCode) => {
                        console.log('roomCode', roomCode);
                        if (roomCode && roomCode.trim().length > 0) {
                            return await HospitalRoomService.validateRoom({ roomCode: roomCode.trim() })
                                .then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }
                        return true;
                    }
                ),
            roomName: Yup.string()
                .required('Required')
                .test(
                    'room-code-validation',
                    'Invalid room name',
                    async(roomName) => {
                        console.log('roomName', roomName);
                        if (roomName && roomName.trim().length > 0) {
                            return await HospitalRoomService.validateRoom({ roomName: roomName.trim() })
                                .then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }
                        return true;
                    }
                ),
            description: Yup.string().required('Required!'),
            image: Yup.mixed().nullable()
                .test('is-correct-file', 'File is too big!', (file) => {
                    console.log('file', file);
                    let valid = true;
                    if (file) {
                        const size = file.size / 1024 / 1024
                        if (size > 10) {
                            valid = false
                        }

                    }
                    return valid;
                })
        });
    }

    return null;
}