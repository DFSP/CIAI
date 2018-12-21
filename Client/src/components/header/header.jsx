import React from "react";

import "./header.css";
import Navbar from "react-bootstrap/lib/Navbar";
import Nav from "react-bootstrap/lib/Nav";
import NavItem from "react-bootstrap/lib/NavItem";
import NavDropdown from "react-bootstrap/lib/NavDropdown";
import MenuItem from "react-bootstrap/lib/MenuItem";
import { Link } from 'react-router-dom';

import avatar from "../../resources/images/avatar.jpg";
import signOut from "../../resources/icons/signOut.svg";

function Header(props) {

    function makeBrand() {
        let name;
        props.routes.map((route, key) => { //TODO interface
            if (route.path === props.location.pathname) {
                name = route.name;
            }
            if (route.path.indexOf("/proposals/proposalDetails/") > -1 &&
                props.location.pathname.indexOf("/proposals/proposalDetails/") > -1) {
                name = route.name;
            }
            return null;
        });
        return name;
    }

    return (
        <Navbar collapseOnSelect center>
            <Navbar.Header>
                <Navbar.Brand>
                    <a>{makeBrand()}</a>
                </Navbar.Brand>
                <Navbar.Toggle />
            </Navbar.Header>
            <Navbar.Collapse>
                <Nav pullRight>
                    <NavDropdown eventKey={3}
                                 title={
                                     <img
                                         src={avatar}
                                         className="avatar"
                                         alt="profile"/>
                                 }
                                 id="basic-nav-dropdown"
                                 noCaret>
                            <MenuItem componentClass={Link} href="/myActivity" to="/myActivity" eventKey={3.1}>
                                A minha atividade
                            </MenuItem>
                        <MenuItem divider />
                            <MenuItem componentClass={Link} href="/profile" to="/profile" eventKey={3.2}>
                                Perfil
                            </MenuItem>
                        <MenuItem divider />
                        <MenuItem eventKey={3.3}>
                            <img src={signOut} className="icon" alt="logo"/> Sair
                        </MenuItem>
                    </NavDropdown>
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
}

export default Header
