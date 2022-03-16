import { Component } from 'react';

class ConfirmRegistration extends Component {
    constructor(props) {
        super(props);
        this.state = {
            submitting: false,
            data: this.props.location.state
        }

        

        console.log('state passed', this.state.data);
        this.onSubmit = this.onSubmit.bind(this);
        this.return = this.return.bind(this);
    }

    onSubmit() {
        console.log('signup value', this.state.data);
        // toast.info("form submit!", {
        //     position: "bottom-center",
        //     autoClose: 2000,
        //     hideProgressBar: true,
        //     closeOnClick: true,
        //     pauseOnHover: true,
        //     draggable: true,
        //     progress: undefined
        // });

        // this.setState({ submitting: true });

        // console.log('server response!');
        // this.props.navigate('/registration/confirm', { state: values });
    }

    return() {
        this.props.navigate('/registration', {state: this.state.data});
    }

    componentDidMount() {
        
    }


    render() {
        const { firstName, lastName, email, gender, mobileNo, doctorCode } = this.props.location.state;

        return <>
            <div className="mt-3 m-auto w-50 p-3 shadow rounded text-center">
                <h1 className="mb-3 text-primary">Confirm Details</h1>

                <div className="row mb-3">
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">First Name:</label>
                        <label className="fs-4">{firstName}</label>
                    </div>
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">Last Name:</label>
                        <label className="fs-4">{lastName}</label>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">Email:</label>
                        <label className="fs-4">{email}</label>
                    </div>
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">Gender:</label>
                        <label className="fs-4">{gender}</label>
                    </div>
                </div>

                <div className="row mb-3">
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">Mobile No:</label>
                        <label className="fs-4">{mobileNo}</label>
                    </div>
                    <div className="col">
                        <label className="me-2 fw-bold fs-4">Doctor Code:</label>
                        <label className="fs-4">{doctorCode}</label>
                    </div>
                </div>

                <div className="mb-3 row">
                    <div className="col">
                        <button onClick={this.return} type="submit" className="me-3 btn btn-primary" disabled={this.state.submitting}>
                            Back
                        </button>

                        <button onClick={this.onSubmit} type="button" className="btn btn-primary " disabled={this.state.submitting}>
                            {
                                this.state.submitting ? (<><span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    Loading...</>) : 'Register'

                            }
                        </button>
                    </div>
                </div>
            </div>
        </>;
    }
}

export default ConfirmRegistration;
