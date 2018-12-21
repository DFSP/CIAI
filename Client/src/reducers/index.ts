import { combineReducers } from 'redux';
import { proposals, proposalsHasErrored, proposalsIsLoading, proposalSelected } from './proposals';
import { proposal } from './proposal'
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
    companiesIsLoading,
    proposal,
});
