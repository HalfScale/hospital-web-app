import './styles/main.css'
import { Component } from 'react';
import ReactPaginate from 'react-paginate';
import NotificationsService from '../../services/NotificationsService';
import NotificationPane from './NotificationPane';

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            notifications: [],
            page: 0,
            size: 8,
            sort: 'id,desc',
            totalPages: 0
        }

        this.displayNotifications = this.displayNotifications.bind(this);
        this.toggleAppointment = this.toggleAppointment.bind(this);
        this.toggleNotifications = this.toggleNotifications.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
    }

    componentDidMount() {
        this.toggleNotifications();
    }

    displayNotifications() {
        let { notifications } = this.state;

        if (notifications.length !== 0) {
            return notifications.map(notification => {
                return <NotificationPane
                    key={notification.id}
                    data={notification}
                    toggleAppointment={this.toggleAppointment}
                />
            });
        }
    }

    toggleNotifications() {
        let { page, size, sort} = this.state;

        NotificationsService.getNotificationsByLoggedUser({
            page: page,
            size: size,
            sort: sort
        }).then(resp => {
                console.log('getNotificationsByLoggedUser', resp);
                this.setState({
                    notifications: resp.data.content
                });
            });
    }

    toggleAppointment(id) {
        this.props.navigate(`/appointment/details/${id}`);
    }

    handlePageChange(page) {
        this.setState({
            page: page.selected
        }, () => this.toggleNotifications());
    }

    render() {
        // pagination, handle page
        let { totalPages } = this.state;

        return <>
            <div className="mt-3 m-auto w-50 rounded shadow pb-2">
                <h2 className="text-center pt-3 mb-4 text-muted">Notifications</h2>
                <hr />

                {this.displayNotifications()}

                {
                    totalPages > 1 && <ReactPaginate
                        className="pagination justify-content-center"
                        nextLabel="next >"
                        onPageChange={this.handlePageChange}
                        pageRangeDisplayed={3}
                        marginPagesDisplayed={2}
                        pageCount={totalPages}
                        previousLabel="< prev"
                        pageClassName="page-item"
                        pageLinkClassName="page-link"
                        previousClassName="page-item"
                        previousLinkClassName="page-link"
                        nextClassName="page-item"
                        nextLinkClassName="page-link"
                        breakLabel="..."
                        breakClassName="page-item"
                        breakLinkClassName="page-link"
                        containerClassName="pagination"
                        activeClassName="active"
                        renderOnZeroPageCount={null}
                    />
                }

            </div>
        </>;
    }
}

export default Notifications;
