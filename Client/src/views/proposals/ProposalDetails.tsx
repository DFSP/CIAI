import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Panel, Tabs, Tab, Label } from 'react-bootstrap';
import { proposalFetchData } from '../../actions/proposal';
import { IProposal } from '../../reducers/items';
import Reviews from '../reviews/Reviews';
import Users from '../users/Users';
import Comments from './comments/Comments';

interface IRouteInfo { proposalId: string; }

interface IProposalDetailsProps {
  match: IRouteInfo;
  proposal: IProposal;
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
}

class ProposalDetails extends React.Component<IProposalDetailsProps,any> {
  constructor(props: IProposalDetailsProps) {
      super(props);
  }

  public componentDidMount() {
    this.props.fetchData();
  }

  public render() {
    if (this.props.hasErrored) {
      return <p>Oops! Houve um erro ao carregar os dados.</p>;
    }
    if (this.props.isLoading) {
      return <p>Loading...</p>;
    }

    const { proposal } = this.props;

    return (<Fragment>
      <Panel>
        <Panel.Body>
          <Label>Title:</Label> {proposal.title} <br />
          <Label>Description:</Label> {proposal.description} <br />
          <Label>Creation date:</Label> {proposal.creationDate} <br/>
          <Label>Proposer:</Label> {proposal.proposer} <br/>
        </Panel.Body>
      </Panel>
      <Tabs defaultActiveKey="2" name="tabController">
        <Tab eventKey="1" title="Comments">
          <Comments />
        </Tab>
        <Tab eventKey="2" title="Reviews">
          <Reviews />
        </Tab>
        <Tab eventKey="3" title="Members">
          <Users />
        </Tab>
      </Tabs>
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
