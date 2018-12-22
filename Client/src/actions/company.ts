import * as halfred from 'halfred';
import { itemsHasErrored, itemsIsLoading } from './items'

export function companyFetchData() {
  let company = {};
  return (dispatch: any) => {
    dispatch(itemsIsLoading(true));

    fetch('/company.json', {
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
      company = halfred.parse(json).original();
      fetch('/company-employees.json', {
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
        const employees = halfred.parse(json2)
            .embeddedResourceArray("employees")
            .map(resource => resource.original());
        const finalCompany= Object.assign({ employees }, company)
        dispatch(itemsIsLoading(false));
        dispatch(proposalFetchDataSuccess(finalCompany));
      }).catch(() => dispatch(itemsHasErrored(true)))
    }).catch(() => dispatch(itemsHasErrored(true)));
  }
}

export function proposalFetchDataSuccess(company: any) {
  return {
    type: 'COMPANY_FETCH_DATA_SUCCESS',
    company
  };
}
