import { SimPair } from "./sim-pair";

export interface SimResult {
    error: string;
    hash: string;
    name: string;
    others: SimPair[];
    type: string;
}
