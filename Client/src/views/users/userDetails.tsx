import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Panel, Label } from 'react-bootstrap';
import { userFetchData } from '../../actions/user';
import { IUser } from '../../reducers/proposals';

interface IRouteInfo { proposalId: string; }

interface IUserDetailsProps {
  match: IRouteInfo;
  user: IUser;
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
}

class ProposalDetails extends React.Component<IUserDetailsProps,any> {
  constructor(props: IUserDetailsProps) {
      super(props);
  }

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

    const { user } = this.props;

    return (<Fragment>
      <Panel>
      {
        user &&
        <Fragment>
          <Panel.Body>
            <Label>Personal details</Label>
            <Label>First name:</Label> {user.firstName} <br />
            <Label>Last name:</Label> {user.lastName} <br />
            <Label>Username:</Label> {user.userName} <br/>
            <Label>Email:</Label> {user.email} <br/>
            <Label>Role:</Label> {user.role} <br/>
          </Panel.Body>
          {
            user.job &&
            <Panel.Body>
              <Fragment>
                <Label>Employee details</Label>
                <Label>Job:</Label> {user.job} <br />
                other fields...
              </Fragment>
            </Panel.Body>
          }
          </Fragment>
      }
      </Panel>
    </Fragment>);
  }
}

const mapStateToProps = (state: any) => {
    return {
        user: state.user,
        hasErrored: state.itemsHasErrored,
        isLoading: state.itemsIsLoading
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(userFetchData())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ProposalDetails);
