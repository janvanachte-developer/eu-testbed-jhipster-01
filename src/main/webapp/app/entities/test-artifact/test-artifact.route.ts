import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TestArtifactComponent } from './test-artifact.component';
import { TestArtifactDetailComponent } from './test-artifact-detail.component';
import { TestArtifactPopupComponent } from './test-artifact-dialog.component';
import { TestArtifactDeletePopupComponent } from './test-artifact-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TestArtifactResolvePagingParams implements Resolve<any> {

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

export const testArtifactRoute: Routes = [
    {
        path: 'test-artifact',
        component: TestArtifactComponent,
        resolve: {
            'pagingParams': TestArtifactResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testArtifact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'test-artifact/:id',
        component: TestArtifactDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testArtifact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const testArtifactPopupRoute: Routes = [
    {
        path: 'test-artifact-new',
        component: TestArtifactPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testArtifact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-artifact/:id/edit',
        component: TestArtifactPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testArtifact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'test-artifact/:id/delete',
        component: TestArtifactDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.testArtifact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
