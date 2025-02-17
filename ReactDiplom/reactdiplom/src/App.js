import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Suspense, lazy } from "react";
import Navbar from "./components/Shared/Navbar";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Loader from "./components/Shared/Loader"; // Добавим компонент Loader для улучшения UX
import "./styles/global.css";

// Ленивая загрузка страниц
const Home = lazy(() => import("./pages/HomePage"));
const Profile = lazy(() => import("./pages/ProfilePage"));
const Friends = lazy(() => import("./pages/FriendsPage"));
const Subscriptions = lazy(() => import("./pages/SubscriptionsPage"));
const Login = lazy(() => import("./components/Auth/LoginForm"));
const Register = lazy(() => import("./pages/RegisterPage"));
const NotFound = lazy(() => import("./pages/NotFoundPage"));

// Создаем клиент для React Query
const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false, // Отключаем повторный запрос при фокусе окна
            retry: 1, // Количество попыток повторного запроса при ошибке
        },
    },
});

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <Router>
                <AuthProvider>
                    <Navbar />
                    <Suspense fallback={<Loader />}> {/* Используем компонент Loader для загрузки */}
                        <Routes>
                            {/* Публичные маршруты */}
                            <Route path="/" element={<Home />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />

                            {/* Защищенные маршруты */}
                            <Route
                                path="/profile"
                                element={
                                    <ProtectedRoute>
                                        <Profile />
                                    </ProtectedRoute>
                                }
                            />
                            <Route
                                path="/friends"
                                element={
                                    <ProtectedRoute>
                                        <Friends />
                                    </ProtectedRoute>
                                }
                            />
                            <Route
                                path="/subscriptions"
                                element={
                                    <ProtectedRoute>
                                        <Subscriptions />
                                    </ProtectedRoute>
                                }
                            />

                            {/* Маршрут для 404 */}
                            <Route path="*" element={<NotFound />} />
                        </Routes>
                    </Suspense>
                </AuthProvider>
            </Router>
        </QueryClientProvider>
    );
}

export default App;