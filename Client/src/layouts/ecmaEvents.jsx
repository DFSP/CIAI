import React from "react";
import Header from "../components/header/header.jsx";
import Footer from "../components/footer/footer.jsx";
import Sidebar from "../components/sidebar/sidebar.jsx";
import {Redirect, Route, Switch} from 'react-router-dom';

import routes from "../routes/ecmaEvents.jsx";

import classes from "./ecmaEvents.css"

const switchRoutes = (
    <Switch>
        {routes.map((prop, key) => {
            if (prop.redirect) {
                return <Redirect from={prop.path} to={prop.to} key={key} />;
            } else {
                return <Route path={prop.path} component={prop.component} key={key} />;
            }
        })}
    </Switch>
);

function EcmaEvents(props) { //TODO props type
    return (
        <div className={classes.wrapper}>
            <Sidebar
                routes={routes}
                location={props.location}
            />
            <div className={classes.mainPanel}>
                <Header
                    routes={switchRoutes}
                />
                <div className={classes.content}>
                    <div className={classes.container}>{switchRoutes}</div>
                </div>
                <Footer/>
            </div>
        </div>
    );
}

export default EcmaEvents
