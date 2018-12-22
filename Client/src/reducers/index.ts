import { combineReducers } from 'redux';
import { items, itemsHasErrored, itemsIsLoading, itemSelected } from './items';
import { proposal } from './proposal'
import { user } from './user'
import { modalStatusChanged } from './modals';

export default combineReducers({
    items,
    itemsHasErrored,
    itemsIsLoading,
    modalStatusChanged,
    itemSelected,
    proposal,
    user
});
