import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ActorComponent } from './actor.component';
import { ActorDetailComponent } from './actor-detail.component';
import { ActorPopupComponent } from './actor-dialog.component';
import { ActorDeletePopupComponent } from './actor-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ActorResolvePagingParams implements Resolve<any> {

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

export const actorRoute: Routes = [
    {
        path: 'actor',
        component: ActorComponent,
        resolve: {
            'pagingParams': ActorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'actor/:id',
        component: ActorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actorPopupRoute: Routes = [
    {
        path: 'actor-new',
        component: ActorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'actor/:id/edit',
        component: ActorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'actor/:id/delete',
        component: ActorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testbedApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
