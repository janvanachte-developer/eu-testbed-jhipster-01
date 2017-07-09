import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    TestStepService,
    TestStepPopupService,
    TestStepComponent,
    TestStepDetailComponent,
    TestStepDialogComponent,
    TestStepPopupComponent,
    TestStepDeletePopupComponent,
    TestStepDeleteDialogComponent,
    testStepRoute,
    testStepPopupRoute,
    TestStepResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...testStepRoute,
    ...testStepPopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TestStepComponent,
        TestStepDetailComponent,
        TestStepDialogComponent,
        TestStepDeleteDialogComponent,
        TestStepPopupComponent,
        TestStepDeletePopupComponent,
    ],
    entryComponents: [
        TestStepComponent,
        TestStepDialogComponent,
        TestStepPopupComponent,
        TestStepDeleteDialogComponent,
        TestStepDeletePopupComponent,
    ],
    providers: [
        TestStepService,
        TestStepPopupService,
        TestStepResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedTestStepModule {}
