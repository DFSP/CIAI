import { IProposal } from './items'

interface IProposalAction { type: string, proposal: IProposal }

export function proposal(state = {},
  action: IProposalAction) {
    switch (action.type) {
        case 'PROPOSAL_FETCH_DATA_SUCCESS':
            return action.proposal;
        default:
            return state;
    }
}
