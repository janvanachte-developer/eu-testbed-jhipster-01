import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TestbedMetaDataModule } from './meta-data/meta-data.module';
import { TestbedActorModule } from './actor/actor.module';
import { TestbedDomainModule } from './domain/domain.module';
import { TestbedSpecificationModule } from './specification/specification.module';
import { TestbedTestSuiteModule } from './test-suite/test-suite.module';
import { TestbedTestCaseModule } from './test-case/test-case.module';
import { TestbedTestStepModule } from './test-step/test-step.module';
import { TestbedTestArtifactModule } from './test-artifact/test-artifact.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TestbedMetaDataModule,
        TestbedActorModule,
        TestbedDomainModule,
        TestbedSpecificationModule,
        TestbedTestSuiteModule,
        TestbedTestCaseModule,
        TestbedTestStepModule,
        TestbedTestArtifactModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestbedEntityModule {}
