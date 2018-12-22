import * as halfred from 'halfred';
import { itemsHasErrored, itemsIsLoading } from './items'

export function userFetchData() {
  let user = {};
  return (dispatch: any) => {
    dispatch(itemsIsLoading(true));

    fetch('/user.json', {
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
      user = halfred.parse(json).original();
      dispatch(itemsIsLoading(false));
      dispatch(proposalFetchDataSuccess(user));
    }).catch(() => dispatch(itemsHasErrored(true)));
  }
}

export function proposalFetchDataSuccess(user: any) {
  return {
    type: 'USER_FETCH_DATA_SUCCESS',
    user
  };
}
