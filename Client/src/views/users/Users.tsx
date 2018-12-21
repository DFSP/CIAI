import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { itemSelected } from '../../actions/items';
import ListWithControllers from '../common/ListWithControllers';

import { Modal, FormGroup } from 'react-bootstrap';

export interface IUsersProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class UsersList extends React.Component<IUsersProps,any> {

  constructor(props: IUsersProps) {
    super(props);
    this.state = { title: "", description: "" }

    this.handleSave = this.handleSave.bind(this);
    this.createUser = this.createUser.bind(this);
    this.updateUser = this.updateUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onDropdownChange = this.onDropdownChange.bind(this);
  }

  public componentWillReceiveProps(nextProps: IUsersProps) {
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
      this.updateUser()
    } else {
      this.createUser()
    }
  }

  public createUser() {
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./users.json', 'POST', formData, 'Created with success!');
  }

  public updateUser() {
    // const proposal = this.props.proposalSelected;
    const formData = new FormData();
    const { title, description, state } = this.state;
    formData.append('title', title);
    formData.append('description', description);
    formData.append('state', state);
    this.fetchUrl('./users.json', 'PUT', formData, 'Updated with success!');
  }

  public deleteUser() {
    // const { id } = this.props.proposalSelected;
    this.fetchUrl('./users.json', 'DELETE', new FormData(), 'Deleted with success!');
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

            <Modal.Body>
              <FormGroup>
                Users Form Group
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
