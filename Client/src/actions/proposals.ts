import * as halfred from 'halfred';
import  { IProposal } from '../reducers/proposals'

export function proposalsHasErrored(boolState: boolean) {
  return {
    type: 'PROPOSALS_HAS_ERRORED',
    hasErrored: boolState
  };
}

export function proposalsIsLoading(boolState: boolean) {
  return {
    type: 'PROPOSALS_IS_LOADING',
    isLoading: boolState
  };
}

export function proposalsFetchDataSuccess(proposals: IProposal[]) {
  return {
    type: 'PROPOSALS_FETCH_DATA_SUCCESS',
    proposals
  };
}

export function proposalsFetchData() {
  return (dispatch: any) => {
    dispatch(proposalsIsLoading(true));

    fetch('/proposals.json', {
      method: 'GET',
      headers: new Headers({
         'Authorization': 'Basic '+btoa('admin:password'),
       }),
    })
    .then(response => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(response.statusText);
    }).then(json => {
      const proposals = halfred.parse(json)
        .embeddedResourceArray("proposals")
        .map(resource => resource.original());
      dispatch(proposalsIsLoading(false));
      dispatch(proposalsFetchDataSuccess(proposals));
    }).catch(() => dispatch(proposalsHasErrored(true)));
  }
}

export function proposalSelected(proposal: IProposal) {
  return {
    type: 'PROPOSAL_SELECTED',
    proposalSelected: proposal
  };
}
