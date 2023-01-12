import { Role, getRole } from "./role";

export class User{
    id?: number;
    email: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    activated?: boolean;
    imageName?: string;
    blocked?: boolean;
    role?: Role;

    constructor(
        id: number,
        email: string,
        firstName: string,
        lastName: string,
        phoneNumber: string,
        activated: boolean,
        imageName: string,
        blocked: boolean,
        role: string,
        profilePhoto: string)
    {
        this.role = getRole[role];
    }
}