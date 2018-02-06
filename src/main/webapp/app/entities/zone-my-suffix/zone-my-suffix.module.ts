import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    ZoneMySuffixService,
    ZoneMySuffixPopupService,
    ZoneMySuffixComponent,
    ZoneMySuffixDetailComponent,
    ZoneMySuffixDialogComponent,
    ZoneMySuffixPopupComponent,
    ZoneMySuffixDeletePopupComponent,
    ZoneMySuffixDeleteDialogComponent,
    zoneRoute,
    zonePopupRoute,
    ZoneMySuffixResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...zoneRoute,
    ...zonePopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ZoneMySuffixComponent,
        ZoneMySuffixDetailComponent,
        ZoneMySuffixDialogComponent,
        ZoneMySuffixDeleteDialogComponent,
        ZoneMySuffixPopupComponent,
        ZoneMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        ZoneMySuffixComponent,
        ZoneMySuffixDialogComponent,
        ZoneMySuffixPopupComponent,
        ZoneMySuffixDeleteDialogComponent,
        ZoneMySuffixDeletePopupComponent,
    ],
    providers: [
        ZoneMySuffixService,
        ZoneMySuffixPopupService,
        ZoneMySuffixResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerZoneMySuffixModule {}
