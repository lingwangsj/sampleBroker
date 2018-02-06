import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { WlanMySuffixComponent } from './wlan-my-suffix.component';
import { WlanMySuffixDetailComponent } from './wlan-my-suffix-detail.component';
import { WlanMySuffixPopupComponent } from './wlan-my-suffix-dialog.component';
import { WlanMySuffixDeletePopupComponent } from './wlan-my-suffix-delete-dialog.component';

@Injectable()
export class WlanMySuffixResolvePagingParams implements Resolve<any> {

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

export const wlanRoute: Routes = [
    {
        path: 'wlan-my-suffix',
        component: WlanMySuffixComponent,
        resolve: {
            'pagingParams': WlanMySuffixResolvePagingParams
        },
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
