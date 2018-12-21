import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { itemSelected } from '../../actions/items';
import { fetchUrl } from '../../utils/utils';
import ListWithControllers from '../common/ListWithControllers';

import { Modal, FormGroup, ControlLabel, FormControl, DropdownButton, MenuItem, Button } from 'react-bootstrap';

export interface IUsersProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class UsersList extends React.Component<IUsersProps,any> {

  constructor(props: IUsersProps) {
    super(props);
    this.state = { firstName: "", lastName: "", username: "", email: "", role: "",
    city: "", address: "", zipCode: "", homePhone: "", cellPhone: "",
    gender: "", salary: "", birthday: "" };

    this.handleSave = this.handleSave.bind(this);
    this.createUser = this.createUser.bind(this);
    this.updateUser = this.updateUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onDropdownChange = this.onDropdownChange.bind(this);
  }

  public componentWillReceiveProps(nextProps: IUsersProps) {
    if (nextProps.itemSelected) {
      const { firstName, lastName, username, email, role, city, address, zipCode,
        homePhone, cellPhone, gender, salary, birthday} = nextProps.itemSelected;

      this.setState({ firstName, lastName, username, email, role, city, address, zipCode,
        homePhone, cellPhone, gender, salary, birthday});
    }
  }

  public handleModal(openModal: boolean, toCreate: boolean) {
    if (toCreate) {
      this.props.selectItem({
        id: -1,
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        role: "",
        city: "",
        address: "",
        zipCode: "",
        homePhone: "",
        cellPhone: "",
        gender: "",
        salary: "",
        birthday: ""
      });
    }
    this.props.changeModalStatus(openModal);
  }

  public handleSave() {
    if (this.props.itemSelected.id) {
      this.updateUser()
    } else {
      this.createUser()
    }
  }

  public createUser() {
    const formData = new FormData();
    const { firstName, lastName, username, email, role, city, address, zipCode,
      homePhone, cellPhone, gender, salary, birthday} = this.state;
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('username', username);
    formData.append('email', email);
    formData.append('role', role);
    formData.append('city', city);
    formData.append('address', address);
    formData.append('zipCode', zipCode);
    formData.append('homePhone', homePhone);
    formData.append('cellPhone', cellPhone);
    formData.append('gender', gender);
    formData.append('salary', salary);
    formData.append('birthday', birthday);
    fetchUrl('./users.json', 'POST', formData, 'Created with success!', this.handleModal);
  }

  public updateUser() {
    // const proposal = this.props.proposalSelected;
    const formData = new FormData();
    const { firstName, lastName, username, email, role, city, address, zipCode,
      homePhone, cellPhone, gender, salary, birthday} = this.state;
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('username', username);
    formData.append('email', email);
    formData.append('role', role);
    formData.append('city', city);
    formData.append('address', address);
    formData.append('zipCode', zipCode);
    formData.append('homePhone', homePhone);
    formData.append('cellPhone', cellPhone);
    formData.append('gender', gender);
    formData.append('salary', salary);
    formData.append('birthday', birthday);
    fetchUrl('./users.json', 'PUT', formData, 'Updated with success!', this.handleModal);
  }

  public deleteUser() {
    // const { id } = this.props.proposalSelected;
    fetchUrl('./users.json', 'DELETE', new FormData(), 'Deleted with success!', this.handleModal);
  }

  public onChange(e: any) {
    this.setState({ [e.target.name]: e.target.value });
  }

  public onDropdownChange(e: any) {
    this.setState({ state: e });
  }

