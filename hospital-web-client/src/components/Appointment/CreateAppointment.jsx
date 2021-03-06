import './styles/main.css';
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import { Modal, Button } from 'react-bootstrap'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import moment from 'moment';
import HospitalHeader from '../HospitalHeader';
import AuthService from '../../services/AuthService';
import AppointmentService from '../../services/AppointmentService';
import DoctorsService from '../../services/DoctorsService';
import getYupValidation from '../../utils/YupValidationFactory';
import ReactPaginate from 'react-paginate';
import DoctorAppointmentTableRow from './DoctorAppointmentTableRow';

class CreateAppointment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            doctorId: props.params.doctorId,
            userHasAddress: false,
            firstName: '',
            lastName: '',
            gender: '',
            address: '',
            email: '',
            mobileNo: '',
            firstTime: 'true',
            startDate: '',
            endDate: '',
            startHour: '01',
            startMinute: '00',
            startTimePeriod: 'am',
            endHour: '01',
            endMinute: '00',
            endTimePeriod: 'pm',
            reasonForAppointment: '',
            page: 0,
            size: 5,
            sort: 'id,asc',
            totalPages: 0,
            appointments: []
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.checkDoctorAppointments = this.checkDoctorAppointments.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.showAppointmentModal = this.showAppointmentModal.bind(this);
        this.displayAppointmentRows = this.displayAppointmentRows.bind(this);
        this.formatDate = this.formatDate.bind(this);
        this.back = this.back.bind(this);
    }

    componentDidMount() {
        if (this.props.location.state) {
            let { firstName, lastName, address, gender, firstTime, email, mobileNo,
                startDate, startHour, startMinute, startTimePeriod, endDate, endHour, endMinute,
                endTimePeriod, reasonForAppointment } = this.props.location.state;
            this.setState({
                firstName: firstName,
                lastName: lastName,
                gender: gender,
                firstTime: firstTime,
                address: address ? address : '',
                email: email,
                mobileNo: mobileNo,
                startDate: startDate,
                startHour: startHour,
                startMinute: startMinute,
                startTimePeriod: startTimePeriod,
                endDate: endDate,
                endHour: endHour,
                endMinute: endMinute,
                endTimePeriod: endTimePeriod,
                reasonForAppointment: reasonForAppointment
            });
        } else {
            AuthService.fetchUserFromAPI()
                .then(resp => {
                    let { firstName, lastName, address, gender, users: { email }, mobileNo } = resp.data;
                    this.setState({
                        firstName: firstName,
                        lastName: lastName,
                        gender: gender,
                        address: address ? address : '',
                        userHasAddress: address ? true : false,
                        email: email,
                        mobileNo: mobileNo
                    });

                    return DoctorsService.getDoctorDetails(this.state.doctorId);
                })
                .then(resp => {
                }).catch(error => this.props.navigate('/'));
        }
    }

    onSubmit(values) {
        let { doctorId, firstName, lastName, gender, email, mobileNo } = this.state;
        let { startDate, startHour, startMinute, startTimePeriod,
            endDate, endHour, endMinute, endTimePeriod } = values;

        //additional fields to display on page confirmation
        values.doctorId = doctorId;
        values.firstName = firstName;
        values.lastName = lastName;
        values.gender = gender;
        values.email = email;
        values.mobileNo = mobileNo;

        const formattedStartDate = moment(`${startDate} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD h:mm a').format('YYYY-MM-DD HH:mm:ss');
        const formattedEndDate = moment(`${endDate} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD h:mm a').format('YYYY-MM-DD HH:mm:ss');
        let { page, size, sort } = this.state;
        AppointmentService.findDoctorsAppointment(this.state.doctorId, {
            page: page,
            size: size,
            sort: sort,
            startDate: formattedStartDate,
            endDate: formattedEndDate
        }).then(resp => {
            if (resp.data.content.length === 0) {
                this.props.navigate('/appointment/create/confirm', { state: values });
            } else {
                toast.error('Appointment dates are used!');
            }
        });

    }

    checkDoctorAppointments({ values, validateForm, setErrors, setFieldTouched }) {
        let { address, reasonForAppointment, startDate, startHour, startMinute, startTimePeriod,
            endDate, endHour, endMinute, endTimePeriod } = values;

        validateForm().then(val => {
            if (Object.keys(val).length !== 0) {
                Object.keys(val).forEach(key => {
                    setFieldTouched(key, true, true);
                });
                setErrors(val);
            } else {
                this.setState({
                    address: address,
                    startTimePeriod: startTimePeriod,
                    reasonForAppointment: 
                    reasonForAppointment, 
                    startDate: startDate, 
                    startHour: startHour, 
                    startMinute: startMinute, 
                    endDate: endDate, 
                    endHour: endHour, 
                    endMinute: endMinute, 
                    endTimePeriod: endTimePeriod
                });
                const formattedStartDate = this.formatDate(values, 'YYYY-MM-DD HH:mm:ss').startDate;
                const formattedEndDate = this.formatDate(values, 'YYYY-MM-DD HH:mm:ss').endDate;
                let { page, size, sort } = this.state;
                AppointmentService.findDoctorsAppointment(this.state.doctorId, {
                    page: page,
                    size: size,
                    sort: sort,
                    startDate: formattedStartDate,
                    endDate: formattedEndDate
                }).then(resp => {
                    this.setState({
                        showModal: true,
                        totalPages: resp.data.totalPages,
                        appointments: resp.data.content
                    });
                });
            }

        });
    }

    handlePageChange(page) {
        let { size, sort, doctorId } = this.state;
        AppointmentService.findDoctorsAppointment(doctorId, {
            page: page.selected,
            size: size,
            sort: sort,
            startDate: this.formatDate(this.state, 'YYYY-MM-DD HH:mm:ss').startDate,
            endDate: this.formatDate(this.state, 'YYYY-MM-DD HH:mm:ss').endDate
        }).then(resp => {
            this.setState({
                showModal: true,
                appointments: resp.data.content
            });
        });
    }

    formatDate({startDate, startHour, startMinute, startTimePeriod,
        endDate, endHour, endMinute, endTimePeriod }, format) {

            return {
                startDate: moment(`${startDate} ${startHour}:${startMinute} ${startTimePeriod}`, 'YYYY-MM-DD h:mm a').format(format),
                endDate: moment(`${endDate} ${endHour}:${endMinute} ${endTimePeriod}`, 'YYYY-MM-DD h:mm a').format(format)
            }
    }

    closeModal() {
        this.setState({
            showModal: false,
            appointments: []
        });
    }

    showAppointmentModal() {
        this.setState({
            showModal: true
        });
    }

    displayAppointmentRows() {
        let { appointments } = this.state;

        if (appointments.length !== 0) {
            return appointments.map(appointment => {
                return <DoctorAppointmentTableRow key={appointment.id} data={appointment} />
            });
        }

        return <tr>
            <td className="text-center" colSpan={2}>No Appointments</td>
        </tr>;
    }

    back() {
        this.props.navigate('/appointment');
    }

    render() {
        let { userHasAddress, firstName, lastName, address, email, mobileNo, startDate, endDate,
            startHour, startMinute, startTimePeriod, endHour,
            endMinute, endTimePeriod, reasonForAppointment, firstTime, showModal, totalPages } = this.state;

        let appointmentSchema = getYupValidation('appointment');

        return <>
            <Modal
                show={showModal}
                onHide={this.closeModal}
                backdrop="static"
                keyboard={false}>

                <Modal.Header>
                    <Modal.Title>Doctor Appointments</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <h5 className="text-muted"></h5>
                    <table className="table">
                        <thead>
                            <tr>
                                <th className="text-center" scope="col">Start Date</th>
                                <th className="text-center" scope="col">End Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.displayAppointmentRows()}
                        </tbody>
                    </table>
                    <ReactPaginate
                        className="pagination justify-content-center"
                        nextLabel="next >"
                        onPageChange={this.handlePageChange}
                        pageRangeDisplayed={3}
                        marginPagesDisplayed={2}
                        pageCount={totalPages}
                        previousLabel="< prev"
                        pageClassName="page-item"
                        pageLinkClassName="page-link"
                        previousClassName="page-item"
                        previousLinkClassName="page-link"
                        nextClassName="page-item"
                        nextLinkClassName="page-link"
                        breakLabel="..."
                        breakClassName="page-item"
                        breakLinkClassName="page-link"
                        containerClassName="pagination"
                        activeClassName="active"
                        renderOnZeroPageCount={null}
                    />
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={this.closeModal}>
                        Close
                    </Button>
                </Modal.Footer>

            </Modal>
            <div className="mt-3 common-container rounded shadow">
                <HospitalHeader label="Create Appointment" />

                <Formik
                    initialValues={{
                        startDate, endDate, startHour, startMinute, startTimePeriod, endHour,
                        endMinute, endTimePeriod, reasonForAppointment, firstTime, address
                    }}
                    onSubmit={this.onSubmit}
                    validationSchema={appointmentSchema}
                    validateOnBlur={false}
                    validateOnChange={false}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>
                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label">First name:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{firstName}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">Last Name:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{lastName}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">Address:</label>

                                    {
                                        userHasAddress ? <label className="col-sm-6 col-form-label text-muted">{address}</label>
                                            : <div className="col-sm-6">
                                                <Field type="text" className="col-sm-6 form-control" name="address" />
                                                <ErrorMessage name="address" component="div" className="text-red" />
                                            </div>
                                    }

                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-4 col-form-label ">First Time Hospital Visit?:</label>
                                    <div className="radio-section col-sm-2 form-check form-check-inline">
                                        <Field className="form-check-input" type="radio" name="firstTime" value="true" />
                                        <label className="form-check-label">Yes</label>
                                    </div>
                                    <div className="radio-section col-sm-2 form-check form-check-inline">
                                        <Field className="form-check-input" type="radio" name="firstTime" value="false" />
                                        <label className="form-check-label">No</label>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">Start Date:</label>
                                    <div className="col-sm-3">
                                        <Field min={moment().format('YYYY-MM-DD')} type="date" className="form-control" name="startDate" />
                                        <ErrorMessage name="startDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="startHour" >
                                            {
                                                ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="startMinute" >
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="startTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">End Date:</label>
                                    <div className="col-sm-3">
                                        <Field min={moment().format('YYYY-MM-DD')} type="date" className="form-control" name="endDate" />
                                        <ErrorMessage name="endDate" component="div" className="text-red" />
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="endHour" >
                                            {
                                                ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'].map(option => {
                                                    return <option key={option} value={option}>{option}</option>
                                                })
                                            }
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="endMinute" >
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </Field>
                                    </div>
                                    <div className="col-sm-2">
                                        <Field as="select" className="form-select mb-3" name="endTimePeriod" >
                                            <option value="am">AM</option>
                                            <option value="pm">PM</option>
                                        </Field>
                                    </div>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">Email:</label>
                                    <label className="col-sm-4 col-form-label text-muted">{email}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-2 col-form-label ">Mobile No:</label>
                                    <label className="col-sm-3 col-form-label text-muted">{mobileNo}</label>
                                </div>

                                <div className="mb-3 row">
                                    <label className="col-sm-4 col-form-label ">Reason for Appointment</label>
                                    <div className="col-sm-6">
                                        <Field as="textarea" className="form-control" name="reasonForAppointment" />
                                        <ErrorMessage name="reasonForAppointment" component="div" className="text-red" />
                                    </div>
                                </div>

                                <section className="button-section pb-2 text-center">
                                    <button type="submit" className="btn btn-primary me-2">Confirm</button>
                                    <button type="button" onClick={e => { this.checkDoctorAppointments(props); }} className="btn btn-primary me-2">Check Appointments</button>
                                    <button type="button" onClick={this.back} className="btn btn-primary">Back</button>
                                </section>

                            </Form>
                        )
                    }
                </Formik>

                <ToastContainer className="text-center"
                    position="bottom-center"
                    autoClose={2000}
                    hideProgressBar={true}
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    pauseOnHover={false}
                    draggable={false}
                    theme="colored" />
            </div>
        </>;
    }
}

export default CreateAppointment;
