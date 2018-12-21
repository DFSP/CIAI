import { combineReducers } from 'redux';
import { items, itemsHasErrored, itemsIsLoading, itemSelected } from './proposals';
import { proposal } from './proposal'
import { modalStatusChanged } from './modals';

export default combineReducers({
    items,
    itemsHasErrored,
    itemsIsLoading,
    modalStatusChanged,
    itemSelected,
    proposal
});
