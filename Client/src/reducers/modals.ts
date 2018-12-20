export function modalStatusChanged(state = false, action: any) {
    switch (action.type) {
        case 'MODAL_STATUS_CHANGED':
            return action.modalStatus;
        default:
            return state;
    }
}
