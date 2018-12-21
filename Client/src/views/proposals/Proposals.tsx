import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { itemSelected } from '../../actions/items';
import ListWithControllers from '../common/ListWithControllers';

import { Button, Modal, DropdownButton, MenuItem, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';

export interface IProposalProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class ProposalList extends React.Component<IProposalProps,any> {

  constructor(props: IProposalProps) {
    super(props);
    this.state = { title: "", description: "" }

    this.handleSave = this.handleSave.bind(this);
    this.createProposal = this.createProposal.bind(this);
    this.updateProposal = this.updateProposal.bind(this);
    this.deleteProposal = this.deleteProposal.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onDropdownChange = this.onDropdownChange.bind(this);
  }

  public componentWillReceiveProps(nextProps: IProposalProps) {
    if (nextProps.itemSelected) {
      const { title, description, state } = nextProps.itemSelected;
      this.setState({ title, description, state });
    }
  }

  public handleModal(openModal: boolean, toCreate: boolean) {
    if (toCreate) {
      this.props.selectItem({
        id: -1,
        title: "",
        description: "",
        state: "PENDING_APPROVAL",
        creationDate: "",
        _links: ""
      });
    }
    this.props.changeModalStatus(openModal);
  }

  public handleSave() {
    if (this.props.itemSelected.id) {
      this.updateProposal()
    } else {
      this.createProposal()
    }
  }

  public createProposal() {
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./proposals.json', 'POST', formData, 'Created with success!');
  }

  public updateProposal() {
    // const proposal = this.props.proposalSelected;
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./proposals.json', 'PUT', formData, 'Updated with success!');
  }

  public deleteProposal() {
    // const { id } = this.props.proposalSelected;
    this.fetchUrl('./proposals.json', 'DELETE', new FormData(), 'Deleted with success!');
  }

  public fetchUrl(url: string, method: string, body: any, successMessage: string) {
    fetch(url, {
      method,
      body,
      headers: new Headers({
         'Authorization': 'Basic '+btoa('admin:password'),
       }),
    })
    .then(response => {
      if (response.ok) {
        this.props.changeModalStatus(false);
        alert(successMessage);
      } else {
        throw new Error(response.statusText);
      }
    }).catch((e: string) => alert(e));
  }

  public onChange(e: any) {
    this.setState({ [e.target.name]: e.target.value });
  }

  public onDropdownChange(e: any) {
    this.setState({ state: e });
  }

  public render() {
    const getStateValue = (state: string) => state === "PENDING_APPROVAL" ? "Pending approval" : "Approved";

    return (
      <Fragment>
        <ListWithControllers
          title="Proposals"
          fetchFrom="/proposals.json"
          embeddedArray="proposals"
          show={this.show}
          handleAdd={() => this.handleModal(true,true)}
          handleUpdate={() => this.handleModal(true,false)}
          handleDelete={this.deleteProposal}
        />
        {
          this.props.modalOpen &&
          <Modal.Dialog>
            <Modal.Header>
              <Modal.Title>Update proposal</Modal.Title>
            </Modal.Header>

            <Modal.Body>
              <FormGroup>
                <ControlLabel>Title</ControlLabel>
                <FormControl
                  name="title"
                  type="text"
                  label="Title"
                  value={this.state.title}
                  onChange={this.onChange}
                />
                <ControlLabel>Description</ControlLabel>
                <FormControl
                  componentClass="textarea"
                  name="description"
                  value={this.state.description}
                  onChange={this.onChange}
                />
                <DropdownButton
                  id="dropdown-basic-0"
                  name="state"
                  onSelect={this.onDropdownChange}
                  title={getStateValue(this.state.state)}>
                    <MenuItem
                      eventKey="PENDING_APPROVAL"
                      active={this.state.state === "PENDING_APPROVAL"}>
                        Pending approval
                    </MenuItem>
                    <MenuItem
                      eventKey="APPROVED"
                      active={this.state.state === "APPROVED"}>
                        Approved
                    </MenuItem>
                </DropdownButton>
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

  private show = (p: any) => `${p.title}: (${p.description})`;
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

export default connect(mapStateToProps, mapDispatchToProps)(ProposalList);
