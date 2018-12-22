import * as React from 'react';
import { Fragment } from 'react';
import { connect } from 'react-redux';
import { Panel, Label } from 'react-bootstrap';
import { companyFetchData } from '../../actions/company';
import { ICompany } from '../../reducers/proposals';
import Users from '../users/Users';

interface IRouteInfo { proposalId: string; }

interface ICompanyDetailsProps {
  match: IRouteInfo;
  company: ICompany;
  isLoading: boolean;
  hasErrored: boolean;
  fetchData: () => void;
}

class ProposalDetails extends React.Component<ICompanyDetailsProps,any> {
  constructor(props: ICompanyDetailsProps) {
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

    const { company } = this.props;

    return (<Fragment>
      <Panel>
      {
        company &&
        <Fragment>
          <Panel.Body>
            <Label>Personal details</Label>
            <Label>Name:</Label> {company.name} <br />
            <Label>City:</Label> {company.city} <br />
            <Label>ZipCode:</Label> {company.zipCode} <br />
            <Label>Address:</Label> {company.address} <br />
            <Label>Phone:</Label> {company.phone} <br />
            <Label>Email:</Label> {company.email} <br />
            <Label>Fax:</Label> {company.fax} <br />
          </Panel.Body>
          {
            company.employees &&
            <Users />
          }
          </Fragment>
      }
      </Panel>
    </Fragment>);
  }
}

const mapStateToProps = (state: any) => {
    return {
        company: state.company,
        hasErrored: state.itemsHasErrored,
        isLoading: state.itemsIsLoading
    };
};

const mapDispatchToProps = (dispatch: any) => {
    return {
        fetchData: () => dispatch(companyFetchData())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ProposalDetails);
