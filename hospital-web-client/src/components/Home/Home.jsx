import './styles/main.css';
import { Component } from 'react';
import firstSlide from './assets/wallpaper_hospital_2.png';
import secondSlide from './assets/doctors_wallpaper.jpg';
import thirdSlide from './assets/wallpaper_hospital_3.jpg';
import DoctorsService from '../../services/DoctorsService';
import defaultImg from '../default.png'
import hospitalBldg from './assets/hospital-bldg.jpg';
import hospitalDoctor from './assets/doctors_feature.png'
import hospitalWeb from './assets/hospital_web.jpg'
import DoctorProfileComponent from './DoctorProfileComponent';
import { Link } from 'react-router-dom';

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            defaultImg: defaultImg,
            doctors: [],
            page: 0,
            size: 3,
            sort: 'id,asc',
        }

        this.displayDoctors = this.displayDoctors.bind(this);
    }

    componentDidMount() {
        DoctorsService.getDoctors({
            page: this.state.page,
            size: this.state.size,
            sort: this.state.sort
        }).then(resp => {
            console.log('getDoctors', resp)
            this.setState({
                doctors: resp.data.content,
            });
        }).catch(err => {
            console.log('err', err.response)
        });
    }

    displayDoctors(doctors) {
        return doctors.map(doctor => <DoctorProfileComponent key={doctor.id} data={doctor} />)
    }

    render() {
        return (
            <>
                <main>
                    <div id="myCarousel" className="carousel slide" data-bs-ride="carousel">
                        <div className="carousel-indicators">
                            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="0" className="active" aria-current="true" aria-label="Slide 1"></button>
                            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
                            <button type="button" data-bs-target="#myCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
                        </div>

                        <div className="carousel-inner">
                            <div className="carousel-item active">
                                <img src={firstSlide} alt="hospital_wallpaper" />

                                <div className="container">
                                    <div className="carousel-caption text-start">
                                        <h1>Do you need any consultation services? Say no more.</h1>
                                        <p>Book an apointment to your desired doctor.</p>
                                        <p><Link to={`/registration`} className="btn btn-lg btn-primary">Sign up today</Link></p>
                                    </div>
                                </div>
                            </div>

                            <div className="carousel-item">
                                <img src={secondSlide} alt="hospital_wallpaper" />
                            </div>

                            <div className="carousel-item">
                                <img src={thirdSlide} alt="hospital_wallpaper" />

                                <div className="container">
                                    <div className="carousel-caption text-end">
                                        <h1>Our doctors are one of the best out there.</h1>
                                        <p>You should definitely check our doctors</p>
                                        <p><Link to={`/doctors`} className="btn btn-lg btn-primary">Browse doctors</Link></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <button className="carousel-control-prev" type="button" data-bs-target="#myCarousel" data-bs-slide="prev">
                            <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span className="visually-hidden">Previous</span>
                        </button>

                        <button className="carousel-control-next" type="button" data-bs-target="#myCarousel" data-bs-slide="next">
                            <span className="carousel-control-next-icon" aria-hidden="true"></span>
                            <span className="visually-hidden">Next</span>
                        </button>
                    </div>

                    <div className="container marketing">
                        <div className="row">
                            {
                                this.state.doctors.length > 0 ? this.displayDoctors(this.state.doctors) : <>
                                    <DoctorProfileComponent />
                                    <DoctorProfileComponent />
                                    <DoctorProfileComponent />
                                </>
                            }

                        </div>

                        {/* START THE FEATURETTES */}

                        <hr className="featurette-divider" />

                        <div className="row featurette">
                            <div className="col-md-7">
                                <h2 className="featurette-heading">One of the best hospitals out there. <span className="text-muted">Without a doubt.</span></h2>
                                <p className="lead">Tons of promising feedback around the world. We deliver the result you want.</p>
                            </div>
                            <div className="col-md-5">
                                <img src={hospitalBldg} alt="hospita-bldg" className="featurette-responsive featurette-img" />
                            </div>
                        </div>

                        <hr className="featurette-divider" />

                        <div className="row featurette">
                            <div className="col-md-7 order-md-2">
                                <h2 className="featurette-heading">Comprised of elite doctors. <span className="text-muted">See for yourself.</span></h2>
                                <p className="lead">We are comprised of doctors with different specialities. Check the doctor list for you to see.</p>
                            </div>
                            <div className="col-md-5 order-md-1">
                                <img src={hospitalDoctor} alt="hospita-doctor" className="featurette-responsive featurette-img" />
                            </div>
                        </div>

                        <hr className="featurette-divider" />

                        <div className="row featurette">
                            <div className="col-md-7">
                                <h2 className="featurette-heading">And lastly, <span className="text-muted">Offers online services.</span></h2>
                                <p className="lead">We offer different convinient services for patients who wants to avail them. Might as well test it out.</p>
                            </div>  
                            <div className="col-md-5">
                                <img src={hospitalWeb} alt="hospita-doctor" className="featurette-responsive featurette-img" />
                            </div>
                        </div>

                        <hr className="featurette-divider" />

                    </div>

                    <footer className="container">
                        <p className="float-end"><a href="#">Back to top</a></p>
                        <p>&copy; 2017–2021 Hospital, Inc. &middot; <a href="#">Privacy</a> &middot; <a href="#">Developed by halfscale</a></p>
                    </footer>
                </main>
            </>
        );
    }
}

export default Home;
