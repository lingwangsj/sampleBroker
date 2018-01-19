import { BaseEntity } from './../../shared';

export class WlanMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public networkId?: string,
        public wlanName?: string,
        public zone?: BaseEntity,
        public groups?: BaseEntity[],
    ) {
    }
}
