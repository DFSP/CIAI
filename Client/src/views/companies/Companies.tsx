import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { proposalSelected } from '../../actions/proposals';
import { IProposal } from '../../reducers/proposals';
import ListWithControllers from '../common/ListWithControllers';

import { Modal, FormGroup } from 'react-bootstrap';

export interface ICompanyProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  proposalSelected: IProposal;
  selectProposal: (proposal: IProposal) => void;
}

class CompaniesList extends React.Component<ICompanyProps,any> {

  constructor(props: ICompanyProps) {
    super(props);
    this.state = { title: "", description: "" }

    this.handleSave = this.handleSave.bind(this);
    this.createCompany = this.createCompany.bind(this);
    this.updateCompany = this.updateCompany.bind(this);
    this.deleteCompany = this.deleteCompany.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onDropdownChange = this.onDropdownChange.bind(this);
  }

  public componentWillReceiveProps(nextProps: ICompanyProps) {
    if (nextProps.proposalSelected) {
      const { title, description, state } = nextProps.proposalSelected;
      this.setState({ title, description, state });
    }
  }

  public handleModal(openModal: boolean, toCreate: boolean) {
    if (toCreate) {
      this.props.selectProposal({
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
    if (this.props.proposalSelected.id) {
      this.updateCompany()
    } else {
      this.createCompany()
    }
  }

  public createCompany() {
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./companies.json', 'POST', formData, 'Created with success!');
  }

  public updateCompany() {
    // const proposal = this.props.proposalSelected;
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./companies.json', 'PUT', formData, 'Updated with success!');
  }

  public deleteCompany() {
    // const { id } = this.props.proposalSelected;
    this.fetchUrl('./companies.json', 'DELETE', new FormData(), 'Deleted with success!');
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
    return (
      <Fragment>
        <ListWithControllers
          title="Companies"
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
                Companies Form Group
              </FormGroup>
            </Modal.Body>

            <Modal.Footer>
              Footer here
            </Modal.Footer>
          </Modal.Dialog>
        }
      </Fragment>
    );
  }

  private show = (p: any) => `${p.name}: (${p.city})`;
}

const mapStateToProps = (state: any) => {
    return {
        modalOpen: state.modalStatusChanged,
        proposalSelected: state.proposalSelected,
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        changeModalStatus: (status: boolean) => dispatch(modalStatusChanged(status)),
        selectProposal: (proposal: any) => dispatch(proposalSelected(proposal))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(CompaniesList);
