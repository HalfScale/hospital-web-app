import './styles/main.css';
import { Component } from 'react';
import HospitalHeader from '../HospitalHeader';
import HospitalRoomService from '../../services/HospitalRoomService';
import { ErrorMessage, Field, Form, Formik } from "formik";
import getYupValidation from '../../utils/YupValidationFactory';
import roomDefaultImg from './room-default.png'
import { buildRoomImageURL } from '../../utils/Utils';
import { ToastContainer, toast } from 'react-toastify';
import { isFileImage } from '../../utils/Utils';

class EditRoom extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roomId: this.props.params.id,
            roomCode: '',
            roomName: '',
            description: '',
            imagePreview: roomDefaultImg,
            imgHash: '',
            image: null
        }
        this.onSubmit = this.onSubmit.bind(this);
        this.back = this.back.bind(this);
        this.preview = this.preview.bind(this);
        this.loadFile = this.loadFile.bind(this);
    }

    componentDidMount() {

        if (!this.props.location.state) {
            HospitalRoomService.findRoomById(this.state.roomId)
                .then(resp => {
                    let { id, roomCode, roomName, description, roomImage } = resp.data;
                    this.setState({
                        roomId: id,
                        roomCode: roomCode,
                        roomName: roomName,
                        description: description,
                        imagePreview: roomImage ? buildRoomImageURL(roomImage) : roomDefaultImg,
                        imgHash: roomImage
                    });
                }).catch(error => {
                    if(error.response.status === 400) {
                        this.props.navigate('/404-not-found');
                    }
                });
        } else {
            let { state } = this.props.location;
            this.setState({
                roomId: state.roomId,
                roomCode: state.roomCode,
                roomName: state.roomName,
                description: state.description,
                image: state.image,
                imgHash: state.imgHash
            }, () => {
                if (state.image) {
                    this.loadFile(state.image);
                } else {
                    this.setState({
                        imagePreview: buildRoomImageURL(state.imgHash)
                    })
                }
            });
        }
    }

    onSubmit(values) {
        let { image } = this.state;

        values.id = this.state.roomId;

        let fd = new FormData();
        fd.append('file', image);
        fd.append('hospitalRoomDto', JSON.stringify(values));

        HospitalRoomService.updateRoom(fd).then(resp => {
            this.props.navigate('/hospital_rooms', {
                state: {
                    showToast: true,
                    message: 'Room updated succesfully!'
                }
            });
        });
    }

    back() {
        this.props.navigate('/hospital_rooms');
    }

    preview() {
        let { roomId, roomCode, roomName, description, image, imgHash } = this.state;
        if (roomCode && roomName && description) {
            this.props.navigate('/hospital_rooms/preview', {
                state: {
                    roomId: roomId,
                    roomCode: roomCode,
                    roomName: roomName,
                    description: description,
                    image: image,
                    imgHash: imgHash,
                    action: 'edit'
                }
            });
        } else {
            toast.warn('Please fill the required fields!');
        }
    }

    loadFile(file) {
        let fileReader = new FileReader();
        fileReader.onload = () => {
            this.setState({ imagePreview: fileReader.result });
        }
        fileReader.readAsDataURL(file);
    }

    render() {
        let { roomId, roomCode, roomName, description, imagePreview, fileName } = this.state;
        const roomValidationSchema = getYupValidation('room');

        return <>
            <div className="mt-3 m-auto edit-room-container rounded shadow">
                <HospitalHeader label="Edit Room Details" />

                <img src={imagePreview} className="d-block m-auto room-image shadow rounded" alt="hospital-room" />

                <hr />

                <Formik
                    initialValues={{
                        roomCode, roomName, description, roomId
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
                                        if (isFileImage(file.type)) {
                                            this.setState({ image: e.currentTarget.files[0] });
                                            this.loadFile(e.target.files[0]);
                                        } else {
                                            toast.error('Invalid file type!');
                                        }
                                    }} className="form-control" type="file" name="file" value={fileName} />
                                    <ErrorMessage name="image" component="div" className="text-red text-center" />
                                </div>

                                <div className="form-floating room-form-group m-auto mb-3">
                                    <Field onChange={e => this.setState({ roomCode: e.target.value })} id="room-code" className="form-control" type="text" name="roomCode" placeholder="placeholder" />
                                    <label htmlFor="room-code">Room Code</label>
                                    <ErrorMessage name="roomCode" component="div" className="text-red" />
                                </div>

                                <div className="form-floating room-form-group m-auto mb-3">
                                    <Field onChange={e => this.setState({ roomName: e.target.value })} id="room-name" className="form-control" type="text" name="roomName" placeholder="placeholder" />
                                    <label htmlFor="room-name">Room Name</label>
                                    <ErrorMessage name="roomName" component="div" className="text-red" />
                                </div>

                                <div className="form-floating room-form-group m-auto">
                                    <Field onChange={e => this.setState({ description: e.target.value })} id="description" className="form-control" type="text" name="description" placeholder="placeholder" />
                                    <label htmlFor="description">Description</label>
                                    <ErrorMessage name="description" component="div" className="text-red" />
                                </div>

                                <section className="button-section p-2 text-center">
                                    <button type="submit" className="m-2 btn btn-primary">Save</button>
                                    <button onClick={this.preview} type="button" className="m-2 btn btn-primary">View</button>
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

export default EditRoom;
