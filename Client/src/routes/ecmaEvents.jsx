import Clippy from "../resources/icons/clippy.svg"
import Person from "../resources/icons/person.svg"
import Organization from "../resources/icons/organization.svg"
import Proposals from "../views/proposals/Proposals.tsx"
import ProposalDetails from "../views/proposals/ProposalDetails.tsx"
import Users from "../views/users/Users.tsx"
import UserDetails from "../views/users/userDetails.tsx"
import Companies from "../views/companies/Companies.tsx"
import CompanyDetails from "../views/companies/companyDetails.tsx"
import Profile from "../views/profile/profile.jsx"
import MyActivity from "../views/myActivity/myActivity.jsx"
import Login from "../views/login/login.tsx"

const routes = [
    {
        component: Login,
        name: "Login",
        path: "/login",
    },
    {
        component: Proposals,
        name: "Propostas",
        path: "/proposals",
    },
    {
        component: ProposalDetails,
        name: "Detalhes da Proposta",
        path: "/proposals/:id/details",
    },
    {
        component: Users,
        name: "Utilizadores",
        path: "/users",
    },
    {
        component: UserDetails,
        name: "Detalhes do Utilizador",
        path: "/users/:id/details",
    },
    {
        component: Companies,
        name: "Companhias",
        path: "/companies",
    },
    {
        component: CompanyDetails,
        name: "Detalhes da Companhia",
        path: "/companies/:id/details",
    },
    {
        component: Profile,
        name: "Perfil",
        path: "/profile",
    },
    {
        component: MyActivity,
        name: "A minha atividade",
        path: "/myActivity",
    },
    { redirect: true, path: "/", to: "/proposals", name: "Redirect" }
];

export default routes;
