import Clippy from "../resources/icons/clippy.svg"
import Person from "../resources/icons/person.svg"
import Organization from "../resources/icons/organization.svg"
import Proposals from "../views/proposals/Proposals.tsx"
import Users from "../views/users/Users.tsx"
import Companies from "../views/companies/Companies.tsx"

const routes = [
    {
        component: Proposals,
        icon: Clippy,
        name: "Propostas",
        path: "/proposals",
    },
    {
        component: Users,
        icon: Person,
        name: "Utilizadores",
        path: "/users",
    },
    {
        component: Companies,
        icon: Organization,
        name: "Companhias",
        path: "/companies",
    }
];

export default routes;
