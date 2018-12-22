import { IReview } from "./items";

interface IUserAction { type: string, review: IReview }

export function review(state = [],
                          action: IUserAction) {
    switch (action.type) {
        case 'REVIEW_FETCH_DATA_SUCCESS':
            return action.review;
        default:
            return state;
    }
}
