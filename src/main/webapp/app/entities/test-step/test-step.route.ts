import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TestStepComponent } from './test-step.component';
import { TestStepDetailComponent } from './test-step-detail.component';
import { TestStepPopupComponent } from './test-step-dialog.component';
import { TestStepDeletePopupComponent } from './test-step-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TestStepResolvePagingParams implements Resolve<any> {

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

export const testStepRoute: Routes = [
    {
        path: 'test-step',
        component: TestStepComponent,
        resolve: {
            'pagingParams': TestStepResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testStep.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'test-step/:id',
        component: TestStepDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testStep.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const testStepPopupRoute: Routes = [
    {
        path: 'test-step-new',
        component: TestStepPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testStep.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-step/:id/edit',
        component: TestStepPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testStep.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-step/:id/delete',
        component: TestStepDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testStep.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
