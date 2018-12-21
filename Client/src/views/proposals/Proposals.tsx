import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link } from "react-router-dom";
import { proposalsFetchData, proposalSelected } from '../../actions/proposals';
import { modalStatusChanged } from '../../actions/modals';
import { IProposal } from '../../reducers/proposals';

import { Button, Modal, DropdownButton, MenuItem, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';

export interface IProposalProps {
  proposals: IProposal[];
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  selectProposal: (proposal: IProposal) => void;
  proposalSelected: IProposal;
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
    if (nextProps.proposalSelected) {
      const { title, description, state } = nextProps.proposalSelected;
      this.setState({ title, description, state });
    }
  }

  public componentDidMount() {
    this.props.fetchData();
  }

  public handleModal(status: boolean, proposal?: IProposal) {
    if (status && proposal) {
      this.props.selectProposal(proposal);
    } else {
      this.props.selectProposal({
        id: -1,
        title: "",
        description: "",
        state: "PENDING_APPROVAL",
        creationDate: "",
        _links: ""
      })
    }
    this.props.changeModalStatus(status);
  }

  public handleSave() {
    if (this.props.proposalSelected.id) {
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

  public deleteProposal(id: number) {
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
    if (this.props.hasErrored) {
      return <p>Sorry! There was an error loading the items.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    const getStateValue = (state: string) => state === "PENDING_APPROVAL" ? "Pending approval" : "Approved";

    return (
      <Fragment>
        <Button onClick={() => this.handleModal(true)}>Add new</Button>
        <ul>
          {
            this.props.proposals && this.props.proposals.map(p => (
              <li key={p.id}>
                <Link key={`${p.id}_link`} to={`/proposals/proposalDetails/${p.id}`}>{p.title}</Link>
                <button key={`${p.id}_updateBtn`} onClick={() => this.handleModal(true, p)}>Update</button>
                <button key={`${p.id}_deleteBtn`} onClick={() => this.deleteProposal(p.id)}>Delete</button>
              </li>
            ))
          }
        </ul>
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
              <Button onClick={() => this.handleModal(false)}>Close</Button>
              <Button onClick={this.handleSave} bsStyle="primary">Save changes</Button>
            </Modal.Footer>
          </Modal.Dialog>
        }
      </Fragment>
    );
  }
}

const mapStateToProps = (state: any) => {
    return {
        proposals: state.proposals,
        hasErrored: state.proposalsHasErrored,
        isLoading: state.proposalsIsLoading,
        modalOpen: state.modalStatusChanged,
        proposalSelected: state.proposalSelected,
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(proposalsFetchData()),
        changeModalStatus: (status: boolean) => dispatch(modalStatusChanged(status)),
        selectProposal: (proposal: IProposal) => dispatch(proposalSelected(proposal))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ProposalList);
