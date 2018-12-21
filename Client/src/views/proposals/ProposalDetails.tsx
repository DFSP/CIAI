import * as React from 'react';
import { RouteComponentProps } from 'react-router-dom';

interface IRouteInfo { proposalId: string; }

const proposalDetails = ({ match } : RouteComponentProps<IRouteInfo>) => (
    <div>
        TODO proposalDetails {match.params.proposalId}
    </div>
);

export default proposalDetails
