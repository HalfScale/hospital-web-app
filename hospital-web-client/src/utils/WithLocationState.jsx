import { useLocation } from 'react-router-dom'

function withLocationState(Component) {
    return props => <Component {...props} location={useLocation()}/>
}

export default withLocationState;