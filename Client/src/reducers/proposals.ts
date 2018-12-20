export function proposalsHasErrored(state = false, action: any) {
    switch (action.type) {
        case 'PROPOSALS_HAS_ERRORED':
            return action.hasErrored;
        default:
            return state;
    }
}
export function proposalsIsLoading(state = false, action: any) {
    switch (action.type) {
        case 'PROPOSALS_IS_LOADING':
            return action.isLoading;
        default:
            return state;
    }
}
export function proposals(state = [], action: any) {
    switch (action.type) {
        case 'PROPOSALS_FETCH_DATA_SUCCESS':
            return action.proposals;
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
