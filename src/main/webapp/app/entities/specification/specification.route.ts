import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SpecificationComponent } from './specification.component';
import { SpecificationDetailComponent } from './specification-detail.component';
import { SpecificationPopupComponent } from './specification-dialog.component';
import { SpecificationDeletePopupComponent } from './specification-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class SpecificationResolvePagingParams implements Resolve<any> {

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

export const specificationRoute: Routes = [
    {
        path: 'specification',
        component: SpecificationComponent,
        resolve: {
            'pagingParams': SpecificationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.specification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'specification/:id',
        component: SpecificationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.specification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const specificationPopupRoute: Routes = [
    {
        path: 'specification-new',
        component: SpecificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.specification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'specification/:id/edit',
        component: SpecificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.specification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'specification/:id/delete',
        component: SpecificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.specification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
