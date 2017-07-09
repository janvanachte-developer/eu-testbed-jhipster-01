import { BaseEntity } from './../../shared';

export class TestCase implements BaseEntity {
    constructor(
        public id?: number,
        public testSuiteId?: number,
        public metaDataId?: number,
        public steps?: BaseEntity[],
    ) {
    }
}
