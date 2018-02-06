import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    APGroupMySuffixService,
    APGroupMySuffixPopupService,
    APGroupMySuffixComponent,
    APGroupMySuffixDetailComponent,
    APGroupMySuffixDialogComponent,
    APGroupMySuffixPopupComponent,
    APGroupMySuffixDeletePopupComponent,
    APGroupMySuffixDeleteDialogComponent,
    aPGroupRoute,
    aPGroupPopupRoute,
    APGroupMySuffixResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...aPGroupRoute,
    ...aPGroupPopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        APGroupMySuffixComponent,
        APGroupMySuffixDetailComponent,
        APGroupMySuffixDialogComponent,
        APGroupMySuffixDeleteDialogComponent,
        APGroupMySuffixPopupComponent,
        APGroupMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        APGroupMySuffixComponent,
        APGroupMySuffixDialogComponent,
        APGroupMySuffixPopupComponent,
        APGroupMySuffixDeleteDialogComponent,
        APGroupMySuffixDeletePopupComponent,
    ],
    providers: [
        APGroupMySuffixService,
        APGroupMySuffixPopupService,
        APGroupMySuffixResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerAPGroupMySuffixModule {}
