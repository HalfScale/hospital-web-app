import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import * as Yup from 'yup';
import { ToastContainer, toast } from 'react-toastify';


class ProfileEdit extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            id: -1,
            firstName: '',
            lastName: '',
            mobileNo: '',
            address: '',
            birthDate: '',
            imagePreview: 'https://github.com/mdo.png',
            image: null
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.loadFile = this.loadFile.bind(this);

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

        setTimeout(() => {
            console.log('server response!');
            this.setState({ submitting: false });
        }, 2000);
    }

    loadFile(event) {
        console.log('on change triggered', event);
        let fileReader = new FileReader();
        fileReader.onload = () => {
            this.setState({ imagePreview: fileReader.result })
        }
        fileReader.readAsDataURL(event.target.files[0]);
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
            mobileNo: Yup.string().min(11, 'Enter a correct mobileNo').required('Required!'),
            birthDate: Yup.string().notRequired().nullable(),
            address: Yup.string().notRequired().nullable(),
            image: Yup.mixed().nullable()
                .test('is-correct-file', 'File too big', (file) => {
                    console.log('file size', file.size);
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

        let { firstName, lastName, mobileNo, birthDate, address, image } = this.state;
        return (
            <>
                <div className="mt-3 m-auto w-50">
                    <Formik
                        initialValues={{ firstName, lastName, mobileNo, birthDate, address, image }}
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
                                        <div className="w-50 mx-auto mt-4 input-group mb-3">
                                            {/* <input onChange={e => this.loadFile(e)} type="file" className="form-control" id="inputGroupFile02" /> */}
                                            <Field onChange={e => {
                                                props.setFieldValue('image', e.currentTarget.files[0]);
                                                this.loadFile(e);
                                            }} className="form-control" type="file" name="file" />
                                        </div>
                                        <ErrorMessage name="image" component="div" className="text-red" />
                                    </div>

                                    <hr className="hr-text"></hr>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="firstName" placeholder="placeholder" />
                                                <label>First Name</label>
                                                <ErrorMessage name="firstName" component="div" className="text-red" />
                                            </div>
                                        </div>
                                        <div className="col">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="lastName" placeholder="placeholder"></Field>
                                                <label>Last Name:</label>
                                                <ErrorMessage name="lastName" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="mobileNo" placeholder="placeholder" />
                                                <label>Mobile</label>
                                                <ErrorMessage name="mobileNo" component="div" className="text-red" />
                                            </div>
                                        </div>
                                        <div className="col">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="date" name="birthDate" placeholder="placeholder"></Field>
                                                <label>Birth date</label>
                                                <ErrorMessage name="birthDate" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col">
                                            <div className="form-floating mb-3">
                                                <Field className="form-control" type="text" name="address" placeholder="placeholder" />
                                                <label>Address</label>
                                                <ErrorMessage name="address" component="div" className="text-red" />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="mb-3 row">
                                        <div className="col">
                                            <button type="submit" className="me-3 btn btn-primary" disabled={this.state.submitting}>
                                                Back
                                            </button>

                                            <button type="submit" className="btn btn-primary" disabled={this.state.submitting}>
                                                {
                                                    this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                        Loading...</>) : 'Edit'

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

export default ProfileEdit;
