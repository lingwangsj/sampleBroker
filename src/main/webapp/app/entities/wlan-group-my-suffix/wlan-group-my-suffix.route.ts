import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { WlanGroupMySuffixComponent } from './wlan-group-my-suffix.component';
import { WlanGroupMySuffixDetailComponent } from './wlan-group-my-suffix-detail.component';
import { WlanGroupMySuffixPopupComponent } from './wlan-group-my-suffix-dialog.component';
import { WlanGroupMySuffixDeletePopupComponent } from './wlan-group-my-suffix-delete-dialog.component';

@Injectable()
export class WlanGroupMySuffixResolvePagingParams implements Resolve<any> {

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

export const wlanGroupRoute: Routes = [
    {
        path: 'wlan-group-my-suffix',
        component: WlanGroupMySuffixComponent,
        resolve: {
            'pagingParams': WlanGroupMySuffixResolvePagingParams
        },
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
