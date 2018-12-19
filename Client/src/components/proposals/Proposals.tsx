import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { itemsFetchData, modalStatusChanged, proposalSelected } from '../../actions/items';
import { Button, Modal, DropdownButton, MenuItem } from 'react-bootstrap';
import './proposals.css'

export interface IProposal {
  id: number;
  title: string;
  description: string;
  state: string;
  creationDate: string;
  _links: any;
}

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

class ProposalList extends React.Component<IProposalProps,{}> {

  constructor(props: IProposalProps) {
    super(props);
    this.updateProposal = this.updateProposal.bind(this);
  }

  public componentDidMount() {
    this.props.fetchData();
  }

  public handleModal(status: boolean, proposal?: IProposal) {
    this.props.changeModalStatus(status);
    if (status && proposal) {
      this.props.selectProposal(proposal);
    }
  }

  public updateProposal() {
    const proposal = this.props.proposalSelected;
    const formData = new FormData();
    formData.append('title', proposal.title);
    formData.append('description', proposal.description);
    formData.append('state', proposal.state);

    fetch('/proposals.json', {
      method: 'PUT',
      body: formData,
      headers: new Headers({
         'Authorization': 'Basic '+btoa('admin:password'),
       }),
    })
    .then(response => {
      if (response.ok) {
        this.props.changeModalStatus(false);
        alert('Updated with success!');
      } else {
        throw new Error(response.statusText);
      }
    }).catch((e: string) => alert(e))
  }

  public deleteProposal(id: number) {
    fetch('/proposals.json', {
      method: 'DELETE',
      headers: new Headers({
         'Authorization': 'Basic '+btoa('admin:password'),
       }),
    })
    .then(response => {
      if (response.ok) {
        alert('Deleted with success!');
      } else {
        throw new Error(response.statusText);
      }
    }).catch((e: string) => alert(e))
  }

  public render() {
    if (this.props.hasErrored) {
      return <p>Sorry! There was an error loading the items.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <Fragment>
        <ul>
          {
            this.props.proposals && this.props.proposals.map(p => (
              <li key={p.id}>
                {p.title}
                <button key={p.id} onClick={() => this.handleModal(true, p)}>Update</button>
                <button key={p.title} onClick={() => this.deleteProposal(p.id)}>Delete</button>
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
              <p>Title: <input type="text" /></p>
              <p>Description: <input type="text" /></p>
              <p><DropdownButton id="dropdown-basic-0" title="State">
                <MenuItem eventKey="1" active>Pending</MenuItem>
                <MenuItem eventKey="2">Approved</MenuItem>
              </DropdownButton></p>
            </Modal.Body>

            <Modal.Footer>
              <Button onClick={() => this.handleModal(false)}>Close</Button>
              <Button onClick={this.updateProposal} bsStyle="primary">Save changes</Button>
            </Modal.Footer>
          </Modal.Dialog>
        }
      </Fragment>
    );
  }
}

const mapStateToProps = (state: any) => {
  console.log(state);
    return {
        proposals: state.items,
        hasErrored: state.itemsHasErrored,
        isLoading: state.itemsIsLoading,
        modalOpen: state.modalStatusChanged,
        proposalSelected: state.proposalSelected
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(itemsFetchData()),
        changeModalStatus: (status: boolean) => dispatch(modalStatusChanged(status)),
        selectProposal: (proposal: IProposal) => dispatch(proposalSelected(proposal))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ProposalList);
