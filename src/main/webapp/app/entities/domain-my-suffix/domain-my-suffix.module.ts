import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    DomainMySuffixService,
    DomainMySuffixPopupService,
    DomainMySuffixComponent,
    DomainMySuffixDetailComponent,
    DomainMySuffixDialogComponent,
    DomainMySuffixPopupComponent,
    DomainMySuffixDeletePopupComponent,
    DomainMySuffixDeleteDialogComponent,
    domainRoute,
    domainPopupRoute,
    DomainMySuffixResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...domainRoute,
    ...domainPopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DomainMySuffixComponent,
        DomainMySuffixDetailComponent,
        DomainMySuffixDialogComponent,
        DomainMySuffixDeleteDialogComponent,
        DomainMySuffixPopupComponent,
        DomainMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        DomainMySuffixComponent,
        DomainMySuffixDialogComponent,
        DomainMySuffixPopupComponent,
        DomainMySuffixDeleteDialogComponent,
        DomainMySuffixDeletePopupComponent,
    ],
    providers: [
        DomainMySuffixService,
        DomainMySuffixPopupService,
        DomainMySuffixResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerDomainMySuffixModule {}
