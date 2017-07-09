import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    ActorService,
    ActorPopupService,
    ActorComponent,
    ActorDetailComponent,
    ActorDialogComponent,
    ActorPopupComponent,
    ActorDeletePopupComponent,
    ActorDeleteDialogComponent,
    actorRoute,
    actorPopupRoute,
    ActorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...actorRoute,
    ...actorPopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ActorComponent,
        ActorDetailComponent,
        ActorDialogComponent,
        ActorDeleteDialogComponent,
        ActorPopupComponent,
        ActorDeletePopupComponent,
    ],
    entryComponents: [
        ActorComponent,
        ActorDialogComponent,
        ActorPopupComponent,
        ActorDeleteDialogComponent,
        ActorDeletePopupComponent,
    ],
    providers: [
        ActorService,
        ActorPopupService,
        ActorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedActorModule {}
