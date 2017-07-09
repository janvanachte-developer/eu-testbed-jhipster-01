import { BaseEntity } from './../../shared';

export class Specification implements BaseEntity {
    constructor(
        public id?: number,
        public domainId?: number,
        public metaDataId?: number,
        public testSuites?: BaseEntity[],
    ) {
    }
}
