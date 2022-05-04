import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import getYupValidation from '../../utils/YupValidationFactory';
import HospitalHeader from '../HospitalHeader';
import roomDefaultImg from './room-default.png'

class CreateRoom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomCode: '',
            roomName: '',
            description: '',
            image: roomDefaultImg
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.viewSample = this.viewSample.bind(this);
        this.back = this.back.bind(this);
    }

    onSubmit() {

    }

    viewSample() {

    }

    back() {

    }

    render() {
        let { image } = this.state;
        const roomValidationSchema = getYupValidation('room');

        return <>
            <div className="mt-3 m-auto w-50 rounded shadow">
                <HospitalHeader label="Create Room" />

                <img src={image} className="d-block m-auto room-image shadow rounded" alt="hospital-room-image" />

                <hr />

                <Formik
                    initialValues={{

                    }}
                    onSubmit={this.onSubmit}
                    validationSchema={roomValidationSchema}
                    validateOnBlur={false}
                    validateOnChange={false}
                    enableReinitialize={true}
                >
                    {
                        (props) => (
                            <Form>
                                <div className="w-50 mx-auto mt-4 input-group mb-3">
                                    <Field onChange={e => {

                                    }} className="form-control" type="file" name="file" />
                                </div>

                                {/* <ErrorMessage name="image" component="div" className="text-red" /> */}
                                {/* <Field className="form-control" type="text" name="firstName" placeholder="placeholder" /> */}

                                <div class="form-floating w-50 m-auto mb-3">
                                    <input type="email" class="form-control" id="floatingInput" placeholder="roomCode"/>
                                    <label for="floatingInput">Room Code</label>
                                </div>

                                <div class="form-floating w-50 m-auto mb-3">
                                    <input type="password" class="form-control" id="floatingPassword" placeholder="roomName"/>
                                    <label for="floatingPassword">Room Name</label>
                                </div>

                                <div class="form-floating w-50 m-auto">
                                    <input type="password" class="form-control" id="floatingPassword" placeholder="description" />
                                    <label for="floatingPassword">Description</label>
                                </div>

                                <section className="text-center mt-3 pb-2">
                                    <button className="m-2 btn btn-primary">Back</button>
                                    <button className="m-2 btn btn-primary">View</button>
                                    <button className="m-2 btn btn-primary">Save</button>
                                </section>
                            </Form>
                        )
                    }

                </Formik>
            </div>
        </>;
    }
}

export default CreateRoom;
