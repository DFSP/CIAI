import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { modalStatusChanged } from '../../actions/modals';
import { itemSelected } from '../../actions/items';
import { fetchUrl } from '../../utils/utils';
import ListWithControllers from '../common/ListWithControllers';
import { IReview } from '../../reducers/proposals';

import {
  Modal,
  Button,
  Panel,
  ButtonToolbar
} from 'react-bootstrap';

export interface IReviewProps {
  changeModalStatus: (status: boolean) => void;
  modalOpen: boolean;
  itemSelected: any;
  selectItem: (item: any) => void;
}

class ReviewsList extends React.Component<IReviewProps,any> {

  constructor(props: IReviewProps) {
    super(props);
    this.state = { name: "", city: "", zipCode: "", address: "", phone: "", email: "", fax: "" }
  }

  public componentWillReceiveProps(nextProps: IReviewProps) {
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
              handleDelete={this.deleteReview}
          />
          {
            this.props.modalOpen &&
            <Modal.Dialog>
              <Modal.Header>
                <Modal.Title>Update review</Modal.Title>
              </Modal.Header>

              <Modal.Body>
                Reviews modal body
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
      this.updateReview()
    } else {
      this.createReview()
    }
  };

  private createReview = () => {
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

  private updateReview = () => {
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

  private deleteReview = () => {
    // const { id } = this.props.proposalSelected;
    fetchUrl('./companies.json', 'DELETE', new FormData(), 'Deleted with success!', this.handleModal);
  };

  /* private onChange = (e: any) => {
    this.setState({ [e.target.name]: e.target.value });
  }; */

  private predicate = (c:IReview,s:string) => (String(c.title)+String(c.text)).indexOf(s) !== -1;

  private show = (p: IReview) =>
      <div>
        <Panel.Heading>
          <Panel.Title toggle>{p.title}</Panel.Title>
        </Panel.Heading>
        <Panel.Body>
          {p.text}
        </Panel.Body>
        <Panel.Body collapsible>
          <ButtonToolbar>
            <Button onClick={() => this.handleModal(true,false)}>Atualizar</Button>
            <Button onClick={this.deleteReview}>Apagar</Button>
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

export default connect(mapStateToProps, mapDispatchToProps)(ReviewsList);
