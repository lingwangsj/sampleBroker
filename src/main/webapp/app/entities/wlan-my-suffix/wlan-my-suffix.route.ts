import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { WlanMySuffixComponent } from './wlan-my-suffix.component';
import { WlanMySuffixDetailComponent } from './wlan-my-suffix-detail.component';
import { WlanMySuffixPopupComponent } from './wlan-my-suffix-dialog.component';
import { WlanMySuffixDeletePopupComponent } from './wlan-my-suffix-delete-dialog.component';

export const wlanRoute: Routes = [
    {
        path: 'wlan-my-suffix',
        component: WlanMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Wlans'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wlan-my-suffix/:id',
        component: WlanMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Wlans'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wlanPopupRoute: Routes = [
    {
        path: 'wlan-my-suffix-new',
        component: WlanMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Wlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wlan-my-suffix/:id/edit',
        component: WlanMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Wlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wlan-my-suffix/:id/delete',
        component: WlanMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Wlans'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
