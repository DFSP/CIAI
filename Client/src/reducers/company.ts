import {ICompany} from "./company";

interface ICompanyHasErroredAction { type: string, hasErrored: boolean }
interface ICompanyIsLoadingAction { type: string, isLoading: boolean }
interface ICompanyAction { type: string, companies: ICompany[] }
 // interface ICompanySelectedAction { type: string, proposalSelected: IProposal }

export interface ICompany {
    id: number;
    name: string;
    city: string;
    zipCode: string;
    address: string;
    phone: number;
    email: string;
    fax: string;
    _links: any;
}

export function companiesHasErrored(state = false,
                                    action: ICompanyHasErroredAction) {
    switch (action.type) {
        case 'COMPANIES_HAS_ERRORED':
            return action.hasErrored;
        default:
            return state;
    }
}
export function companiesIsLoading(state = false,
                                   action: ICompanyIsLoadingAction) {
    switch (action.type) {
        case 'COMPANIES_IS_LOADING':
            return action.isLoading;
        default:
            return state;
    }
}
export function companies(state = [],
                          action: ICompanyAction) {
    switch (action.type) {
        case 'COMPANIES_FETCH_DATA_SUCCESS':
            return action.companies;
        default:
            return state;
    }
}