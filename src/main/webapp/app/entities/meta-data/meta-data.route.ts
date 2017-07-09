import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MetaDataComponent } from './meta-data.component';
import { MetaDataDetailComponent } from './meta-data-detail.component';
import { MetaDataPopupComponent } from './meta-data-dialog.component';
import { MetaDataDeletePopupComponent } from './meta-data-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class MetaDataResolvePagingParams implements Resolve<any> {

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

export const metaDataRoute: Routes = [
    {
        path: 'meta-data',
        component: MetaDataComponent,
        resolve: {
            'pagingParams': MetaDataResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.metaData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'meta-data/:id',
        component: MetaDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.metaData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const metaDataPopupRoute: Routes = [
    {
        path: 'meta-data-new',
        component: MetaDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.metaData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meta-data/:id/edit',
        component: MetaDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.metaData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'meta-data/:id/delete',
        component: MetaDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.metaData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
