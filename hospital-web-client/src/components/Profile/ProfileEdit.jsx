import './styles/main.css';
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import UserService from '../../services/UserService';
import AuthService from '../../services/AuthService';
import { buildProfileURL } from '../../utils/Utils';
import { DEFAULT_PROFILE_IMG } from '../../constants/GlobalConstants';
import getYupValidation from '../../utils/YupValidationFactory';
import moment from 'moment';
import { isFileImage } from '../../utils/Utils';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class ProfileEdit extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            isDoctor: false,
            firstName: '',
            lastName: '',
            mobileNo: '',
            address: '',
            birthDate: '',
            imagePreview: DEFAULT_PROFILE_IMG,
            image: null,
            specialization: '0001IM',
            doctorCodeId: '0001IM',
            expertise: '',
            noOfYearsExperience: '',
            education: '',
            schedule: ''
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.loadFile = this.loadFile.bind(this);

    }

    componentDidMount() {
        AuthService.fetchUserFromAPI().then(resp => {
            console.log('getLoggedInUser', resp);

            let { firstName, lastName, mobileNo, address,
                birthDate, profileImage, doctorCodeId, specialization,
                noOfYearsExperience, schedule, expertise, education } = resp.data;

            this.setState({
                isDoctor: doctorCodeId ? true : false,
                firstName: firstName,
                lastName: lastName,
                mobileNo: mobileNo,
                address: address ? address : '',
                birthDate: birthDate ? birthDate : '',
                imagePreview: profileImage ? buildProfileURL(profileImage) : DEFAULT_PROFILE_IMG,
                doctorCodeId: doctorCodeId ? doctorCodeId : '',
                specialization: specialization ? specialization : '',
                noOfYearsExperience: noOfYearsExperience ? noOfYearsExperience : '',
                schedule: schedule ? schedule : '',
                expertise: expertise ? expertise : '',
                education: education ? education : ''
            });

        }).catch(err => {
            console.log('error', err)
            if (err.response.status === 401) {
                AuthService.logout();
                this.props.navigate('/');
            }
        });
    }

    onSubmit(values) {

        let fd = new FormData();
        let updatedValues = {};
        values.doctorCode = values.doctorCodeId;

        if (!this.state.isDoctor) {

            const patientValues = {
                doctorCodeId: null,
                doctorCode: null,
                specialization: null,
                noOfYearsExperience: null,
                schedule: null,
                expertise: null,
                education: null
            };

            updatedValues = { ...values, ...patientValues }
            console.log('patient values', values);
        } else {
            let newValues = {};
            Object.keys(values).forEach(key => {
                if (!values[key]) {
                    newValues[key] = null;
                }
            });


            updatedValues = { ...values, ...newValues };
            console.log('newValues', newValues);
        }

        fd.append('file', values.image);
        fd.append('updateData', JSON.stringify(updatedValues));

        this.setState({ submitting: true });

        UserService.updateProfile(fd).then(resp => {
            console.log('resp', resp);
            window.location.pathname = '/user/profile'
        }).catch(error => console.log('err', error.response));
    }

    loadFile(file) {
        if (isFileImage(file.type)) {
            let fileReader = new FileReader();
            fileReader.onload = () => {

                this.setState({ imagePreview: fileReader.result })
            }
            fileReader.readAsDataURL(file);
        }
    }

    render() {
        const SignUpSchema = getYupValidation('profileEdit');

        let { firstName, lastName, mobileNo, birthDate,
            address, image, doctorCodeId, specialization, noOfYearsExperience,
            education, expertise, schedule } = this.state;

        return (
            <>
                <div className="mt-3 common-container">
                    <Formik
                        initialValues={{
                            firstName, lastName, mobileNo, doctorCodeId,
                            birthDate, address, image, specialization, noOfYearsExperience,
                            education, expertise, schedule
                        }}
                        onSubmit={this.onSubmit}
                        validationSchema={SignUpSchema}
                        validateOnBlur={false}
                        validateOnChange={false}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form className="p-3 shadow rounded">

                                    <header className="profile-header text-center mb-4">
                                        <h1 className="display-1">Hospital Name</h1>
                                        <h2 className="text-muted">User Information</h2>
                                    </header>

                                    <div className="profile-image text-center">
                                        <img src={this.state.imagePreview} alt="mdo" width="140" height="140" className="me-3 rounded-circle shadow" />
                                        <div className="file-uploader-field mt-4 input-group mb-3">
                                            <Field onChange={e => {
                                                const file = e.currentTarget.files[0];
                                                props.setFieldValue('image', file);
                                                if (isFileImage(file.type)) {
                                                    this.loadFile(file);
                                                } else {
                                                    toast.error('Invalid file type!');
                                                }
                                            }} className="form-control" type="file" name="file" />
                                        </div>
                                        <ErrorMessage name="image" component="div" className="text-red" />
                                    </div>

                                    <hr className="hr-text"></hr>

                                    <div className="row mb-3">
                                        <div className="col-sm-6">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="firstName" placeholder="placeholder" />
                                                <label>First Name</label>
                                                <ErrorMessage name="firstName" component="div" className="text-red" />
                                            </div>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="lastName" placeholder="placeholder"></Field>
                                                <label>Last Name:</label>
                                                <ErrorMessage name="lastName" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col-sm-6">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="mobileNo" placeholder="placeholder" />
                                                <label>Mobile</label>
                                                <ErrorMessage name="mobileNo" component="div" className="text-red" />
                                            </div>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="form-floating mb-3">
                                                <Field max={moment().format('YYYY-MM-DD')} className="form-control" type="date" name="birthDate" placeholder="placeholder"></Field>
                                                <label>Birth date</label>
                                                <ErrorMessage name="birthDate" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col-sm-12">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="address" placeholder="placeholder" />
                                                <label>Address</label>
                                                <ErrorMessage name="address" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    {/* For users that is doctor */}
                                    {
                                        doctorCodeId && <>
                                            <hr className="hr-text"></hr>

                                            <div className="row mb-3">
                                                <div className="col-sm-6">
                                                    <Field className="form-select-lg form-select" as="select" name="doctorCodeId">
                                                        <option value="0001IM">Internal Medicine</option>
                                                        <option value="0002PD">GrePediatricianen</option>
                                                        <option value="0003SG">Surgeon</option>
                                                        <option value="0004OB">Obstetrician/Gynecologist</option>
                                                        <option value="0005CD">Cardiologist</option>
                                                        <option value="0006GSG">Gastroenterologist</option>
                                                        <option value="0007NG">Neurologist</option>
                                                    </Field>
                                                    <ErrorMessage name="specialization" component="div" className="text-red" />
                                                </div>

                                                <div className="col-sm-6">
                                                    <div className="form-floating mb-3">
                                                        <Field className="form-control" type="text" name="noOfYearsExperience" placeholder="placeholder" />
                                                        <label>Years of experience</label>
                                                        <ErrorMessage name="noOfYearsExperience" component="div" className="text-red" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div className="row mb-3">
                                                <div className="col-sm-6">
                                                    <div className="form-floating mb-3">
                                                        <Field className="form-control" type="text" name="education" placeholder="placeholder" />
                                                        <label>Education</label>
                                                        <ErrorMessage name="education" component="div" className="text-red" />
                                                    </div>
                                                </div>

                                                <div className="col-sm-6">
                                                    <div className="form-floating mb-3">
                                                        <Field className="form-control" type="text" name="schedule" placeholder="placeholder" />
                                                        <label>Schedule</label>
                                                        <ErrorMessage name="schedule" component="div" className="text-red" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div className="row mb-3">
                                                <div className="col-sm-12">
                                                    <div className="form-floating mb-3">
                                                        <Field className="form-control" type="text" name="expertise" placeholder="placeholder" />
                                                        <label>Expertise</label>
                                                        <ErrorMessage name="expertise" component="div" className="text-red" />
                                                    </div>
                                                </div>
                                            </div>
                                        </>
                                    }

                                    <section className="button-section pb-2 text-center">
                                        <button type="submit" className="me-3 btn btn-primary" disabled={this.state.submitting}>
                                            {
                                                this.state.submitting ? (<><span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                    Loading...</>) : 'Save'

                                            }
                                        </button>

                                        <button onClick={e => this.props.navigate('/user/profile')} type="submit" className="btn btn-primary">
                                            Back
                                        </button>
                                    </section>
                                </Form>
                            )

                        }

                    </Formik>
                </div>

                <ToastContainer className="text-center"
                    position="bottom-center"
                    autoClose={2500}
                    hideProgressBar={true}
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    pauseOnHover={false}
                    draggable={false}
                    theme="colored" />
            </>
        );
    }
}

export default ProfileEdit;
