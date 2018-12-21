import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { itemsFetchData, itemSelected } from '../../actions/items';
import SimpleList from '../common/SimpleList';

import { Button } from 'react-bootstrap';

export interface IItemProps {
  items: any[];
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: (url: string, embeddedArray: string) => void;
  selectItem: (item: any) => void;
  itemSelected: any;
  handleAdd: () => void;
  handleUpdate: () => void;
  handleDelete: () => void;
  show: (s: any) => string;
  title: string;
  fetchFrom: string;
  embeddedArray: string;
}

class ListWithControllers extends React.Component<IItemProps,any> {

  public componentDidMount() {
    this.props.fetchData(this.props.fetchFrom, this.props.embeddedArray);
  }

  public render() {
    if (this.props.hasErrored) {
      return <p>Sorry! There was an error loading the items.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    const item = this.props.itemSelected;

    return (
      <Fragment>
        <Button onClick={this.props.handleAdd}>Add new</Button>
        {
          item && item.id >= 0 &&
          <Fragment>
            <Button onClick={this.props.handleUpdate}>Update</Button>
            <Button onClick={this.props.handleDelete}>Delete</Button>
            <Button>
              <Link to={`/proposals/proposalDetails/${item.id}`}>View details</Link>
            </Button>
          </Fragment>
        }
        <SimpleList<any>
          title={this.props.title}
          list={this.props.items}
          show={this.props.show}
          select={this.props.selectItem}
        />
      </Fragment>
    );
  }
}

const mapStateToProps = (state: any) => {
    return {
        items: state.items,
        hasErrored: state.itemsHasErrored,
        isLoading: state.itemsIsLoading,
        itemSelected: state.itemSelected,
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: (url: string, embeddedArray: string) => dispatch(itemsFetchData(url, embeddedArray)),
        selectItem: (item: any) => dispatch(itemSelected(item))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ListWithControllers);
