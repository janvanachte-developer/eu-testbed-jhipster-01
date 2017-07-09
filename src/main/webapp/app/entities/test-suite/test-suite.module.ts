import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    TestSuiteService,
    TestSuitePopupService,
    TestSuiteComponent,
    TestSuiteDetailComponent,
    TestSuiteDialogComponent,
    TestSuitePopupComponent,
    TestSuiteDeletePopupComponent,
    TestSuiteDeleteDialogComponent,
    testSuiteRoute,
    testSuitePopupRoute,
    TestSuiteResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...testSuiteRoute,
    ...testSuitePopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TestSuiteComponent,
        TestSuiteDetailComponent,
        TestSuiteDialogComponent,
        TestSuiteDeleteDialogComponent,
        TestSuitePopupComponent,
        TestSuiteDeletePopupComponent,
    ],
    entryComponents: [
        TestSuiteComponent,
        TestSuiteDialogComponent,
        TestSuitePopupComponent,
        TestSuiteDeleteDialogComponent,
        TestSuiteDeletePopupComponent,
    ],
    providers: [
        TestSuiteService,
        TestSuitePopupService,
        TestSuiteResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedTestSuiteModule {}
