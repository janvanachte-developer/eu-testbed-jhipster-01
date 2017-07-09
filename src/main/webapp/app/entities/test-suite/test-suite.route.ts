import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TestSuiteComponent } from './test-suite.component';
import { TestSuiteDetailComponent } from './test-suite-detail.component';
import { TestSuitePopupComponent } from './test-suite-dialog.component';
import { TestSuiteDeletePopupComponent } from './test-suite-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TestSuiteResolvePagingParams implements Resolve<any> {

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

export const testSuiteRoute: Routes = [
    {
        path: 'test-suite',
        component: TestSuiteComponent,
        resolve: {
            'pagingParams': TestSuiteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testSuite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'test-suite/:id',
        component: TestSuiteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testSuite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const testSuitePopupRoute: Routes = [
    {
        path: 'test-suite-new',
        component: TestSuitePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testSuite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-suite/:id/edit',
        component: TestSuitePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testSuite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-suite/:id/delete',
        component: TestSuiteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testSuite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
