import { combineReducers } from 'redux';
import { proposals, proposalsHasErrored, proposalsIsLoading, proposalSelected } from './proposals';
import { modalStatusChanged } from './modals';
import {companies, companiesHasErrored, companiesIsLoading} from "./company";



export default combineReducers({
    proposals,
    proposalsHasErrored,
    proposalsIsLoading,
    modalStatusChanged,
    proposalSelected,
    companies,
    companiesHasErrored,
    companiesIsLoading
});
