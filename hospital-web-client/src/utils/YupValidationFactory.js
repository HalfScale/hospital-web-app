import * as Yup from 'yup';
import moment from 'moment';
import AuthService from '../services/AuthService';
import HospitalRoomService from '../services/HospitalRoomService';
import AppointmentService from '../services/AppointmentService';
import { isFileImage } from './Utils';

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
                .test('is-file-size-valid', 'File too big', (file) => {
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
                .test('is-file-valid', 'Incorrect file format', (file) => {
                    let valid = true;
                    if (file) {
                        if (!isFileImage(file.type)) {
                            valid = false;
                        }
                    }
                    return valid;
                }),
            noOfYearsExperience: Yup.string().matches(/^[0-9]*$/, 'Invalid years of experience.')
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
                .max(8, 'Exceeds maximum character of 8')
                .required('Required!')
                .test(
                    'room-code-validation',
                    'Invalid room code',
                    async(roomCode, validationContext) => {

                        if (validationContext.parent.roomId && (roomCode && roomCode.trim().length > 0)) {
                            return await HospitalRoomService.validateRoom({
                                    roomId: validationContext.parent.roomId,
                                    roomCode: roomCode.trim()
                                }).then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }

                        if (roomCode && roomCode.trim().length > 0) {
                            return await HospitalRoomService.validateRoom({ roomCode: roomCode.trim() })
                                .then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }
                        return true;
                    }
                ),
            roomName: Yup.string()
                .max(150, 'Exceeds maximum character of 150')
                .required('Required')
                .test(
                    'room-code-validation',
                    'Invalid room name',
                    async(roomName, validationContext) => {
                        console.log('value', validationContext);
                        if (validationContext.parent.roomId && (roomName && roomName.trim().length > 0)) {
                            return await HospitalRoomService.validateRoom({
                                    roomName: roomName.trim(),
                                    roomId: validationContext.parent.roomId
                                }).then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }
                        if (roomName && roomName.trim().length > 0) {
                            return await HospitalRoomService.validateRoom({ roomName: roomName.trim() })
                                .then(res => res.status === 204)
                                .catch(err => { console.log('err', err) });
                        }
                        return true;
                    }
                ),
            description: Yup.string()
                .max(250, 'Exceeds maximum character of 250')
                .required('Required!'),
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
                .test('is-file-valid', 'Incorrect file format', (file) => {
                    let valid = true;
                    if (file) {
                        if (!isFileImage(file.type)) {
                            valid = false;
                        }
                    }
                    return valid;
                })
        });
    }

    if (schemaType === 'roomReservation') {
        return Yup.object().shape({
            hasAssociated: Yup.boolean(),
            associatedId: Yup.string().when('hasAssociated', {
                is: true,
                then: Yup.string().required('Associated ID is required!')
            }).test(
                'hospital-room-validation',
                'Invalid appointment id!',
                async(associatedId) => {
                    if (associatedId && associatedId.trim().length > 0) {
                        return await AppointmentService.findById(associatedId)
                            .then(res => res.status === 200)
                            .catch(err => { console.log('err', err) });
                    }
                    return true;
                }
            ),
            startDate: Yup.date().required('Start date is required!'),
            endDate: Yup.date().required('End date is required!').min(Yup.ref('startDate'), `End date can't be before start date!`)
        }).test({
            name: 'invalidTime',
            test: function(value) {
                let start = moment(value.startDate).format('YYYY-MM-DD');
                let end = moment(value.endDate).format('YYYY-MM-DD');

                let { startHour, startMinute, startTimePeriod, endHour, endMinute, endTimePeriod } = value;

                const startDateMoment = moment(`${start} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD hh:mm a');
                const endDateMoment = moment(`${end} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD hh:mm a');
                if (startDateMoment.isSame(endDateMoment) || startDateMoment.isBefore(endDateMoment)) {
                    return true;
                }

                return this.createError({
                    path: 'endDate',
                    message: 'Invalid time range!'
                });
            }
        });
    }

    if (schemaType === 'appointment') {
        return Yup.object().shape({
            address: Yup.string().nullable().required('Address is required!'),
            reasonForAppointment: Yup.string().required('Reason is required!')
                .max(250, 'Maximum of 250 characters!'),
            startDate: Yup.date().required('Start date is required!'),
            endDate: Yup.date().required('End date is required!').min(Yup.ref('startDate'), `End date can't be before start date!`)
        }).test({
            name: 'invalidTime',
            test: function(value) {
                const today = moment();
                const start = moment(value.startDate).format('YYYY-MM-DD');
                const end = moment(value.endDate).format('YYYY-MM-DD');

                let { startHour, startMinute, startTimePeriod, endHour, endMinute, endTimePeriod } = value;

                const startDateMoment = moment(`${start} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD hh:mm a');
                const endDateMoment = moment(`${end} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD hh:mm a');

                if ((startDateMoment.isSame(today) || startDateMoment.isAfter(today)) &&
                    (endDateMoment.isSame(today) || endDateMoment.isAfter(today)) &&
                    (startDateMoment.isSame(endDateMoment) || startDateMoment.isBefore(endDateMoment))) {

                    return true;
                }

                return this.createError({
                    path: 'endDate',
                    message: 'Invalid date/time range!'
                });
            }
        });;
    }
    if (schemaType === 'appointmentEdit') {
        return Yup.object().shape({
            startDate: Yup.date().required('Start date is required!'),
            endDate: Yup.date().required('End date is required!').min(Yup.ref('startDate'), `End date can't be before start date!`)
        }).test({
            name: 'invalidTime',
            test: function(value) {
                const today = moment();
                const start = moment(value.startDate).format('YYYY-MM-DD');
                const end = moment(value.endDate).format('YYYY-MM-DD');

                let { startHour, startMinute, startTimePeriod, endHour, endMinute, endTimePeriod } = value;

                const startDateMoment = moment(`${start} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD hh:mm a');
                const endDateMoment = moment(`${end} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD hh:mm a');

                if ((startDateMoment.isSame(today) || startDateMoment.isAfter(today)) &&
                    (endDateMoment.isSame(today) || endDateMoment.isAfter(today)) &&
                    (startDateMoment.isSame(endDateMoment) || startDateMoment.isBefore(endDateMoment))) {

                    return true;
                }

                return this.createError({
                    path: 'endDate',
                    message: 'Invalid date/time range!'
                });
            }
        });;
    }

    if (schemaType === 'message') {
        return Yup.object().shape({
            message: Yup.string()
                .max(250, 'Exceeds maximum characters of 250!')
                .required('Required!')
        });
    }

    return null;
}