import * as halfred from "halfred";
import {ICompany} from "../reducers/company";




export function companiesFetchData() {
    return (dispatch: any) => {
        dispatch(companiesIsLoading(true));

        fetch('/companies.json', {
            method: 'GET',
            /*headers: new Headers({
                'Authorization': 'Basic '+btoa('admin:password'),
            }),*/
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error(response.statusText);
            }).then(json => {
            const companies = halfred.parse(json)
                .embeddedResourceArray("companies")
                .map(resource => resource.original());
            dispatch(companiesIsLoading(false));
            dispatch(companiesFetchDataSuccess(companies));
        }).catch(() => dispatch(companiesHasErrored(true)));
    }
}

export function companiesHasErrored(boolState: boolean) {
    return {
        type: 'COMPANIES_HAS_ERRORED',
        hasErrored: boolState
    };
}

export function companiesIsLoading(boolState: boolean) {
    return {
        type: 'COMPANIES_IS_LOADING',
        isLoading: boolState
    };
}

export function companiesFetchDataSuccess(companies: ICompany[]) {
    return {
        type: 'COMPANIES_FETCH_DATA_SUCCESS',
        companies
    };
}