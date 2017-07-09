import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    TestArtifactService,
    TestArtifactPopupService,
    TestArtifactComponent,
    TestArtifactDetailComponent,
    TestArtifactDialogComponent,
    TestArtifactPopupComponent,
    TestArtifactDeletePopupComponent,
    TestArtifactDeleteDialogComponent,
    testArtifactRoute,
    testArtifactPopupRoute,
    TestArtifactResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...testArtifactRoute,
    ...testArtifactPopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TestArtifactComponent,
        TestArtifactDetailComponent,
        TestArtifactDialogComponent,
        TestArtifactDeleteDialogComponent,
        TestArtifactPopupComponent,
        TestArtifactDeletePopupComponent,
    ],
    entryComponents: [
        TestArtifactComponent,
        TestArtifactDialogComponent,
        TestArtifactPopupComponent,
        TestArtifactDeleteDialogComponent,
        TestArtifactDeletePopupComponent,
    ],
    providers: [
        TestArtifactService,
        TestArtifactPopupService,
        TestArtifactResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedTestArtifactModule {}
