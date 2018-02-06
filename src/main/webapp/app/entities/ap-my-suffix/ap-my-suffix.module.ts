import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    APMySuffixService,
    APMySuffixPopupService,
    APMySuffixComponent,
    APMySuffixDetailComponent,
    APMySuffixDialogComponent,
    APMySuffixPopupComponent,
    APMySuffixDeletePopupComponent,
    APMySuffixDeleteDialogComponent,
    aPRoute,
    aPPopupRoute,
    APMySuffixResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...aPRoute,
    ...aPPopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        APMySuffixComponent,
        APMySuffixDetailComponent,
        APMySuffixDialogComponent,
        APMySuffixDeleteDialogComponent,
        APMySuffixPopupComponent,
        APMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        APMySuffixComponent,
        APMySuffixDialogComponent,
        APMySuffixPopupComponent,
        APMySuffixDeleteDialogComponent,
        APMySuffixDeletePopupComponent,
    ],
    providers: [
        APMySuffixService,
        APMySuffixPopupService,
        APMySuffixResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerAPMySuffixModule {}
