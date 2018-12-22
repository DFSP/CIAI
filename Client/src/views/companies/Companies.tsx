import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { itemSelected } from '../../actions/items';
import { fetchUrl } from '../../utils/utils';
import ListWithControllers from '../common/ListWithControllers';

import {
  Modal,
  FormGroup,
  ControlLabel,
  FormControl,
  Button,
  Panel,
  ButtonToolbar,
  ButtonGroup
} from 'react-bootstrap';
import {Link} from "react-router-dom";

export interface ICompanyProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class CompaniesList extends React.Component<ICompanyProps,any> {

  constructor(props: ICompanyProps) {
    super(props);
    this.state = { name: "", city: "", zipCode: "", address: "", phone: "", email: "", fax: "" }
  }

  public componentWillReceiveProps(nextProps: ICompanyProps) {
    if (nextProps.itemSelected) {
      const { name, city, zipCode, address, phone, email, fax } = nextProps.itemSelected;
      this.setState({ name, city, zipCode, address, phone, email, fax });
    }
  }

  public render() {
    return (
        <Fragment>
          <ListWithControllers
              fetchFrom="/companies.json"
              embeddedArray="companies"
              show={this.show}
              handleAdd={() => this.handleModal(true,true)}
              handleUpdate={() => this.handleModal(true,false)}
              handleDelete={this.deleteCompany}
          />
          {
            this.props.modalOpen &&
            <Modal.Dialog>
              <Modal.Header>
                <Modal.Title>Update company</Modal.Title>
              </Modal.Header>

              <Modal.Body>
                <FormGroup>
                  <ControlLabel>Name</ControlLabel>
                  <FormControl
                      name="name"
                      type="text"
                      label="Name"
                      value={this.state.name}
                      onChange={this.onChange}
                  />
                  <ControlLabel>City</ControlLabel>
                  <FormControl
                      name="city"
                      type="text"
                      label="City"
                      value={this.state.city}
                      onChange={this.onChange}
                  />
                  <ControlLabel>Zipcode</ControlLabel>
                  <FormControl
                      name="zipcode"
                      type="text"
                      label="Zipcode"
                      value={this.state.zipCode}
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
                  <ControlLabel>Phone</ControlLabel>
                  <FormControl
                      name="phone"
                      type="text"
                      label="Phone"
                      value={this.state.phone}
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
                  <ControlLabel>fax</ControlLabel>
                  <FormControl
                      name="fax"
                      type="text"
                      label="Fax"
                      value={this.state.fax}
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

  private handleModal = (openModal: boolean, toCreate: boolean) => {
    if (toCreate) {
      this.props.selectItem({
        id: -1,
        name: "",
        city: "",
        zipCode: "",
        address: "",
        phone: "",
        email: "",
        fax: ""
      });
    }
    this.props.changeModalStatus(openModal);
  };

  private handleSave = () => {
    if (this.props.itemSelected.id) {
      this.updateCompany()
    } else {
      this.createCompany()
    }
  };

  private createCompany = () => {
    const formData = new FormData();
    const { name, city, zipCode, address, phone, email, fax }  = this.state;
    formData.append('name', name);
    formData.append('city', city);
    formData.append('zipCode', zipCode);
    formData.append('address', address);
    formData.append('phone', phone);
    formData.append('email', email);
    formData.append('fax', fax);
    fetchUrl('./companies.json', 'POST', formData, 'Created with success!', this.handleModal);
  };

  private updateCompany = () => {
    // const proposal = this.props.proposalSelected;
    const formData = new FormData();
    const { name, city, zipCode, address, phone, email, fax }  = this.state;
    formData.append('name', name);
    formData.append('city', city);
    formData.append('zipCode', zipCode);
    formData.append('address', address);
    formData.append('phone', phone);
    formData.append('email', email);
    formData.append('fax', fax);
    fetchUrl('./companies.json', 'PUT', formData, 'Updated with success!', this.handleModal);
  };

  private deleteCompany = () => {
    // const { id } = this.props.proposalSelected;
    fetchUrl('./companies.json', 'DELETE', new FormData(), 'Deleted with success!', this.handleModal);
  };

  private onChange = (e: any) => {
    this.setState({ [e.target.name]: e.target.value });
  };

  private show = (p: any) =>
      <div>
        <Panel.Heading>
          <Panel.Title toggle>{p.name}</Panel.Title>
        </Panel.Heading>
        <Panel.Body>
          {p.city}
        </Panel.Body>
        <Panel.Body collapsible>
          <ButtonToolbar>
            <Button onClick={() => this.handleModal(true,false)}>Atualizar</Button>
            <Button onClick={this.deleteCompany}>Apagar</Button>
            <ButtonGroup>
              <Link to={`/companies/${p.id}/details`}>
                <Button>
                  Ver detalhes
                </Button>
              </Link>
            </ButtonGroup>
          </ButtonToolbar>
        </Panel.Body>
      </div>
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

export default connect(mapStateToProps, mapDispatchToProps)(CompaniesList);
