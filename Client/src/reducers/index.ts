import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import { proposals, proposalsHasErrored, proposalsIsLoading, proposalSelected } from './proposals';
import { modalStatusChanged } from './modals';

export default combineReducers({
    proposals,
    proposalsHasErrored,
    proposalsIsLoading,
    modalStatusChanged,
    proposalSelected,
    form: formReducer
});
