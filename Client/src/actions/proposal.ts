import * as halfred from 'halfred';
import { itemsHasErrored, itemsIsLoading } from './items'

export function proposalFetchData() {
  let proposal = {};
  return (dispatch: any) => {
    dispatch(itemsIsLoading(true));

    fetch('/proposal.json', {
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
      proposal = halfred.parse(json).original();
      fetch('/comments.json', {
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
      }).then(json2 => {
        const comments = halfred.parse(json2)
            .embeddedResourceArray("comments")
            .map(resource => resource.original());
        const finalProposal = Object.assign({ comments }, proposal)
        dispatch(itemsIsLoading(false));
        dispatch(proposalFetchDataSuccess(finalProposal));
      }).catch(() => dispatch(itemsHasErrored(true)))
    }).catch(() => dispatch(itemsHasErrored(true)));
  }
}

export function proposalFetchDataSuccess(proposal: any) {
  return {
    type: 'PROPOSAL_FETCH_DATA_SUCCESS',
    proposal
  };
}
