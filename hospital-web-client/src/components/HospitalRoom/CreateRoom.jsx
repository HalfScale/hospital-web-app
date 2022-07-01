import './styles/main.css';
import { Component } from 'react';
import { ErrorMessage, Field, Form, Formik } from "formik";
import getYupValidation from '../../utils/YupValidationFactory';
import HospitalHeader from '../HospitalHeader';
import roomDefaultImg from './room-default.png'
import HospitalRoomService from '../../services/HospitalRoomService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { isFileImage } from '../../utils/Utils';

class CreateRoom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomCode: '',
            roomName: '',
            description: '',
            image: null,
            imagePreview: roomDefaultImg
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.viewSample = this.viewSample.bind(this);
        this.back = this.back.bind(this);
        this.loadFile = this.loadFile.bind(this);
    }

    componentDidMount() {

        if (this.props.location.state) {
            let { roomCode, roomName, description, image } = this.props.location.state;
            this.setState({
                roomCode: roomCode,
                roomName: roomName,
                description: description,
                image: image
            }, () => {
                if (image) {
                    this.loadFile(image)
                }
            });
        }
    }

    onSubmit(values) {

        let { image } = this.state;

        let fd = new FormData();

        fd.append('file', image);
        fd.append('hospitalRoomDto', JSON.stringify(values));

        HospitalRoomService.addRoom(fd)
            .then(resp => {
                this.props.navigate('/hospital_rooms', {
                    state: {
                        showToast: true,
                        message: 'Room created succesfully!'
                    }
                });
            });
    }

    viewSample() {
        let { roomCode, roomName, description, image } = this.state;
        if (roomCode && roomName && description) {
            this.props.navigate('/hospital_rooms/preview', {
                state: {
                    roomCode: roomCode,
                    roomName: roomName,
                    description: description,
                    image: image
                }
            });
        } else {
            toast.warn('Please fill the required fields!');
        }

    }

    back() {
        this.props.navigate('/hospital_rooms');
    }

    loadFile(file) {
        let fileReader = new FileReader();
        fileReader.onload = () => {
            this.setState({ imagePreview: fileReader.result })
        }
        fileReader.readAsDataURL(file);
    }

    render() {
        let { roomCode, roomName, description, imagePreview } = this.state;
        const roomValidationSchema = getYupValidation('room');

        return <>
            <div className="create-room-container mt-3 m-auto rounded shadow">
                <HospitalHeader label="Create Room" />

                <img src={imagePreview} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <hr />

                <Formik
                    initialValues={{
                        roomCode, roomName, description
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
                                <div className="mx-auto row room-form-group mt-4 input-group mb-3">
                                    <Field onChange={e => {
                                        const file = e.currentTarget.files[0];
                                        props.setFieldValue('image', e.currentTarget.files[0]); // we are setting this for formik validation
                                        if(isFileImage(file.type)) {
                                            this.setState({ image: e.currentTarget.files[0] });
                                            this.loadFile(e.target.files[0]);
                                        }else {
                                            toast.error('Invalid file type!');
                                        }
                                    }} className="form-control" type="file" name="file" />
                                    <ErrorMessage name="image" component="div" className="text-red text-center" />
                                </div>

                                <div class="form-floating room-form-group m-auto mb-3">
                                    <Field onChange={e => this.setState({ roomCode: e.target.value })} id="room-code" className="form-control" type="text" name="roomCode" placeholder="placeholder" />
                                    <label for="room-code">Room Code</label>
                                    <ErrorMessage name="roomCode" component="div" className="text-red" />
                                </div>

                                <div class="form-floating room-form-group m-auto mb-3">
                                    <Field onChange={e => this.setState({ roomName: e.target.value })} id="room-name" className="form-control" type="text" name="roomName" placeholder="placeholder" />
                                    <label for="room-name">Room Name</label>
                                    <ErrorMessage name="roomName" component="div" className="text-red" />
                                </div>

                                <div class="form-floating room-form-group m-auto">
                                    <Field onChange={e => this.setState({ description: e.target.value })} id="description" className="form-control" type="text" name="description" placeholder="placeholder" />
                                    <label for="description">Description</label>
                                    <ErrorMessage name="description" component="div" className="text-red" />
                                </div>

                                <section className="button-section p-2 text-center">
                                    <button type="submit" className="m-2 btn btn-primary">Save</button>
                                    <button onClick={this.viewSample} type="button" className="m-2 btn btn-primary">View</button>
                                    <button onClick={this.back} type="button" className="m-2 btn btn-primary">Back</button>
                                </section>
                            </Form>
                        )
                    }

                </Formik>
            </div>

            <ToastContainer
                position="bottom-center"
                autoClose={3000}
                hideProgressBar={true}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnHover={false}
                draggable={false}
                theme="colored" />
        </>;
    }
}

export default CreateRoom;
