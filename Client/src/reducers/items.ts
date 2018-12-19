export function itemsHasErrored(state = false, action: any) {
    switch (action.type) {
        case 'ITEMS_HAS_ERRORED':
            return action.hasErrored;
        default:
            return state;
    }
}
export function itemsIsLoading(state = false, action: any) {
    switch (action.type) {
        case 'ITEMS_IS_LOADING':
            return action.isLoading;
        default:
            return state;
    }
}
export function items(state = [], action: any) {
    switch (action.type) {
        case 'ITEMS_FETCH_DATA_SUCCESS':
            return action.proposals;
        default:
            return state;
    }
}
export function modalStatusChanged(state = false, action: any) {
    switch (action.type) {
        case 'MODAL_STATUS_CHANGED':
            return action.modalStatus;
        default:
            return state;
    }
}
export function proposalSelected(state = {}, action: any) {
    switch (action.type) {
        case 'PROPOSAL_SELECTED':
            return action.proposalSelected;
        default:
            return state;
    }
}
