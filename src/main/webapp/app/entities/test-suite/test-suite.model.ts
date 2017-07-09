import { BaseEntity } from './../../shared';

export class TestSuite implements BaseEntity {
    constructor(
        public id?: number,
        public specificationId?: number,
        public metaDataId?: number,
        public actors?: BaseEntity[],
        public testCases?: BaseEntity[],
    ) {
    }
}
