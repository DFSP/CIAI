import Login from "../views/login/login";
import EcmaEvents from "../layouts/ecmaEvents.jsx";

const routes = [
    {
        component: Login,
        path: "/login"
    },
    {
        component: EcmaEvents,
        path: "/"
    }
];

export default routes
