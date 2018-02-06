import { BaseEntity } from './../../shared';

export class DomainMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serviceId?: string,
        public deviceId?: string,
        public serviceName?: string,
        public deviceName?: string,
        public zones?: BaseEntity[],
    ) {
    }
}
