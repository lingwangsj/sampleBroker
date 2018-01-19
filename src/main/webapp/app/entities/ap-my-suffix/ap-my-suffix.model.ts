import { BaseEntity } from './../../shared';

export class APMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public serialNumber?: string,
        public apName?: string,
        public wg24?: BaseEntity,
        public wg50?: BaseEntity,
        public zone?: BaseEntity,
    ) {
    }
}
