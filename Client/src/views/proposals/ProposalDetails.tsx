import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { proposalFetchData } from '../../actions/proposal';
import { IProposal, IComment } from '../../reducers/proposals';

interface IRouteInfo { proposalId: string; }

interface IProposalDetailsProps {
  match: IRouteInfo;
  proposal: IProposal;
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
}

class ProposalDetails extends React.Component<IProposalDetailsProps,{}> {

  public componentDidMount() {
    this.props.fetchData();
  }

  public componentWillReceiveProps(nextProps: IProposalDetailsProps) {
    console.log(nextProps);
  }

  public render() {
    if (this.props.hasErrored) {
      return <p>Sorry! There was an error loading the items.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    const { proposal } = this.props;

    return (<Fragment>
      Title: {proposal.title} <br />
      Description: {proposal.description} <br />
      Creation date: {proposal.creationDate} <br /><br />
      <b>Comments:</b><br />
      <ul>
      {
        proposal.comments && proposal.comments.map((c: IComment) => (
          <div>
          <li key={c.id}>
            <p><b>Title:</b> {c.title}</p>
            <p><b>Text:</b> {c.text}</p>
          </li>
          <br />
          </div>
        ))
      }
      </ul>
    </Fragment>);
  }
}

const mapStateToProps = (state: any) => {
    return {
        proposal: state.proposal,
        hasErrored: state.proposalsHasErrored,
        isLoading: state.proposalsIsLoading
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(proposalFetchData())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ProposalDetails);
