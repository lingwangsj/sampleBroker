import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ZoneMySuffixComponent } from './zone-my-suffix.component';
import { ZoneMySuffixDetailComponent } from './zone-my-suffix-detail.component';
import { ZoneMySuffixPopupComponent } from './zone-my-suffix-dialog.component';
import { ZoneMySuffixDeletePopupComponent } from './zone-my-suffix-delete-dialog.component';

export const zoneRoute: Routes = [
    {
        path: 'zone-my-suffix',
        component: ZoneMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zones'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'zone-my-suffix/:id',
        component: ZoneMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zones'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const zonePopupRoute: Routes = [
    {
        path: 'zone-my-suffix-new',
        component: ZoneMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zones'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'zone-my-suffix/:id/edit',
        component: ZoneMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zones'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'zone-my-suffix/:id/delete',
        component: ZoneMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zones'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
