import './Registration.css';
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import * as Yup from 'yup';
import { ToastContainer, toast } from 'react-toastify';
import AuthService from '../../services/AuthService';

const termsOfAgreementText = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus sagittis, urna dictum venenatis malesuada, lacus eros venenatis eros, ac tristique lorem arcu hendrerit urna. Nulla a libero auctor, tincidunt diam sed, rhoncus massa. Maecenas porttitor pretium lectus non aliquet. Praesent sollicitudin rhoncus ante id ullamcorper. Donec tincidunt non urna viverra consequat. Curabitur vel velit id mi egestas rutrum. Donec maximus risus sapien, ac placerat quam pharetra ac. Aliquam dui nunc, semper eu blandit ac, eleifend at risus. Nullam sed condimentum quam, nec tincidunt massa. Aenean ut cursus felis. Pellentesque feugiat est sollicitudin consequat elementum. Cras fermentum vel magna ultrices interdum. Nam quis quam non ipsum varius laoreet. Nunc sollicitudin facilisis sem, in convallis augue consectetur eget. Suspendisse eu enim pharetra, varius risus a, efficitur lorem. Phasellus vitae ultricies est.';
const TextAreaComponent = (props) => (
    <textarea className="form-control" rows="8" disabled readOnly {...props} defaultValue={termsOfAgreementText}></textarea>
);

class Registration extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            id: -1,
            firstName: '',
            lastName: '',
            email: '',
            gender: '',
            mobileNo: '',
            doctorCode: '',
            password: '',
            confirmPassword: '',
            termsOfAgreement: false,
            data: {}
        }

        //data when user from /registration/confirm, clicked back button with state
        const previousData = this.props.location.state
        // console.log('previousData', previousData);
        if (previousData) {
            this.state = { ...this.state, ...previousData };
        }

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(values) {
        console.log('signup value', values);
        toast.info("form submit!", {
            position: "bottom-center",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined
        });

        this.setState({ submitting: true });

        console.log('server response!');
        this.props.navigate('/registration/confirm', { state: values });
    }

    render() {
        const SignUpSchema = Yup.object().shape({
            firstName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            lastName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            email: Yup.string().email('Invalid email').required('Required!')
                .test(
                    'email-backend-validation',
                    'Email is already used',
                    async (email) => {
                        return await AuthService.checkIfEmailIsValid(email)
                        .then(res => res.status !== 400)
                        .catch(err => { console.log('err', err)}); 
                        // console.log('success', response);
                        // // return response && response.status ;
                    }
                ),
            gender: Yup.string().required('Required!'),
            mobileNo: Yup.string().min(11, 'Enter a correct mobileNo').required('Required!'),
            password: Yup.string().min(8, 'Too Short!').max(15, 'Too Long!').required('Required'),
            confirmPassword: Yup.string().min(8, 'Too Short!').max(15, 'Too Long!').required('Required')
                .oneOf([Yup.ref('password'), null], "Password dont match!"),
            termsOfAgreement: Yup.boolean().oneOf([true], 'Must Accept Terms of Agreement')
        });

        let { firstName, lastName, email, gender, mobileNo, doctorCode, password, confirmPassword, termsOfAgreement } = this.state;
        return (
            <>
                <div className="mt-3 m-auto w-50">
                    <Formik
                        initialValues={{ firstName, lastName, email, gender, mobileNo, doctorCode, password, confirmPassword, termsOfAgreement }}
                        onSubmit={this.onSubmit}
                        validationSchema={SignUpSchema}
                        validateOnBlur={false}
                        validateOnChange={false}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form className="p-3 shadow rounded">

                                    <div className="row mb-3">
                                        <div className="col">
                                            <label>First Name:</label>
                                            <Field className="form-control" type="text" name="firstName"></Field>
                                            <ErrorMessage name="firstName" component="div" className="text-red" />
                                        </div>
                                        <div className="col">
                                            <label>Last Name:</label>
                                            <Field className="form-control" type="text" name="lastName"></Field>
                                            <ErrorMessage name="lastName" component="div" className="text-red" />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <label>Email:</label>
                                            <Field className="form-control" type="text" name="email"></Field>
                                            <ErrorMessage name="email" component="div" className="text-red" />
                                        </div>
                                        <div className="col">
                                            <div>Gender:</div>
                                            <div className="form-check form-check-inline">
                                                <Field className="form-check-input" type="radio" name="gender" value="male" />
                                                <label className="form-check-label">Male</label>
                                            </div>
                                            <div className="form-check form-check-inline">
                                                <Field className="form-check-input" type="radio" name="gender" value="female" />
                                                <label className="form-check-label">Female</label>
                                            </div>
                                            <ErrorMessage name="gender" component="div" className="text-red" />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <label>Mobile No:</label>
                                            <Field className="form-control" type="text" name="mobileNo"></Field>
                                            <ErrorMessage name="mobileNo" component="div" className="text-red" />
                                        </div>
                                        <div className="col">
                                            <label>Doctor Code:</label>
                                            <Field className="form-control" type="text" name="doctorCode"></Field>
                                            <div className="form-text">(Optional)</div>
                                            <ErrorMessage name="doctorCode" component="div" className="text-red" />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <label>Password:</label>
                                            <Field className="form-control" type="password" name="password"></Field>
                                            <ErrorMessage name="password" component="div" className="text-red" />
                                        </div>
                                        <div className="col">
                                            <label>Confirm Password:</label>
                                            <Field className="form-control" type="password" name="confirmPassword"></Field>
                                            <ErrorMessage name="confirmPassword" component="div" className="text-red" />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <label className="fw-bold">Terms of Agreement:</label>
                                            <textarea className="form-control" rows="8" disabled readOnly defaultValue={termsOfAgreementText}>

                                            </textarea>
                                        </div>
                                    </div>

                                    <div className="mb-3 row form-check">
                                        <div className="col">
                                            {/* <input className="form-check-input" type="checkbox" value="yes" /> */}
                                            <Field className="form-check-input" type="checkbox" name="termsOfAgreement"></Field>
                                            <label className="form-check-label">
                                                I Agree to the terms and agreement
                                            </label>
                                            <ErrorMessage name="termsOfAgreement" component="div" className="text-red" />
                                        </div>
                                    </div>

                                    <div className="mb-3 row">
                                        <div className="col">
                                            <button type="submit" className="btn btn-primary" disabled={this.state.submitting}>
                                                {
                                                    this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                        Loading...</>) : 'Register'

                                                }
                                            </button>
                                        </div>
                                    </div>
                                </Form>
                            )

                        }
                    </Formik>
                    <ToastContainer />
                </div>
            </>
        );
    }
}

export default Registration;
