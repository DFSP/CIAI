import * as React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './App.css';

import routes from "./routes/index.jsx";

const switchRoutes = (
    <Switch>
        {routes.map((prop, key) => {
            return <Route path={prop.path} component={prop.component} key={key} />;
        })}
    </Switch>
);

class App extends React.Component {
    public render() {
        return (
            <BrowserRouter basename={"/ecmaEvents"}>
                {switchRoutes}
            </BrowserRouter>
        );
    }
}

export default App;
