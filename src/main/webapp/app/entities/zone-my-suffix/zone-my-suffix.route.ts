import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ZoneMySuffixComponent } from './zone-my-suffix.component';
import { ZoneMySuffixDetailComponent } from './zone-my-suffix-detail.component';
import { ZoneMySuffixPopupComponent } from './zone-my-suffix-dialog.component';
import { ZoneMySuffixDeletePopupComponent } from './zone-my-suffix-delete-dialog.component';

@Injectable()
export class ZoneMySuffixResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const zoneRoute: Routes = [
    {
        path: 'zone-my-suffix',
        component: ZoneMySuffixComponent,
        resolve: {
            'pagingParams': ZoneMySuffixResolvePagingParams
        },
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
