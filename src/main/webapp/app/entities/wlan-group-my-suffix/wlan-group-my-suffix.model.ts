import { BaseEntity } from './../../shared';

export class WlanGroupMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public deviceId?: string,
        public deviceName?: string,
        public members?: BaseEntity[],
        public zone?: BaseEntity,
    ) {
    }
}
