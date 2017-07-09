import { BaseEntity } from './../../shared';

export class TestArtifact implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public type?: string,
        public encofing?: string,
        public uri?: string,
        public content?: any,
        public testStepId?: number,
    ) {
    }
}
