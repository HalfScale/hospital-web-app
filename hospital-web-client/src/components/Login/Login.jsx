import './styles/main.css'
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import * as Yup from 'yup';
import { ToastContainer, toast } from 'react-toastify';
import AuthService from '../../services/AuthService';
import HospitalHeader from '../HospitalHeader';
import getYupValidation from '../../utils/YupValidationFactory';


class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            email: '',
            password: ''
        }

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(values) {
        console.log('login value', values);

        this.setState({ submitting: true });

        AuthService.login(values)
            .then(resp => {
                this.setState({ submitting: false });
                AuthService.setAuthenticatedUser(resp.data.token);
                window.location.pathname = '/'
            }).catch(error => {
                toast.error("Invalid username and password!");
            }).finally(() => this.setState({ submitting: false }));
    }

    render() {
        const SignUpSchema = getYupValidation('login');

        let { email, password } = this.state;

        return (
            <>
                <div className="mt-3 login-container">
                    <Formik
                        initialValues={{ email, password }}
                        onSubmit={this.onSubmit}
                        validationSchema={SignUpSchema}
                        validateOnBlur={false}
                        validateOnChange={false}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form className="p-3 shadow rounded">

                                    <HospitalHeader label="Login" />

                                    <div className="mb-3">
                                        <label className="form-label">Email:</label>
                                        <Field className="form-control" type="text" name="email"></Field>
                                        <ErrorMessage name="email" component="div" className="text-red" />
                                    </div>

                                    <div className="mb-3 col">
                                        <label className="form-label">Password:</label>
                                        <Field className="form-control" type="password" name="password"></Field>
                                        <ErrorMessage name="password" component="div" className="text-red" />
                                    </div>

                                    <section className="button-section pb-2 text-center">
                                        <button type="submit" className="btn btn-primary" disabled={this.state.submitting}>
                                            {
                                                this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                    Loading...</>) : 'Login'

                                            }
                                        </button>
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
            </>
        );
    }
}

export default Login;
