import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    SpecificationService,
    SpecificationPopupService,
    SpecificationComponent,
    SpecificationDetailComponent,
    SpecificationDialogComponent,
    SpecificationPopupComponent,
    SpecificationDeletePopupComponent,
    SpecificationDeleteDialogComponent,
    specificationRoute,
    specificationPopupRoute,
    SpecificationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...specificationRoute,
    ...specificationPopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SpecificationComponent,
        SpecificationDetailComponent,
        SpecificationDialogComponent,
        SpecificationDeleteDialogComponent,
        SpecificationPopupComponent,
        SpecificationDeletePopupComponent,
    ],
    entryComponents: [
        SpecificationComponent,
        SpecificationDialogComponent,
        SpecificationPopupComponent,
        SpecificationDeleteDialogComponent,
        SpecificationDeletePopupComponent,
    ],
    providers: [
        SpecificationService,
        SpecificationPopupService,
        SpecificationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedSpecificationModule {}
