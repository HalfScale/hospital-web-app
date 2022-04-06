import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import getYupValidation from '../../utils/YupValidationFactory';
import HospitalHeader from '../HospitalHeader';

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
            hospitalCode: '',
            password: '',
            confirmPassword: '',
            termsOfAgreement: false,
            data: {}
        }

        const previousData = this.props.location.state
        if (previousData) {
            this.state = { ...this.state, ...previousData };
        }

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(values) {
        console.log('signup value', values);

        this.setState({ submitting: true });

        console.log('server response!');
        this.props.navigate('/registration/confirm', { state: values });
    }

    render() {
        const SignUpSchema = getYupValidation('registration');

        let { firstName, lastName, email, gender, mobileNo, hospitalCode, password, confirmPassword, termsOfAgreement } = this.state;

        return (
            <>
                <div className="mt-3 m-auto w-50">
                    <Formik
                        initialValues={{ firstName, lastName, email, gender, mobileNo, hospitalCode, password, confirmPassword, termsOfAgreement }}
                        onSubmit={this.onSubmit}
                        validationSchema={SignUpSchema}
                        validateOnBlur={false}
                        validateOnChange={false}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form className="p-3 shadow rounded">

                                    <HospitalHeader label="Registration"/>

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
                                                <Field className="form-check-input" type="radio" name="gender" value="1" />
                                                <label className="form-check-label">Male</label>
                                            </div>
                                            <div className="form-check form-check-inline">
                                                <Field className="form-check-input" type="radio" name="gender" value="2" />
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
                                            <Field className="form-control" type="text" name="hospitalCode"></Field>
                                            <div className="form-text">(Optional)</div>
                                            <ErrorMessage name="hospitalCode" component="div" className="text-red" />
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
                </div>
            </>
        );
    }
}

export default Registration;
