import { BaseEntity } from './../../shared';

export class TestStep implements BaseEntity {
    constructor(
        public id?: number,
        public testCaseId?: number,
        public testArtifacts?: BaseEntity[],
    ) {
    }
}
