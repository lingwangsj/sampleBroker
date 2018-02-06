import { BaseEntity } from './../../shared';

export class APMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serviceId?: string,
        public serviceName?: string,
        public apgroup?: BaseEntity,
    ) {
    }
}
