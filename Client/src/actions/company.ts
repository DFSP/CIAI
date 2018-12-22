import * as halfred from 'halfred';
import { itemsHasErrored, itemsIsLoading } from './items'

export function companyFetchData(id: string) {
  let company = {};
  let status = false;
  return (dispatch: any) => {
    dispatch(itemsIsLoading(true));

    fetch(`http://localhost:8080/companies/${id}`, {
      method: 'GET',
      headers: new Headers({
         'Authorization': 'Basic '+btoa('admin:password'),
       }),
    })
    .then(response => {
      alert("1" + response.ok);
      if (response.ok) {
        status = true;
        return response.json();
      }
      throw new Error(response.statusText);
    }).then(json => {
      company = halfred.parse(json).original();
      fetch(`http://localhost:8080/companies/${id}/employees`, {
        method: 'GET',
        headers: new Headers({
           'Authorization': 'Basic '+btoa('admin:password'),
         }),
      }).then(response => {
        alert(response.ok);
        if (response.ok) {
          return response.json();
        }
        throw new Error(response.statusText);
      }).then(json2 => {
        const employees = halfred.parse(json2).original();
        alert(employees);
        const companyWithEmployees = Object.assign({ company }, employees);
        dispatch(itemsIsLoading(false));
        dispatch(companyFetchDataSuccess(companyWithEmployees));
      }).catch(() => {
        if (status) {
          dispatch(itemsIsLoading(false));
          dispatch(companyFetchDataSuccess(company));
        } else {
          dispatch(itemsHasErrored(true));
        }
      });
    }).catch(() => dispatch(itemsHasErrored(true)));
  }
}

export function companyFetchDataSuccess(company: any) {
  return {
    type: 'COMPANY_FETCH_DATA_SUCCESS',
    company
  };
}
