import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { APMySuffixComponent } from './ap-my-suffix.component';
import { APMySuffixDetailComponent } from './ap-my-suffix-detail.component';
import { APMySuffixPopupComponent } from './ap-my-suffix-dialog.component';
import { APMySuffixDeletePopupComponent } from './ap-my-suffix-delete-dialog.component';

@Injectable()
export class APMySuffixResolvePagingParams implements Resolve<any> {

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

export const aPRoute: Routes = [
    {
        path: 'ap-my-suffix',
        component: APMySuffixComponent,
        resolve: {
            'pagingParams': APMySuffixResolvePagingParams
        },
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
