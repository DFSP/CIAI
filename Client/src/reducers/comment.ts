import { IComment } from "./items";

interface ICommentAction { type: string, comment: IComment }

export function comment(state = [],
                          action: ICommentAction) {
    switch (action.type) {
        case 'COMMENT_FETCH_DATA_SUCCESS':
            return action.comment;
        default:
            return state;
    }
}
