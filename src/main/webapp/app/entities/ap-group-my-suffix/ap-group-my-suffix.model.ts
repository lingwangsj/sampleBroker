import { BaseEntity } from './../../shared';

export class APGroupMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serviceId?: string,
        public deviceId?: string,
        public serviceName?: string,
        public deviceName?: string,
        public wg24?: BaseEntity,
        public wg50?: BaseEntity,
        public aps?: BaseEntity[],
        public zone?: BaseEntity,
    ) {
    }
}
