import { BaseEntity } from './../../shared';

export class ZoneMySuffix implements BaseEntity {
    constructor(
        public id?: number,
        public venueId?: string,
        public zoneName?: string,
        public aps?: BaseEntity[],
        public wlanGroups?: BaseEntity[],
        public wlans?: BaseEntity[],
        public domain?: BaseEntity,
    ) {
    }
}
