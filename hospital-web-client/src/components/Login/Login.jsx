import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import * as Yup from 'yup';
import { ToastContainer, toast } from 'react-toastify';


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
        console.log('signup value', values);
        toast.info("form submit!", {
            position: "top-center",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined
        });

        this.setState({submitting: true});

        setInterval(() => {
            console.log('server response!');
            this.setState({submitting: false});
        }, 2000);
    }

    render() {
        const SignUpSchema = Yup.object().shape({
            email: Yup.string()
                .email('Must be a valid email!')
                .required('Required!'),
            password: Yup.string()
                .required('Required!')
        });

        let { email, password } = this.state;
        return (
            <>
                <div className="mt-3 m-auto w-50">
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

                                    <div className="mb-3">
                                        <button type="submit" className="btn btn-primary" disabled={this.state.submitting}>
                                            {
                                                this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                Loading...</>): 'Login'

                                            }
                                        </button>
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

export default Login;
