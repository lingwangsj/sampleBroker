import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { DomainMySuffixComponent } from './domain-my-suffix.component';
import { DomainMySuffixDetailComponent } from './domain-my-suffix-detail.component';
import { DomainMySuffixPopupComponent } from './domain-my-suffix-dialog.component';
import { DomainMySuffixDeletePopupComponent } from './domain-my-suffix-delete-dialog.component';

@Injectable()
export class DomainMySuffixResolvePagingParams implements Resolve<any> {

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

export const domainRoute: Routes = [
    {
        path: 'domain-my-suffix',
        component: DomainMySuffixComponent,
        resolve: {
            'pagingParams': DomainMySuffixResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Domains'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'domain-my-suffix/:id',
        component: DomainMySuffixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Domains'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const domainPopupRoute: Routes = [
    {
        path: 'domain-my-suffix-new',
        component: DomainMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Domains'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'domain-my-suffix/:id/edit',
        component: DomainMySuffixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Domains'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'domain-my-suffix/:id/delete',
        component: DomainMySuffixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Domains'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
