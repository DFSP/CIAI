import * as React from 'react';
import Proposals from './proposals/Proposals';
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
        </Nav>
      </Navbar>
        <div>
          <Route path="/proposals" component={Proposals} />
        </div>
      </div>
    );
  }
}

export default App;