  public render() {
    return (
      <Fragment>
        <ListWithControllers
          title="Users"
          fetchFrom="/users.json"
          embeddedArray="employees"
          show={this.show}
          handleAdd={() => this.handleModal(true,true)}
          handleUpdate={() => this.handleModal(true,false)}
          handleDelete={this.deleteUser}
        />
        {
          this.props.modalOpen &&
          <Modal.Dialog>
            <Modal.Header>
              <Modal.Title>Update User</Modal.Title>
            </Modal.Header>

            <Modal.Body style={{maxHeight: 'calc(100vh - 210px)', overflowY: 'auto'}}>
              <FormGroup>
              <ControlLabel>First name</ControlLabel>
              <FormControl
                name="firstName"
                type="text"
                label="First name"
                value={this.state.firstName}
                onChange={this.onChange}
              />
              <ControlLabel>Last name</ControlLabel>
              <FormControl
                name="lastName"
                type="text"
                label="Last name"
                value={this.state.lastName}
                onChange={this.onChange}
              />
              <ControlLabel>Username</ControlLabel>
              <FormControl
                name="userName"
                type="text"
                label="First name"
                value={this.state.username}
                onChange={this.onChange}
              />
              <ControlLabel>Email</ControlLabel>
              <FormControl
                name="email"
                type="text"
                label="Email"
                value={this.state.email}
                onChange={this.onChange}
              />
              <DropdownButton
                id="dropdown-basic-0"
                name="state"
                onSelect={this.onDropdownChange}
                title={this.state.role}>
                  <MenuItem
                    eventKey="ROLE_COMPANY_ADMIN"
                    active={this.state.role === "ROLE_COMPANY_ADMIN"}>
                      ROLE_COMPANY_ADMIN
                  </MenuItem>
                  <MenuItem
                    eventKey="SYS_ADMIN"
                    active={this.state.role === "SYS_ADMIN"}>
                      SYS_ADMIN
                  </MenuItem>
                  <MenuItem
                    eventKey="STANDARD"
                    active={this.state.role === "STANDARD"}>
                      STANDARD
                  </MenuItem>
              </DropdownButton><br />
              <ControlLabel>City</ControlLabel>
              <FormControl
                name="city"
                type="text"
                label="City"
                value={this.state.city}
                onChange={this.onChange}
              />
              <ControlLabel>Address</ControlLabel>
              <FormControl
                name="address"
                type="text"
                label="Address"
                value={this.state.address}
                onChange={this.onChange}
              />
              <ControlLabel>Zipcode</ControlLabel>
              <FormControl
                name="zipCode"
                type="text"
                label="Zipcode"
                value={this.state.zipCode}
                onChange={this.onChange}
              />
              <ControlLabel>Cellphone</ControlLabel>
              <FormControl
                name="cellPhone"
                type="text"
                label="Cellphone"
                value={this.state.cellPhone}
                onChange={this.onChange}
              />
              <ControlLabel>Homephone</ControlLabel>
              <FormControl
                name="homePhone"
                type="text"
                label="Homephone"
                value={this.state.homePhone}
                onChange={this.onChange}
              />
              <DropdownButton
                id="dropdown-basic-0"
                name="state"
                onSelect={this.onDropdownChange}
                title={this.state.gender}>
                  <MenuItem
                    eventKey="MALE"
                    active={this.state.gender === "MALE"}>
                      MALE
                  </MenuItem>
                  <MenuItem
                    eventKey="FEMALE"
                    active={this.state.gender === "FEMALE"}>
                      FEMALE
                  </MenuItem>
              </DropdownButton><br />
              <ControlLabel>Salary</ControlLabel>
              <FormControl
                name="salary"
                type="text"
                label="Salary"
                value={this.state.salary}
                onChange={this.onChange}
              />
              <ControlLabel>Birthday</ControlLabel>
              <FormControl
                name="birthday"
                type="text"
                label="Birthday"
                value={this.state.birthday}
                onChange={this.onChange}
              />
              </FormGroup>
            </Modal.Body>

            <Modal.Footer>
            <Button onClick={() => this.handleModal(false,false)}>Close</Button>
            <Button onClick={this.handleSave} bsStyle="primary">Save changes</Button>
            </Modal.Footer>
          </Modal.Dialog>
        }
      </Fragment>
    );
  }

  private show = (p: any) => `${p.firstName}: (${p.lastName})`;
}

const mapStateToProps = (state: any) => {
    return {
        modalOpen: state.modalStatusChanged,
        itemSelected: state.itemSelected,
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        changeModalStatus: (status: boolean) => dispatch(modalStatusChanged(status)),
        selectItem: (item: any) => dispatch(itemSelected(item))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(UsersList);
