import { combineReducers } from 'redux';
import { proposals, proposalsHasErrored, proposalsIsLoading, proposalSelected } from './proposals';
import { proposal } from './proposal'
import { modalStatusChanged } from './modals';

export default combineReducers({
    proposals,
    proposalsHasErrored,
    proposalsIsLoading,
    modalStatusChanged,
    proposalSelected,
    proposal
});
