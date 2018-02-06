import { BaseEntity } from './../../shared';

export class ZoneMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serviceId?: string,
        public deviceId?: string,
        public serviceName?: string,
        public deviceName?: string,
        public aps?: BaseEntity[],
        public wlanGroups?: BaseEntity[],
        public wlans?: BaseEntity[],
        public domain?: BaseEntity,
    ) {
    }
}
