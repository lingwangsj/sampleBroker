import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SampleBrokerSharedModule } from '../../shared';
import {
    WlanMySuffixService,
    WlanMySuffixPopupService,
    WlanMySuffixComponent,
    WlanMySuffixDetailComponent,
    WlanMySuffixDialogComponent,
    WlanMySuffixPopupComponent,
    WlanMySuffixDeletePopupComponent,
    WlanMySuffixDeleteDialogComponent,
    wlanRoute,
    wlanPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wlanRoute,
    ...wlanPopupRoute,
];

@NgModule({
    imports: [
        SampleBrokerSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WlanMySuffixComponent,
        WlanMySuffixDetailComponent,
        WlanMySuffixDialogComponent,
        WlanMySuffixDeleteDialogComponent,
        WlanMySuffixPopupComponent,
        WlanMySuffixDeletePopupComponent,
    ],
    entryComponents: [
        WlanMySuffixComponent,
        WlanMySuffixDialogComponent,
        WlanMySuffixPopupComponent,
        WlanMySuffixDeleteDialogComponent,
        WlanMySuffixDeletePopupComponent,
    ],
    providers: [
        WlanMySuffixService,
        WlanMySuffixPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SampleBrokerWlanMySuffixModule {}
