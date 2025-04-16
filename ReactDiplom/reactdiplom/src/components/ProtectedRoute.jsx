// ProtectedRoute.jsx
import {Navigate, Outlet, useLocation} from 'react-router-dom';

const ProtectedRoute = () => {
    const token = localStorage.getItem('isFirstEnterToken');
    const location = useLocation();

    if (!token) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;