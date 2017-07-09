import { BaseEntity } from './../../shared';

export class Domain implements BaseEntity {
    constructor(
        public id?: number,
        public metaDataId?: number,
        public specifications?: BaseEntity[],
    ) {
    }
}
