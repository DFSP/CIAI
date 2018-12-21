import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { proposalsFetchData, proposalSelected } from '../../actions/proposals';
import { IProposal } from '../../reducers/proposals';
import SimpleList from '../common/SimpleList';

import { Button } from 'react-bootstrap';

export interface IProposalProps {
  proposals: IProposal[];
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
  selectProposal: (proposal: IProposal) => void;
  proposalSelected: IProposal;
  handleAdd: () => void;
  handleUpdate: () => void;
  handleDelete: () => void;
  show: (s: IProposal) => string;
  title: string;
}

class ListWithControllers extends React.Component<IProposalProps,any> {

  public componentDidMount() {
    this.props.fetchData();
  }

  public render() {
    if (this.props.hasErrored) {
      return <p>Sorry! There was an error loading the items.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    const proposal = this.props.proposalSelected;

    return (
      <Fragment>
        <Button onClick={this.props.handleAdd}>Add new</Button>
        {
          proposal && proposal.id >= 0 &&
          <Fragment>
            <Button onClick={this.props.handleUpdate}>Update</Button>
            <Button onClick={this.props.handleDelete}>Delete</Button>
            <Button>
              <Link to={`/proposals/proposalDetails/${proposal.id}`}>View details</Link>
            </Button>
          </Fragment>
        }
        <SimpleList<IProposal>
          title={this.props.title}
          list={this.props.proposals}
          show={this.props.show}
          select={this.props.selectProposal}
        />
      </Fragment>
    );
  }
}

const mapStateToProps = (state: any) => {
    return {
        proposals: state.proposals,
        hasErrored: state.proposalsHasErrored,
        isLoading: state.proposalsIsLoading,
        proposalSelected: state.proposalSelected,
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(proposalsFetchData()),
        selectProposal: (proposal: IProposal) => dispatch(proposalSelected(proposal))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ListWithControllers);
