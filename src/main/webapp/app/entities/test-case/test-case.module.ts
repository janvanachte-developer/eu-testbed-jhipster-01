import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    TestCaseService,
    TestCasePopupService,
    TestCaseComponent,
    TestCaseDetailComponent,
    TestCaseDialogComponent,
    TestCasePopupComponent,
    TestCaseDeletePopupComponent,
    TestCaseDeleteDialogComponent,
    testCaseRoute,
    testCasePopupRoute,
    TestCaseResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...testCaseRoute,
    ...testCasePopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TestCaseComponent,
        TestCaseDetailComponent,
        TestCaseDialogComponent,
        TestCaseDeleteDialogComponent,
        TestCasePopupComponent,
        TestCaseDeletePopupComponent,
    ],
    entryComponents: [
        TestCaseComponent,
        TestCaseDialogComponent,
        TestCasePopupComponent,
        TestCaseDeleteDialogComponent,
        TestCaseDeletePopupComponent,
    ],
    providers: [
        TestCaseService,
        TestCasePopupService,
        TestCaseResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedTestCaseModule {}
