import { BaseEntity } from './../../shared';

export class DomainMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: string,
        public domainName?: string,
        public zones?: BaseEntity[],
    ) {
    }
}
