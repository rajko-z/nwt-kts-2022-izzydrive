import { SafeResourceUrl } from "@angular/platform-browser";
import { ca } from "date-fns/locale";
import { Car } from "../car/car";
import { User } from "./user";

export class ProfilePageData{
    title: string;
    subtitle: string;
    image: SafeResourceUrl;
    otherAttributes: Record<string, any>;
}


