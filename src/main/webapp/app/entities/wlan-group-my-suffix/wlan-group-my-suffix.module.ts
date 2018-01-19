import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    WlanGroupMySuffixService,
    WlanGroupMySuffixPopupService,
    WlanGroupMySuffixComponent,
    WlanGroupMySuffixDetailComponent,
    WlanGroupMySuffixDialogComponent,
    WlanGroupMySuffixPopupComponent,
    WlanGroupMySuffixDeletePopupComponent,
    WlanGroupMySuffixDeleteDialogComponent,
    wlanGroupRoute,
    wlanGroupPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wlanGroupRoute,
    ...wlanGroupPopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WlanGroupMySuffixComponent,
        WlanGroupMySuffixDetailComponent,
        WlanGroupMySuffixDialogComponent,
        WlanGroupMySuffixDeleteDialogComponent,
        WlanGroupMySuffixPopupComponent,
        WlanGroupMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        WlanGroupMySuffixComponent,
        WlanGroupMySuffixDialogComponent,
        WlanGroupMySuffixPopupComponent,
        WlanGroupMySuffixDeleteDialogComponent,
        WlanGroupMySuffixDeletePopupComponent,
    ],
    providers: [
        WlanGroupMySuffixService,
        WlanGroupMySuffixPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerWlanGroupMySuffixModule {}
