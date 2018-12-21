import Clippy from "../resources/icons/clippy.svg"
import Person from "../resources/icons/person.svg"
import Organization from "../resources/icons/organization.svg"
import Proposals from "../views/proposals/proposals.jsx"
import ProposalDetails from "../views/proposals/proposalDetails.jsx"
import Users from "../views/users/users.jsx"
import UserDetails from "../views/users/userDetails.jsx"
import Companies from "../views/companies/companies.jsx"
import CompanyDetails from "../views/companies/companyDetails.jsx"
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
        path: "/proposals/proposalDetails",
    },
    {
        component: Users,
        name: "Utilizadores",
        path: "/users",
    },
    {
        component: UserDetails,
        name: "Detalhes do Utilizador",
        path: "/users/userDetails",
    },
    {
        component: Companies,
        name: "Companhias",
        path: "/companies",
    },
    {
        component: CompanyDetails,
        name: "Detalhes da Companhia",
        path: "/companies/companyDetails",
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
