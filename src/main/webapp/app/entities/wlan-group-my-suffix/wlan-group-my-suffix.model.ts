import { BaseEntity } from './../../shared';

export class WlanGroupMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public groupName?: string,
        public members?: BaseEntity[],
        public zone?: BaseEntity,
    ) {
    }
}
