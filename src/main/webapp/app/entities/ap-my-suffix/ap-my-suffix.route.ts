import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { APMySuffixComponent } from './ap-my-suffix.component';
import { APMySuffixDetailComponent } from './ap-my-suffix-detail.component';
import { APMySuffixPopupComponent } from './ap-my-suffix-dialog.component';
import { APMySuffixDeletePopupComponent } from './ap-my-suffix-delete-dialog.component';

export const aPRoute: Routes = [
    {
        path: 'ap-my-suffix',
        component: APMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APS'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ap-my-suffix/:id',
        component: APMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APS'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const aPPopupRoute: Routes = [
    {
        path: 'ap-my-suffix-new',
        component: APMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ap-my-suffix/:id/edit',
        component: APMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ap-my-suffix/:id/delete',
        component: APMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
