import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestbedSharedModule } from '../../shared';
import {
    MetaDataService,
    MetaDataPopupService,
    MetaDataComponent,
    MetaDataDetailComponent,
    MetaDataDialogComponent,
    MetaDataPopupComponent,
    MetaDataDeletePopupComponent,
    MetaDataDeleteDialogComponent,
    metaDataRoute,
    metaDataPopupRoute,
    MetaDataResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...metaDataRoute,
    ...metaDataPopupRoute,
];

@NgModule({
    imports: [
        TestbedSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MetaDataComponent,
        MetaDataDetailComponent,
        MetaDataDialogComponent,
        MetaDataDeleteDialogComponent,
        MetaDataPopupComponent,
        MetaDataDeletePopupComponent,
    ],
    entryComponents: [
        MetaDataComponent,
        MetaDataDialogComponent,
        MetaDataPopupComponent,
        MetaDataDeleteDialogComponent,
        MetaDataDeletePopupComponent,
    ],
    providers: [
        MetaDataService,
        MetaDataPopupService,
        MetaDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedMetaDataModule {}
