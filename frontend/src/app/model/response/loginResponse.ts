import {User} from "../user/user";

export class LoginResponse{
    token : string
    user: User

    constructor(token : string, user: User)
    {

    }
}