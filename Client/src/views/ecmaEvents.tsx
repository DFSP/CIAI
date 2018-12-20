import * as React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './ecmaEvents.css';

import routes from "../routes/index.jsx";

const switchRoutes = (
    <Switch>
        {routes.map((prop, key) => {
            return <Route path={prop.path} component={prop.component} key={key} />;
        })}
    </Switch>
);

class EcmaEvents extends React.Component {
    public render() {
        return (
            <div className="App">
                <BrowserRouter basename={"/ecmaEvents"}>
                    {switchRoutes}
                </BrowserRouter>
            </div>
        );
    }
}

export default EcmaEvents;
