import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { APGroupMySuffixComponent } from './ap-group-my-suffix.component';
import { APGroupMySuffixDetailComponent } from './ap-group-my-suffix-detail.component';
import { APGroupMySuffixPopupComponent } from './ap-group-my-suffix-dialog.component';
import { APGroupMySuffixDeletePopupComponent } from './ap-group-my-suffix-delete-dialog.component';

@Injectable()
export class APGroupMySuffixResolvePagingParams implements Resolve<any> {

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

export const aPGroupRoute: Routes = [
    {
        path: 'ap-group-my-suffix',
        component: APGroupMySuffixComponent,
        resolve: {
            'pagingParams': APGroupMySuffixResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APGroups'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ap-group-my-suffix/:id',
        component: APGroupMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APGroups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const aPGroupPopupRoute: Routes = [
    {
        path: 'ap-group-my-suffix-new',
        component: APGroupMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ap-group-my-suffix/:id/edit',
        component: APGroupMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ap-group-my-suffix/:id/delete',
        component: APGroupMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'APGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
