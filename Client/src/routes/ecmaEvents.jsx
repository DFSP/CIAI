import Proposals from "../views/proposals/Proposals"
import ProposalDetails from "../views/proposals/ProposalDetails"
import Users from "../views/users/Users"
import UserDetails from "../views/users/userDetails"
import Companies from "../views/companies/Companies"
import CompanyDetails from "../views/companies/companyDetails"
import Profile from "../views/profile/profile.jsx"
import MyActivity from "../views/myActivity/myActivity.jsx"

const routes = [
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
