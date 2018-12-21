import * as React from 'react';

import Proposals from "../views/proposals/Proposals";
import Companies from '../views/companies/Companies';
import { Route } from 'react-router-dom';
import { Navbar, Nav, NavItem } from 'react-bootstrap';
import './App.css';


class App extends React.Component {
  public render() {
    return (
      <div>
      <Navbar>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="/">ECMA</a>
          </Navbar.Brand>
        </Navbar.Header>
        <Nav>
          <NavItem eventKey={1} href="/proposals">
            Proposals
          </NavItem>
            <NavItem eventKey={2} href="/companies">
                Companies
            </NavItem>
        </Nav>
      </Navbar>
        <div>
          <Route path="/proposals" component={Proposals} />
            <Route path="/companies" component={Companies} />
        </div>
      </div>
    );
  }
}

export default App;
