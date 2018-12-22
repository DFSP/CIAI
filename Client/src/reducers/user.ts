import { IUser } from "./proposals";

interface IUserAction { type: string, user: IUser }

export function user(state = [],
                          action: IUserAction) {
    switch (action.type) {
        case 'USER_FETCH_DATA_SUCCESS':
            return action.user;
        default:
            return state;
    }
}
