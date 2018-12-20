import React from "react";
import classNames from "classnames";
import { NavLink } from "react-router-dom";
import { Navbar, ListGroup, ListGroupItem } from "react-bootstrap"

import image from "../../resources/images/sidebar.jpg";
import logo from "../../logo.svg"

import classes from "./sidebar.css"

function Sidebar(props) { //TODO props type

    const { routes } = props;

    const activeRoute = routeName => {
        return props.location.pathname.indexOf(routeName) > -1;
    };

    const links = (
        routes.map((prop, key) => {
            return (
                <NavLink
                    to={prop.path}
                    className={classes.item}
                    activeClassName="active"
                    key={key}>
                    <ListGroupItem button className={classNames(classes.itemLink, {"blue": activeRoute(prop.path)})}>
                        {/*<ListItemIcon className={classNames("itemIcon", {"whiteFont": activeRoute(prop.path)})}>*/}
                        <img src={prop.icon} alt="logo" className="img"/>
                        {/*</ListItemIcon>*/}
                        <div className={classNames(classes.itemText, {"whiteFont": activeRoute(prop.path)})}>
                            {prop.name}
                        </div>
                    </ListGroupItem>
                </NavLink>
            );
        })
    );

    const brand = (
        <div className={classes.logo}>
            <a className={classes.logoLink}>
                <div className={classes.logoImage}>
                    <img src={logo} alt="logo" className="img"/>
                </div>
                "Ecma Events"
            </a>
        </div>
    );

    return (
        <div>
            <Navbar className={classes.drawerPaper}>
                {brand}
                <div className={classes.sidebarWrapper}>
                    <ListGroup>
                        {links}
                    </ListGroup>
                </div>
                <div className={classes.background} style={{backgroundImage: "url(" + image + ")"}}/>
            </Navbar>
        </div>
    );
}

export default Sidebar
