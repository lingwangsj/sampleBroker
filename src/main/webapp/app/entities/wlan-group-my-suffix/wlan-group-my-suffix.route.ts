import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { WlanGroupMySuffixComponent } from './wlan-group-my-suffix.component';
import { WlanGroupMySuffixDetailComponent } from './wlan-group-my-suffix-detail.component';
import { WlanGroupMySuffixPopupComponent } from './wlan-group-my-suffix-dialog.component';
import { WlanGroupMySuffixDeletePopupComponent } from './wlan-group-my-suffix-delete-dialog.component';

export const wlanGroupRoute: Routes = [
    {
        path: 'wlan-group-my-suffix',
        component: WlanGroupMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WlanGroups'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wlan-group-my-suffix/:id',
        component: WlanGroupMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WlanGroups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wlanGroupPopupRoute: Routes = [
    {
        path: 'wlan-group-my-suffix-new',
        component: WlanGroupMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WlanGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wlan-group-my-suffix/:id/edit',
        component: WlanGroupMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WlanGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wlan-group-my-suffix/:id/delete',
        component: WlanGroupMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WlanGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
