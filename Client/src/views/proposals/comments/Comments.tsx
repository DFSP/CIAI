import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../../actions/modals';
import { itemSelected } from '../../../actions/items';
import { fetchUrl } from '../../../utils/utils';
import ListWithControllers from '../../common/ListWithControllers';
import { IComment } from '../../../reducers/items'

import {
  Modal,
  Button,
  Panel,
  ButtonToolbar
} from 'react-bootstrap';

export interface ICommentProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class CommentsList extends React.Component<ICommentProps,any> {

  constructor(props: ICommentProps) {
    super(props);
    this.state = { name: "", city: "", zipCode: "", address: "", phone: "", email: "", fax: "" }
  }

  public componentWillReceiveProps(nextProps: ICommentProps) {
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
              predicate={this.predicate}
              handleAdd={() => this.handleModal(true,true)}
              handleUpdate={() => this.handleModal(true,false)}
              handleDelete={this.deleteComment}
          />
          {
            this.props.modalOpen &&
            <Modal.Dialog>
              <Modal.Header>
                <Modal.Title>Update comment</Modal.Title>
              </Modal.Header>

              <Modal.Body>
                Comments modal body
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
      this.updateComment()
    } else {
      this.createComment()
    }
  };

  private createComment = () => {
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

  private updateComment = () => {
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

  private deleteComment = () => {
    // const { id } = this.props.proposalSelected;
    fetchUrl('./companies.json', 'DELETE', new FormData(), 'Deleted with success!', this.handleModal);
  };

  /* private onChange = (e: any) => {
    this.setState({ [e.target.name]: e.target.value });
  }; */

  private predicate = (c:IComment,s:string) => (String(c.title)+String(c.text)).indexOf(s) !== -1;

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
            <Button onClick={this.deleteComment}>Apagar</Button>
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

export default connect(mapStateToProps, mapDispatchToProps)(CommentsList);
