interface IProposalsHasErroredAction { type: string, hasErrored: boolean }
interface IProposalsIsLoadingAction { type: string, isLoading: boolean }
interface IProposalsAction { type: string, proposals: IProposal[] }
interface IProposalSelectedAction { type: string, proposalSelected: IProposal }

export interface IComment {
  id: number;
  title: string;
  text: string;
}

export interface IProposal {
  id: number;
  title: string;
  description: string;
  state: string;
  creationDate: string;
  _links: any;
  comments?: IComment[];
}

export function proposalsHasErrored(state = false,
  action: IProposalsHasErroredAction) {
    switch (action.type) {
        case 'PROPOSALS_HAS_ERRORED':
            return action.hasErrored;
        default:
            return state;
    }
}

export function proposalsIsLoading(state = false,
  action: IProposalsIsLoadingAction) {
    switch (action.type) {
        case 'PROPOSALS_IS_LOADING':
            return action.isLoading;
        default:
            return state;
    }
}

export function proposals(state = [],
  action: IProposalsAction) {
    switch (action.type) {
        case 'PROPOSALS_FETCH_DATA_SUCCESS':
            return action.proposals;
        default:
            return state;
    }
}

export function proposalSelected(state = {},
  action: IProposalSelectedAction) {
    switch (action.type) {
        case 'PROPOSAL_SELECTED':
            return action.proposalSelected;
        default:
            return state;
    }
}
