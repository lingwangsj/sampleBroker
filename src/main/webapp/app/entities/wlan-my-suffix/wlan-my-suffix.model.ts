import { BaseEntity } from './../../shared';

export class WlanMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serviceId?: string,
        public deviceId?: string,
        public serviceName?: string,
        public deviceName?: string,
        public zone?: BaseEntity,
        public groups?: BaseEntity[],
    ) {
    }
}
