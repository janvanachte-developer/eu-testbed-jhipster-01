import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TestCaseComponent } from './test-case.component';
import { TestCaseDetailComponent } from './test-case-detail.component';
import { TestCasePopupComponent } from './test-case-dialog.component';
import { TestCaseDeletePopupComponent } from './test-case-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TestCaseResolvePagingParams implements Resolve<any> {

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

export const testCaseRoute: Routes = [
    {
        path: 'test-case',
        component: TestCaseComponent,
        resolve: {
            'pagingParams': TestCaseResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testCase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'test-case/:id',
        component: TestCaseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testCase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const testCasePopupRoute: Routes = [
    {
        path: 'test-case-new',
        component: TestCasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testCase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-case/:id/edit',
        component: TestCasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testCase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-case/:id/delete',
        component: TestCaseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testCase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
