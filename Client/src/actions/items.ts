import * as halfred from 'halfred';
import  { IProposal } from '../components/proposals/Proposals'

export function itemsHasErrored(boolState: boolean) {
  return {
    type: 'ITEM_HAS_ERRORED',
    hasErrored: boolState
  };
}

export function itemsIsLoading(boolState: boolean) {
  return {
    type: 'ITEM_IS_LOADING',
    isLoading: boolState
  };
}

export function itemsFetchDataSuccess(items: any) {
  return {
    type: 'ITEMS_FETCH_DATA_SUCCESS',
    proposals: items
  };
}

export function itemsFetchData() {
  return (dispatch: any) => {
    dispatch(itemsIsLoading(true));

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
      dispatch(itemsIsLoading(false));
      dispatch(itemsFetchDataSuccess(proposals));
    }).catch(() => dispatch(itemsHasErrored(true)));
  }
}

export function modalStatusChanged(boolState: boolean) {
  return {
    type: 'MODAL_STATUS_CHANGED',
    modalStatus: boolState
  };
}

export function proposalSelected(proposal: IProposal) {
  return {
    type: 'PROPOSAL_SELECTED',
    proposalSelected: proposal
  };
}
