import * as React from "react";
import {Button, FormGroup, FormControl, ControlLabel, Jumbotron} from "react-bootstrap";
import "./login.css";

export default class Login extends React.Component<{},any> {
  constructor(props: {}) {
    super(props);

    this.state = {
      username: "",
      password: ""
    };
  }

  public render() {
    return (
      <div className="Login">
        <Jumbotron className="loginHeader">
          <h2>Ecma Events</h2>
          <p>
           Plataforma para organização e gestão de eventos.
          </p>
        </Jumbotron>;
        <form onSubmit={this.handleSubmit}>
          <FormGroup controlId="username" bsSize="large">
            <ControlLabel>Username</ControlLabel>
            <FormControl
              autoFocus
              type="text"
              value={this.state.username}
              onChange={this.handleChange}
            />
          </FormGroup>
          <FormGroup controlId="password" bsSize="large">
            <ControlLabel>Password</ControlLabel>
            <FormControl
              value={this.state.password}
              onChange={this.handleChange}
              type="password"
            />
          </FormGroup>
          <Button
            block
            bsSize="large"
            bsStyle="success"
            disabled={!this.validateForm()}
            type="submit"
          >
            Entrar
          </Button>
        </form>
      </div>
    );
  }

  private validateForm() {
    return this.state.username.length > 0 && this.state.password.length > 0;
  }

  private handleChange = (event: any) => {
    this.setState({
      [event.target.id]: event.target.value
    });
  };

  private handleSubmit = (event: any) => {
    event.preventDefault();
  }
}
